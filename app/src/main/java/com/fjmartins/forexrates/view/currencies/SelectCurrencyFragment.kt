package com.fjmartins.forexrates.view.currencies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fjmartins.forexrates.databinding.FragmentSelectCurrencyBinding
import com.fjmartins.forexrates.di.Injectable
import com.fjmartins.forexrates.view.rates.RatesViewModel
import javax.inject.Inject

class SelectCurrencyFragment : Fragment(), Injectable {

    companion object {
        fun newInstance() = SelectCurrencyFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: RatesViewModel
    private lateinit var binding: FragmentSelectCurrencyBinding
    private var selectedCurrency: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectCurrencyBinding.inflate(inflater, container, false)
        activity?.run { // Instantiate viewModel from the activity's context so that we can get this same instance from other fragments
            viewModel = ViewModelProvider(this, viewModelFactory).get(RatesViewModel::class.java)
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

        binding.currencySpinner

        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            ArrayList<String>()
        )

        binding.currencySpinner.run {
            adapter = spinnerAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {}
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    selectedCurrency = p2

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

            viewModel.setAmount(amount)
        }
    }
}