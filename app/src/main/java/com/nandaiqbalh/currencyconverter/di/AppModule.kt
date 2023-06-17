package com.nandaiqbalh.currencyconverter.di

import com.nandaiqbalh.currencyconverter.data.network.CurrencyApi
import com.nandaiqbalh.currencyconverter.data.network.repository.MainRepository
import com.nandaiqbalh.currencyconverter.data.network.repository.MainRepositoryImpl
import com.nandaiqbalh.currencyconverter.util.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "http://api.exchangeratesapi.io/v1/"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

	@Singleton
	@Provides
	fun provideCurrencyApi() = Retrofit.Builder()
		.baseUrl(BASE_URL)
		.addConverterFactory(GsonConverterFactory.create())
		.build()
		.create(CurrencyApi::class.java)

	@Singleton
	@Provides
	fun provideMainRepository(currencyApi: CurrencyApi) : MainRepository = MainRepositoryImpl(currencyApi)

	@Singleton
	@Provides
	fun provideDispatchers() : DispatcherProvider = object : DispatcherProvider{
		override val main: CoroutineDispatcher
			get() = Dispatchers.Main
		override val io: CoroutineDispatcher
			get() = Dispatchers.IO
		override val default: CoroutineDispatcher
			get() = Dispatchers.Default
		override val unconfined: CoroutineDispatcher
			get() = Dispatchers.Unconfined
	}
}