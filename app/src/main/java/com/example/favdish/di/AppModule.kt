package com.example.favdish.di


import android.content.Context
import androidx.room.Room
import com.example.favdish.database.FavDishDao
import com.example.favdish.database.FavDishDao_Impl
import com.example.favdish.database.FavDishDatabase
import com.example.favdish.network.RandomDishService
import com.example.favdish.repository.FavDishImpl
import com.example.favdish.repository.FavDishRepository
import com.example.favdish.utiles.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object AppModule {


    @Singleton
    @Provides
    fun provideRetrofit():Retrofit =
             Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()



    @Singleton
    @Provides
    fun provideRandomDishService(retrofit: Retrofit): RandomDishService{
        return retrofit
              .create(RandomDishService::class.java)
    }

    @Singleton
    @Provides
    fun provideDishDatabase(@ApplicationContext context: Context): FavDishDatabase =
        Room.databaseBuilder(
            context,
            FavDishDatabase::class.java,
            "fav_dish_database")
            .fallbackToDestructiveMigration()
            .build()


    @Singleton
    @Provides
    fun provideDishDao(dishDatabase: FavDishDatabase): FavDishDao{
        return dishDatabase.getFavDishDao()
    }

    @Singleton
    @Provides
    fun provideFavDishRepository(
        favDishService: RandomDishService,
        dao: FavDishDao
    ): FavDishImpl{
        return FavDishRepository(favDishService,dao)
    }



}