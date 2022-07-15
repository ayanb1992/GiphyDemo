package com.example.giphydemo.model.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_gifs")
data class FavoriteGifs(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "url") val url: String,
    val title: String
)