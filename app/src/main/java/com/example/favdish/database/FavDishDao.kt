package com.example.favdish.database

import androidx.room.*
import com.example.favdish.models.FavDish
import kotlinx.coroutines.flow.Flow


@Dao
interface FavDishDao {


    @Query("SELECT * FROM FAV_DISH_TABLE ORDER BY ID")
    fun getAllDish(): Flow<List<FavDish>>

    @Query("SELECT * FROM FAV_DISH_TABLE WHERE favorite_dish = 1")
    fun getFavoriteDish(): Flow<List<FavDish>>

    @Query("SELECT * FROM FAV_DISH_TABLE WHERE type = :filterQuery")
    fun getDishByType(filterQuery: String): Flow<List<FavDish>>

    @Insert(onConflict = OnConflictStrategy.REPLACE )
    suspend fun insertDish(favDish: FavDish)

    @Update
    suspend fun updateDish(favDish: FavDish)

    @Delete
    suspend fun deleteDish(favDish: FavDish)
}