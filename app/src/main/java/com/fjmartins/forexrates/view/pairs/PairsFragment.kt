package com.fjmartins.forexrates.view.pairs

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fjmartins.forexrates.R
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

        pairsViewAdapter = PairsAdapter()

        binding.currenciesRecyclerView.apply {
            setHasFixedSize(true)
            adapter = pairsViewAdapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.pairs.observe(viewLifecycleOwner, Observer { pairs ->
            pairsViewAdapter.setCurrencyPairs(pairs)
        })
    }
}