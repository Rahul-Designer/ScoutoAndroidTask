package com.example.scoutoandroidtask.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CarDao {
    @Query("SELECT * FROM car_details ORDER BY ID ASC")
    fun getCar() : LiveData<List<Car>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCar(car: Car)

    @Delete
    fun deleteCar(car: Car)

    @Update
    fun updateCarImage(car: Car)
}