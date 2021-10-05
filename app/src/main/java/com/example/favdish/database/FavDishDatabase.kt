package com.example.favdish.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.favdish.models.FavDish


@Database(entities = [FavDish::class], version = 1, exportSchema = false)
abstract class FavDishDatabase: RoomDatabase() {

    abstract fun getFavDishDao(): FavDishDao

    companion object{

        //Singleton  prevent multiple instance of  database opening at same time
        @Volatile
        private var INSISTENCE: FavDishDatabase? = null

        fun getDatabase(context: Context): FavDishDatabase {

            //if instance is null then create database if not null then return instance
            return INSISTENCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavDishDatabase::class.java,
                    "fav_dish_database"
                ).build()

                INSISTENCE = instance
                return INSISTENCE!!
            }


        }
    }
}