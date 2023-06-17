package com.nandaiqbalh.currencyconverter.data.network

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.nandaiqbalh.currencyconverter.data.network.models.CurrencyResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface CurrencyApi {

	@GET("/latest/")
	suspend fun getRates(
		@Query("base") base:String
	) : Response<CurrencyResponse>

	companion object {
		private const val API_KEY =  "e7ca40569e2c0d48f831e72895523e56"
		private const val BASE_URL = "http://api.exchangeratesapi.io/v1/"

		@JvmStatic
		operator fun invoke(chukerInterceptor: ChuckerInterceptor): CurrencyApi {
			val authInterceptor = Interceptor {
				val originRequest = it.request()
				val oldUrl = originRequest.url
				val newUrl = oldUrl.newBuilder().apply {
					addQueryParameter("access_key", API_KEY)
				}.build()
				it.proceed(originRequest.newBuilder().url(newUrl).build())
			}

			val okHttpClient = OkHttpClient.Builder()
				.addInterceptor(chukerInterceptor)
				.addInterceptor(authInterceptor)
				.connectTimeout(120, TimeUnit.SECONDS)
				.readTimeout(120, TimeUnit.SECONDS)
				.build()

			return Retrofit.Builder()
				.baseUrl(BASE_URL)
				.addConverterFactory(GsonConverterFactory.create())
				.client(okHttpClient)
				.build()
				.create(CurrencyApi::class.java)
		}
	}
}