package com.example.favdish.di

import android.content.Context
import androidx.navigation.Navigator
import androidx.room.Room
import com.example.favdish.database.FavDishDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named


@Module
@InstallIn(SingletonComponent::class)
object AppModuleDorTestiing {

    @Provides
    @Named("test.db")
    fun provideInMemoryDb(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(context, FavDishDatabase::class.java)
            .allowMainThreadQueries().build()



}