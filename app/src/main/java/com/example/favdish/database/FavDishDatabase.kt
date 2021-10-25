package com.example.favdish.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.favdish.models.FavDish


@Database(entities = [FavDish::class], version = 1, exportSchema = false)
abstract class FavDishDatabase: RoomDatabase() {

    abstract fun getFavDishDao(): FavDishDao

}