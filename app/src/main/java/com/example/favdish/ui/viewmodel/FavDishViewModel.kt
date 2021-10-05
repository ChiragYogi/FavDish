package com.example.favdish.ui.viewmodel


import android.graphics.Bitmap
import androidx.lifecycle.*
import com.example.favdish.models.FavDish

import com.example.favdish.repository.FavDishRepository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FavDishViewModel(private val repository: FavDishRepository): ViewModel() {

    val allDishData: LiveData<List<FavDish>> = repository.getAllDish().asLiveData()
    val favoriteDishData: LiveData<List<FavDish>> = repository.getFavoriteDish().asLiveData()

    fun insertDishToDatabase(favDish: FavDish){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertDish(favDish)
        }
    }

    fun updateDishToDatabase(favDish: FavDish){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateDish(favDish)
        }
    }

    fun deleteDishToDatabase(favDish: FavDish){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteDish(favDish)
        }
    }



    fun getDishByType(query: String): LiveData<List<FavDish>> =
          repository.getDishByType(query).asLiveData()

        }















