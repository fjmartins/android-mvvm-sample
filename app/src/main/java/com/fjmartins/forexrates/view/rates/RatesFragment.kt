package com.fjmartins.forexrates.view.rates

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fjmartins.forexrates.databinding.FragmentRatesBinding
import com.fjmartins.forexrates.di.Injectable
import com.fjmartins.forexrates.model.ConversionHelper
import com.fjmartins.forexrates.model.Pair
import com.fjmartins.forexrates.view.rates.adapter.PairsAdapter
import javax.inject.Inject

class RatesFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: RatesViewModel
    private lateinit var binding: FragmentRatesBinding
    private lateinit var pairsViewAdapter: PairsAdapter

    private var conversionHelper: ConversionHelper? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRatesBinding.inflate(inflater, container, false)
        activity?.run { // Instantiate viewModel from the activity's context so that we can get this same instance from other fragments
            viewModel = ViewModelProvider(this, viewModelFactory).get(RatesViewModel::class.java)
        }

        conversionHelper = arguments?.getParcelable("conversionHelper")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pairsViewAdapter = PairsAdapter()
        pairsViewAdapter.setCurrencyPairs(ArrayList<Pair>().toList())

        binding.apply {
            this.viewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
            ratesRecyclerView.apply {
                setHasFixedSize(true)
                adapter = pairsViewAdapter
                layoutManager = LinearLayoutManager(context)
            }
        }

        conversionHelper?.run {
            viewModel.calculateRates(this.currency.orEmpty(), this.amount)
        }

        viewModel.pairs.observe(viewLifecycleOwner, Observer { pairs ->
            pairsViewAdapter.setCurrencyPairs(pairs)
        })
    }
}