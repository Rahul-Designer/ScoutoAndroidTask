package com.example.scoutoandroidtask.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.scoutoandroidtask.model.DashboardListview

@Database(entities = [Car::class], version = 1)
abstract class CarDatabase : RoomDatabase() {

    abstract fun carDao() : CarDao

    companion object {
        @Volatile
        private var INSTANCE: CarDatabase? = null

        fun getDatabase(context: Context): CarDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        CarDatabase::class.java, "Car_Database"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }

}