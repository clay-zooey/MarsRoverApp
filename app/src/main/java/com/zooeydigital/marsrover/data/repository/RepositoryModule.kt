package com.zooeydigital.marsrover.data.repository

import com.zooeydigital.marsrover.domain.repository.MarsRoverRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMarsRoverRepository(
        repository: MarsRoverRepositoryImpl,
    ): MarsRoverRepository
}
