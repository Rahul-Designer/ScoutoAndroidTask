package com.example.scoutoandroidtask.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.scoutoandroidtask.repository.JsonRepository

class DashboardViewModelFactory(private val jsonRepository: JsonRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DashboardViewModel(jsonRepository) as T
    }
}