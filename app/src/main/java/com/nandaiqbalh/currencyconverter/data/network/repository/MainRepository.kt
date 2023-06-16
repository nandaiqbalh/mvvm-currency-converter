package com.nandaiqbalh.currencyconverter.data.network.repository

import com.nandaiqbalh.currencyconverter.data.network.CurrencyApi
import com.nandaiqbalh.currencyconverter.data.network.models.CurrencyResponse
import com.nandaiqbalh.currencyconverter.util.Resource
import javax.inject.Inject

interface MainRepository {

	suspend fun getRates(base: String): Resource<CurrencyResponse?>
}

class MainRepositoryImpl @Inject constructor(
	private val currencyApi: CurrencyApi
) : MainRepository {
	override suspend fun getRates(base: String): Resource<CurrencyResponse?> {
		return try {
			val response = currencyApi.getRates(base = base)
			val result = response.body()

			if (response.isSuccessful && response != null){
				Resource.Success(result)
			} else {
				Resource.Error(response.message())
			}

		} catch (e: Exception) {
			Resource.Error(e.message ?: "An error occured!")
		}
	}
}