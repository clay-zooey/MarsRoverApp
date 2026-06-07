package com.zooeydigital.marsrover.data.repository

import com.zooeydigital.marsrover.core.dispatchers.AppDispatchers
import com.zooeydigital.marsrover.data.marsvista.MarsVistaApi
import com.zooeydigital.marsrover.data.marsvista.MarsVistaPhotoMapper
import com.zooeydigital.marsrover.data.marsvista.MarsVistaRoverMapper
import com.zooeydigital.marsrover.domain.model.MarsRover
import com.zooeydigital.marsrover.domain.model.MarsPhoto
import com.zooeydigital.marsrover.domain.repository.MarsRoverRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

@Singleton
class MarsRoverRepositoryImpl @Inject constructor(
    private val api: MarsVistaApi,
    private val appDispatchers: AppDispatchers,
) : MarsRoverRepository {
    // Thread-safe in-memory cache for static rover metadata
    private var cachedRovers: List<MarsRover>? = null

    override fun getRovers(): Flow<List<MarsRover>> = flow {
        // TODO: Implement offline local data persistence using Room DB to cache rovers
        // Return cached metadata instantly to avoid redundant over-fetching
        cachedRovers?.let {
            emit(it)
            return@flow
        }

        val rovers = MarsVistaRoverMapper.map(api.getRovers())
        val roversWithCameras = coroutineScope {
            rovers.map { rover ->
                async {
                    val fullRover = runCatching {
                        api.getRover(rover.id).data
                    }.getOrNull()

                    val mappedRover = fullRover?.let {
                        MarsVistaRoverMapper.mapSingle(it)
                    } ?: rover

                    mappedRover
                }
            }.awaitAll()
        }

        cachedRovers = roversWithCameras
        emit(roversWithCameras)
    }.flowOn(appDispatchers.io)

    override fun getPhotos(roverId: String, date: String, page: Int): Flow<List<MarsPhoto>> = flow {
        val photos = api.getPhotosForDate(roverId = roverId, earthDate = date, page = page)
        emit(MarsVistaPhotoMapper.map(photos))
    }.flowOn(appDispatchers.io)
}
