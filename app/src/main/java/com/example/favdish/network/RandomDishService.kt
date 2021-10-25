package com.example.favdish.network

import com.example.favdish.models.networkmodel.RandomRecipesResponce
import com.example.favdish.utiles.Constants.APIKEY
import com.example.favdish.utiles.Constants.API_ENDPOINTS
import com.example.favdish.utiles.Constants.API_KEY
import com.example.favdish.utiles.Constants.TAGE_VALUE
import com.example.favdish.utiles.Constants.TAGS
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomDishService {

    @GET(API_ENDPOINTS)
    suspend fun getRandomDish(
        @Query(TAGS)
        tags: String,
        @Query(API_KEY)
        apiKey: String
    ): Response<RandomRecipesResponce>
}