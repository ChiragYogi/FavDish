package com.example.favdish.repository

import com.example.favdish.BuildConfig
import com.example.favdish.database.FavDishDao
import com.example.favdish.models.FavDish
import com.example.favdish.models.networkmodel.RandomRecipesResponce

import com.example.favdish.network.RandomDishService
import com.example.favdish.utiles.Constants.APIKEY
import com.example.favdish.utiles.Constants.TAGE_VALUE
import com.example.favdish.utiles.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class FavDishRepository @Inject constructor(
    private val favDishService: RandomDishService
    ,private val favDishDao:FavDishDao){

    suspend fun getRandomDish(): Resource<RandomRecipesResponce>{
        return try {
            withContext(Dispatchers.IO){
                val response  =
                    withTimeout(5000){favDishService.getRandomDish(TAGE_VALUE, BuildConfig.API_KEY)}
                val result = response.body()
                if (response.isSuccessful && result != null){
                    Resource.Success(result)
                } else {
                    Resource.Error(response.message())
                }
            }
        }catch (io: IOException){
            Resource.Error(io.message ?: "An error occurred")
        }catch (http: HttpException){
            Resource.Error(http.message ?: "An error occurred")
        }catch (e: Exception){
            Resource.Error(e.message ?: "An error occurred")
        }
    }

     fun getAllDish(): Flow<List<FavDish>>{
        return favDishDao.getAllDish()
    }

     fun getFavoriteDish(): Flow<List<FavDish>>{
        return favDishDao.getFavoriteDish()
    }

     fun getDishByType(query: String): Flow<List<FavDish>>{
        return favDishDao.getDishByType(query)
    }

   suspend fun insertDish(favDish: FavDish){
       favDishDao.insertDish(favDish)
   }

    suspend fun updateDish(favDish: FavDish){
        favDishDao.updateDish(favDish)
    }

    suspend fun deleteDish(favDish: FavDish){
        favDishDao.deleteDish(favDish)
    }


}