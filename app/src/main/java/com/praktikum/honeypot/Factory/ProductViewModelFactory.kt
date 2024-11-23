package com.praktikum.honeypot.Factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.praktikum.honeypot.ViewModel.ProductViewModel

class ProductViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}