package org.xhanka.ndm_project.data.api

import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import okhttp3.OkHttpClient
import org.xhanka.ndm_project.utils.Constants


class WeatherApiSource @Inject constructor(){
    private lateinit var weatherApiService: WeatherApiService

    fun getNewsApiService(): WeatherApiService {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor).build()

        if (!::weatherApiService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) // THIS IS REQUIRED APPARENTLY
                .client(client)
                .build()

            weatherApiService = retrofit.create(WeatherApiService::class.java)
        }

        return weatherApiService
    }
}