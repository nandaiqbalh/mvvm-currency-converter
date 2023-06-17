package com.nandaiqbalh.currencyconverter

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.nandaiqbalh.currencyconverter.databinding.ActivityMainBinding
import com.nandaiqbalh.currencyconverter.presentation.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

	private var _binding: ActivityMainBinding? = null
	private val binding get() = _binding!!

	private val viewModel: MainViewModel by viewModels()
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		_binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		convertionProcess()

	}

	private fun convertionProcess(){

		binding.btnConvert.setOnClickListener {

			viewModel.convert(
				binding.etFrom.text.toString(),
				binding.spFromCurrency.selectedItem.toString(),
				binding.spToCurrency.selectedItem.toString()
			)

		}

		lifecycleScope.launchWhenStarted {
			viewModel.conversion.collect{ event ->

				when(event){

					is  MainViewModel.CurrencyEvent.Success -> {
						binding.progressBar.isVisible = false
						binding.tvResult.setTextColor(Color.BLACK)
						binding.tvResult.text = event.resultText
					}

					is MainViewModel.CurrencyEvent.Failure -> {
						binding.progressBar.isVisible = false
						binding.tvResult.setTextColor(Color.RED)
						binding.tvResult.text = event.errorText
					}

					is MainViewModel.CurrencyEvent.Loading -> {
						binding.progressBar.isVisible = true
					}

					else -> Unit

				}
			}
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		_binding = null
	}
}