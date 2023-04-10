package com.example.scoutoandroidtask.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.scoutoandroidtask.api.JsonService
import com.example.scoutoandroidtask.model.ApiOneList
import com.example.scoutoandroidtask.model.MakerList

class JsonRepository( private val jsonService: JsonService) {

    private val jsonLiveMakersData = MutableLiveData<ApiOneList>()

    val jsonMakersData: LiveData<ApiOneList>
        get() = jsonLiveMakersData

    suspend fun getJsonData() {
        val result = jsonService.getMakerData()
        if (result.body() != null) {
            jsonLiveMakersData.postValue(result?.body())
        }
    }
}