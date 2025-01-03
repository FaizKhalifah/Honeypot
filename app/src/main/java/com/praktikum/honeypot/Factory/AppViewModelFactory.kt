package com.praktikum.honeypot.Factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.praktikum.honeypot.ViewModel.AuthViewModel
import com.praktikum.honeypot.ViewModel.HomeViewModel
import com.praktikum.honeypot.ViewModel.PartnerViewModel
import com.praktikum.honeypot.ViewModel.ProductViewModel
import com.praktikum.honeypot.ViewModel.ProfileViewModel
import com.praktikum.honeypot.ViewModel.SalesViewModel

class AppViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ProductViewModel::class.java) -> {
                ProductViewModel(context) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(context) as T
            }
            modelClass.isAssignableFrom(PartnerViewModel::class.java) -> {
                PartnerViewModel(context) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(context) as T
            }
            modelClass.isAssignableFrom(SalesViewModel::class.java)->{
                SalesViewModel(context) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
