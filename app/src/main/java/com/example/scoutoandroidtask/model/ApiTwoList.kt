package com.example.scoutoandroidtask.model

data class ApiTwoList(
    val Count: Int,
    val Message: String,
    val Results: List<ModelList>,
    val SearchCriteria: String
)