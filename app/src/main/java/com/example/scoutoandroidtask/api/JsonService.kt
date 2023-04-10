package com.example.scoutoandroidtask.api

import com.example.scoutoandroidtask.model.ApiOneList
import com.example.scoutoandroidtask.model.ApiTwoList
import com.example.scoutoandroidtask.model.MakerList
import com.example.scoutoandroidtask.model.ModelList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JsonService {

    //    API 1
    //    https://vpic.nhtsa.dot.gov/api/vehicles/getallmakes?format=json

    @GET("getallmakes")
    suspend fun getMakerData(@Query("format") format: String = "json"): Response<ApiOneList>

    @GET("GetModelsForMakeId/{makeId}")
    suspend fun getModelData(@Path("makeId") makeId : String,@Query("format") format: String = "json") : Response<ApiTwoList>

}