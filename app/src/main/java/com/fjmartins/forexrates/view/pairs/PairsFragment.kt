package com.fjmartins.forexrates.view.pairs

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fjmartins.forexrates.R
import com.fjmartins.forexrates.di.Injectable
import javax.inject.Inject

class PairsFragment : Fragment(), Injectable {

    companion object {
        fun newInstance() = PairsFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: PairsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.pairs_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PairsViewModel::class.java)

        viewModel.getLiveRates()
    }
}