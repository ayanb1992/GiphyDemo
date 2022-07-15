package com.example.giphydemo.model.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.giphydemo.model.database.entity.FavoriteGifs

@Dao
interface GifDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGifData(gifData: FavoriteGifs)

    @Query("SELECT * FROM favorite_gifs")
    fun selectAllFavorites(): List<FavoriteGifs>

    @Query("DELETE FROM favorite_gifs WHERE id=:id")
    fun removeGifData(id: String)
}