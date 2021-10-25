package com.example.favdish.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.favdish.models.FavDish
import com.example.favdish.models.networkmodel.RandomRecipesResponce
import com.example.favdish.repository.FavDishRepository
import com.example.favdish.utiles.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomDishViewModel
@Inject constructor(
    private val repository: FavDishRepository
) : ViewModel() {


    private val _randomDish = MutableLiveData<Resource<RandomRecipesResponce>>()
    val randomDish: LiveData<Resource<RandomRecipesResponce>> get() = _randomDish

    init {
        getRandomDish()
    }

    fun getRandomDish() {
        viewModelScope.launch {
            _randomDish.postValue(Resource.Loading())
            val response = repository.getRandomDish()
            _randomDish.postValue(response)
        }
    }

    fun addDishTODatabase(favDish: FavDish) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertDish(favDish)
        }
    }


}