package com.example.favdish.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.example.favdish.models.FavDish
import com.example.favdish.models.networkmodel.RandomRecipesResponce
import com.example.favdish.models.networkmodel.Recipe
import com.example.favdish.utiles.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class FavDishRepositoryAndroidTest: FavDishImpl {

    private val allDish = mutableListOf<FavDish>()
    private val favoriteDish = mutableListOf<FavDish>()
    private val getDishByQuery = mutableListOf<FavDish>()
    private val randomeDIsh = listOf<Recipe>()

    private val observAllDish = MutableLiveData<List<FavDish>>(allDish)
    private val observFavoriteDish = MutableLiveData<List<FavDish>>(favoriteDish)

    private val obserAndGetDishByQuery = MutableLiveData<List<FavDish>>(getDishByQuery)


    private var shouldNetworkReturnError = false

    fun setShouldReturnNetworkError(value: Boolean){
        shouldNetworkReturnError = value
    }

    fun refreshLiveData() {

        observAllDish.postValue(allDish)
        observFavoriteDish.postValue(favoriteDish)
    }





    override suspend fun insertDish(favDish: FavDish) {
        favoriteDish.add(favDish)
        refreshLiveData()
    }

    override suspend fun deleteDish(favDish: FavDish) {
        favoriteDish.remove(favDish)
        refreshLiveData()
    }

    override suspend fun updateDish(favDish: FavDish) {
        favoriteDish.add(favDish)
        refreshLiveData()
    }

    override fun getAllDish(): Flow<List<FavDish>> {
            return observAllDish.asFlow()
    }

    override fun getFavoriteDish(): Flow<List<FavDish>> {
       return observFavoriteDish.asFlow()
    }

    override fun getDishByType(query: String): Flow<List<FavDish>> {
        return obserAndGetDishByQuery.asFlow()
    }

    override suspend fun getRandomDish(): Resource<RandomRecipesResponce> {
        return if (shouldNetworkReturnError){
            Resource.Error("Error",null)
        }else{
            Resource.Success(RandomRecipesResponce(randomeDIsh))

        }

    }

}