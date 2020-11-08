package com.fjmartins.forexrates.view.currencies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.fjmartins.forexrates.R
import com.fjmartins.forexrates.databinding.FragmentCurrencyBinding
import com.fjmartins.forexrates.di.Injectable
import com.fjmartins.forexrates.model.ConversionHelper
import javax.inject.Inject

class CurrencyFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: CurrencyViewModel
    private lateinit var binding: FragmentCurrencyBinding
    private var selectedCurrency: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCurrencyBinding.inflate(inflater, container, false)
        activity?.run { // Instantiate viewModel from the activity's context so that we can get this same instance from other fragments
            viewModel = ViewModelProvider(this, viewModelFactory).get(CurrencyViewModel::class.java)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCurrencies()

        binding.apply {
            this.viewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            ArrayList<String>()
        )

        binding.currencySpinner.apply {
            adapter = spinnerAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    v: View?,
                    position: Int,
                    p3: Long
                ) {
                    selectedCurrency = position
                    viewModel.setSelectedCurrency(selectedCurrency)
                }
            }
        }

        viewModel.currencies.observe(viewLifecycleOwner, Observer { currencies ->
            spinnerAdapter.clear()
            spinnerAdapter.addAll(currencies.map {
                (it.name + " - " + it.description)
            })
        })

        binding.btnConvert.setOnClickListener {
            var amount = 0.0
            if (binding.amount.text?.isNotEmpty() == true) {
                amount = binding.amount.text.toString().toDouble()
            }

            val convHelper = ConversionHelper().apply {
                this.currency = viewModel.selectedCurrency.name
                this.amount = amount
            }

            val bundle = bundleOf("conversionHelper" to convHelper)
            it.findNavController()
                .navigate(R.id.action_selectCurrencyFragment_to_ratesFragment, bundle)
        }
    }
}