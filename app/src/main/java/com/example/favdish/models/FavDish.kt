package com.example.favdish.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "fav_dish_table")
data class FavDish(
    @ColumnInfo val image: String,
    @ColumnInfo(name = "image_source") val imageSource: String, // Local or Online
    @ColumnInfo val title: String,
    @ColumnInfo val type: String,
    @ColumnInfo val category: String,
    @ColumnInfo val ingredients: String,
    @ColumnInfo(name = "cooking_time") val cookingTime: String,
    @ColumnInfo(name = "instructions") val directionToCook: String,
    @ColumnInfo(name = "favorite_dish") var favoriteDish: Boolean = false,
    @PrimaryKey(autoGenerate = true) val id: Int? = null
): Parcelable
