package com.fjmartins.forexrates.view.util.binding

import androidx.databinding.BindingAdapter
import com.google.android.material.textview.MaterialTextView

@BindingAdapter("bind:setCurrency")
fun MaterialTextView.setCurrency(pair: String) {
    this.text = pair.substring(3)
}