package com.example.scoutoandroidtask.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "car_details")
data class Car(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val Make_Name: String,
    val Model_Name: String
)
