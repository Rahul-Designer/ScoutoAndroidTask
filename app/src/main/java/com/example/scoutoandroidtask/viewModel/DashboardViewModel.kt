package com.example.scoutoandroidtask.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scoutoandroidtask.model.ApiOneList
import com.example.scoutoandroidtask.model.MakerList
import com.example.scoutoandroidtask.repository.JsonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashboardViewModel(private val jsonRepository: JsonRepository) : ViewModel() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            jsonRepository.getJsonData()

        }
    }

    val makers: LiveData<ApiOneList>
        get() = jsonRepository.jsonMakersData
}