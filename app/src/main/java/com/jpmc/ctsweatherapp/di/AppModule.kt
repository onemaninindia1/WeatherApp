package com.jpmc.ctsweatherapp.di

import com.jpmc.ctsweatherapp.BuildConfig
import com.jpmc.ctsweatherapp.api.WeatherAPI
import com.jpmc.ctsweatherapp.api.WeatherHelper
import com.jpmc.ctsweatherapp.api.WeatherHelperImpl
import com.jpmc.ctsweatherapp.util.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Singleton

private const val API_KEY = "31b77fd22edeaaa693ea80ea5ddf3a63"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    var client: Interceptor = Interceptor { chain ->
        val url = chain.request().url.newBuilder()
            .addQueryParameter("appid", API_KEY)
            .build()
        val newRequest: Request = chain.request().newBuilder()
            .url(url)
            .build()
        chain.proceed(newRequest)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(client)
            .addInterceptor(loggingInterceptor)
            .build()
    } else {
        OkHttpClient
            .Builder()
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit() : Retrofit {
        val okHttpClient: OkHttpClient = provideOkHttpClient()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }
    @Provides
    @Singleton
    fun provideWeatherAPI(retrofit: Retrofit) = retrofit.create(WeatherAPI::class.java)

    @Provides
    @Singleton
    fun provideApiHelper(weatherHelperHelper: WeatherHelperImpl): WeatherHelper = weatherHelperHelper
}