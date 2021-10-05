package com.example.favdish.repository

import com.example.favdish.database.FavDishDao
import com.example.favdish.models.FavDish
import com.example.favdish.models.networkmodel.RandomRecipesResponce
import com.example.favdish.network.FavDishApiClient.apiClient
import com.example.favdish.utiles.Constants.APIKEY
import com.example.favdish.utiles.Constants.TAGE_VALUE
import com.example.favdish.utiles.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import java.io.IOException

class FavDishRepository(private val dao:FavDishDao){


    suspend fun getRandomDish(): Resource<RandomRecipesResponce>{
        return try {
            withContext(Dispatchers.IO){
                val response  =
                    withTimeout(5000){apiClient.getRandomDish(TAGE_VALUE, APIKEY)}
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
        return dao.getAllDish()
    }

     fun getFavoriteDish(): Flow<List<FavDish>>{
        return dao.getFavoriteDish()
    }

     fun getDishByType(query: String): Flow<List<FavDish>>{
        return dao.getDishByType(query)
    }

   suspend fun insertDish(favDish: FavDish){
       dao.insertDish(favDish)
   }

    suspend fun updateDish(favDish: FavDish){
        dao.updateDish(favDish)
    }

    suspend fun deleteDish(favDish: FavDish){
        dao.deleteDish(favDish)
    }


}