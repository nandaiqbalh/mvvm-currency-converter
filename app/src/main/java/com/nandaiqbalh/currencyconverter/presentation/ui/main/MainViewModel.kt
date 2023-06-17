package com.nandaiqbalh.currencyconverter.presentation.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nandaiqbalh.currencyconverter.data.network.models.Rates
import com.nandaiqbalh.currencyconverter.data.network.repository.MainRepository
import com.nandaiqbalh.currencyconverter.util.DispatcherProvider
import com.nandaiqbalh.currencyconverter.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.round

class MainViewModel @Inject constructor(
	private val mainRepository: MainRepository,
	private val dispatcherProvider: DispatcherProvider
): ViewModel() {

	sealed class CurrencyEvent{
		class Success(val resultText: String): CurrencyEvent()
		class Failure(val errorText: String) : CurrencyEvent()
		object Loading :CurrencyEvent()
		object Empty : CurrencyEvent()
	}

	private val _conversion = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
	val conversion : StateFlow<CurrencyEvent> = _conversion

	fun convert(
		amountStr: String,
		fromCurrency: String,
		toCurrency: String
	) {
		val fromAmount = amountStr.toFloatOrNull()
		if (fromAmount  == null) {
			_conversion.value = CurrencyEvent.Failure("Not a valid amount!")
			return
		}

		viewModelScope.launch(dispatcherProvider.io) {
			_conversion.value = CurrencyEvent.Loading

			when(val ratesResponse = mainRepository.getRates(fromCurrency)){

				is Resource.Error -> _conversion.value = CurrencyEvent.Failure(ratesResponse.message!!)

				is Resource.Success -> {
					val rates  = ratesResponse.data!!.rates
					val rate = getRateForCurrency(fromCurrency, rates)

					if (rate == null){
						_conversion.value = CurrencyEvent.Failure("Unexpected error occured!")
					} else{
						val convertedCurrency = (Math.round(fromAmount * rate * 100) / 100.0).toFloat()
						_conversion.value = CurrencyEvent.Success(
							"$fromAmount $fromCurrency = $convertedCurrency $toCurrency "
						)
					}
				}
			}
		}
	}

	private fun getRateForCurrency(currency: String, rates: Rates) = when (currency) {
		"CAD" -> rates.CAD
		"HKD" -> rates.HKD
		"ISK" -> rates.ISK
		"EUR" -> rates.EUR
		"PHP" -> rates.PHP
		"DKK" -> rates.DKK
		"HUF" -> rates.HUF
		"CZK" -> rates.CZK
		"AUD" -> rates.AUD
		"RON" -> rates.RON
		"SEK" -> rates.SEK
		"IDR" -> rates.IDR
		"INR" -> rates.INR
		"BRL" -> rates.BRL
		"RUB" -> rates.RUB
		"HRK" -> rates.HRK
		"JPY" -> rates.JPY
		"THB" -> rates.THB
		"CHF" -> rates.CHF
		"SGD" -> rates.SGD
		"PLN" -> rates.PLN
		"BGN" -> rates.BGN
		"CNY" -> rates.CNY
		"NOK" -> rates.NOK
		"NZD" -> rates.NZD
		"ZAR" -> rates.ZAR
		"USD" -> rates.USD
		"MXN" -> rates.MXN
		"ILS" -> rates.ILS
		"GBP" -> rates.GBP
		"KRW" -> rates.KRW
		"MYR" -> rates.MYR
		else -> null
	}
}