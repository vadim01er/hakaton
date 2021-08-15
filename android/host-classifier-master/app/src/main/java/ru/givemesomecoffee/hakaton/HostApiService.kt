package ru.givemesomecoffee.hakaton

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL =
    "https://45c16337d95b.ngrok.io"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface HostApiService {
    @GET("check")
    suspend fun getHostType(@Query("value") host: String): String
}

object HostApi {
    val retrofitService: HostApiService by lazy {
        retrofit.create(HostApiService::class.java)
    }
}


