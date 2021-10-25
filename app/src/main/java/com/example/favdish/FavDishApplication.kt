package com.example.favdish

import android.app.Application
import com.example.favdish.database.FavDishDatabase

import com.example.favdish.repository.FavDishRepository
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FavDishApplication: Application()