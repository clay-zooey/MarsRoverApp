package com.zooeydigital.marsrover.data.marsvista

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object MarsVistaApiModule {
    @Provides
    @Singleton
    fun provideMarsVistaApi(retrofit: Retrofit): MarsVistaApi =
        retrofit.create(MarsVistaApi::class.java)
}
