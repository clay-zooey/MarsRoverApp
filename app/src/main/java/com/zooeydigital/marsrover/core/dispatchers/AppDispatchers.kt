package com.zooeydigital.marsrover.core.dispatchers

import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

class AppDispatchers @Inject constructor(
    @param:DefaultDispatcher val default: CoroutineDispatcher,
    @param:IoDispatcher val io: CoroutineDispatcher,
    @param:MainDispatcher val main: CoroutineDispatcher,
)
