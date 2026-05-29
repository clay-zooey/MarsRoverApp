package com.zooeydigital.marsrover.core.common

import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

data class AppDispatchers @Inject constructor(
    @param:DefaultDispatcher val default: CoroutineDispatcher,
    @param:IoDispatcher val io: CoroutineDispatcher,
    @param:MainDispatcher val main: CoroutineDispatcher,
)
