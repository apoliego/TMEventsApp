package com.ticketmaster.data.di

import com.ticketmaster.data.network.api.EventsApi
import com.ticketmaster.data.network.api.EventsApi.Companion.API_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS) // Tiempo para establecer conexión
        .readTimeout(15, TimeUnit.SECONDS)   // Tiempo para leer la respuesta
        .writeTimeout(15, TimeUnit.SECONDS)  // Tiempo para escribir datos
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideEventsApi(retrofit: Retrofit): EventsApi =
        retrofit.create(EventsApi::class.java)
}