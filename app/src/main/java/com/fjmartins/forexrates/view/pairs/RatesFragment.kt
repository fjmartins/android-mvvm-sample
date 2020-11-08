package com.fjmartins.forexrates.view.pairs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fjmartins.forexrates.databinding.RatesFragmentBinding
import com.fjmartins.forexrates.di.Injectable
import com.fjmartins.forexrates.view.pairs.adapter.PairsAdapter
import javax.inject.Inject

class RatesFragment : Fragment(), Injectable {

    companion object {
        fun newInstance() = RatesFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: RatesViewModel
    private lateinit var binding: RatesFragmentBinding
    private lateinit var pairsViewAdapter: PairsAdapter

    private var selectedCurrency: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RatesFragmentBinding.inflate(inflater, container, false)
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
            context!!,
            android.R.layout.simple_dropdown_item_1line,
            ArrayList<String>()
        )

        binding.currencySpinner.apply {
            adapter = spinnerAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {}
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    selectedCurrency = p2

                    viewModel.setSelectedCurrency(selectedCurrency)
                }
            }
        }

        pairsViewAdapter = PairsAdapter()

        binding.ratesRecyclerView.apply {
            setHasFixedSize(true)
            adapter = pairsViewAdapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.currencies.observe(viewLifecycleOwner, Observer { currencies ->
            spinnerAdapter.clear()
            spinnerAdapter.addAll(currencies.map {
                (it.name + " - " + it.description)
            })
        })

        viewModel.pairs.observe(viewLifecycleOwner, Observer { pairs ->
            pairsViewAdapter.setCurrencyPairs(pairs)
        })

        binding.loadingIndicator.animate()
    }
}