package com.example.scoutoandroidtask.model

data class ApiOneList(
    val Count: Int,
    val Message: String,
    val Results: List<MakerList>,
    val SearchCriteria: Any
)