package com.example.favdish.network

import com.example.favdish.utiles.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FavDishApiClient {

   private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        val apiClient: RandomDishApi by lazy {
            retrofit.create(RandomDishApi::class.java)

    }


}