package com.fjmartins.forexrates.view.pairs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fjmartins.forexrates.databinding.PairsFragmentBinding
import com.fjmartins.forexrates.di.Injectable
import com.fjmartins.forexrates.view.pairs.adapter.PairsAdapter
import javax.inject.Inject

class PairsFragment : Fragment(), Injectable {

    companion object {
        fun newInstance() = PairsFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: PairsViewModel
    private lateinit var binding: PairsFragmentBinding
    private lateinit var pairsViewAdapter: PairsAdapter

    private var selectedCurrency: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PairsFragmentBinding.inflate(inflater, container, false)
        activity?.run { // Instantiate viewModel from the activity's context so that we can get this same instance from other fragments
            viewModel = ViewModelProvider(this, viewModelFactory).get(PairsViewModel::class.java)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveRates()

        binding.apply {
            this.viewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        binding.currencySpinner

        val spinnerAdapter = ArrayAdapter(context!!, android.R.layout.simple_dropdown_item_1line, ArrayList<String>())
        binding.currencySpinner.adapter = spinnerAdapter

        pairsViewAdapter = PairsAdapter()

        binding.currenciesRecyclerView.apply {
            setHasFixedSize(true)
            adapter = pairsViewAdapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.pairs.observe(viewLifecycleOwner, Observer { pairs ->
            pairsViewAdapter.setCurrencyPairs(pairs)

            spinnerAdapter.clear()
            spinnerAdapter.addAll(pairs.map {
                it.toString()
            })
        })

        binding.loadingIndicator.animate()
    }
}