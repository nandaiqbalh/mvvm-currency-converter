package com.nandaiqbalh.currencyconverter.data.network

import com.nandaiqbalh.currencyconverter.data.network.models.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {

	@GET("/latest/")
	suspend fun getRates(
		@Query("access_key") access_key: String? = API_KEY,
		@Query("base") base:String
	) : Response<CurrencyResponse>

	companion object{
		private const val API_KEY =  "e7ca40569e2c0d48f831e72895523e56"

	}
}