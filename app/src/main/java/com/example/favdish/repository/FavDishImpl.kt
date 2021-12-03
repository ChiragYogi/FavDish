package com.example.favdish.repository

import com.example.favdish.models.FavDish
import com.example.favdish.models.networkmodel.RandomRecipesResponce
import com.example.favdish.utiles.Resource
import kotlinx.coroutines.flow.Flow

interface FavDishImpl {

    suspend fun  insertDish(favDish: FavDish)

    suspend fun  deleteDish(favDish: FavDish)

    suspend fun  updateDish(favDish: FavDish)

    fun getAllDish(): Flow<List<FavDish>>

    fun getFavoriteDish(): Flow<List<FavDish>>

    fun getDishByType(query: String): Flow<List<FavDish>>

    suspend fun getRandomDish(): Resource<RandomRecipesResponce>



}