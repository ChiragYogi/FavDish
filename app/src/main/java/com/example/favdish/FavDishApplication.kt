package com.example.favdish

import android.app.Application
import com.example.favdish.database.FavDishDatabase
import com.example.favdish.network.FavDishApiClient

import com.example.favdish.repository.FavDishRepository

class FavDishApplication: Application() {

    private val database by lazy { FavDishDatabase.getDatabase(this) }


    val repository by lazy { FavDishRepository(database.getFavDishDao()) }

}