package com.example.giphydemo.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.giphydemo.model.database.dao.GifDao
import com.example.giphydemo.model.database.entity.FavoriteGifs
import com.example.giphydemo.util.Constants
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

@Database(entities = [FavoriteGifs::class], version = Constants.DB_VERSION, exportSchema = false)
abstract class GifDatabase : RoomDatabase() {

    abstract fun gifDao(): GifDao

    companion object {
        private var dbInstance: GifDatabase? = null

        fun getDatabase(context: Context): GifDatabase {
            if (dbInstance == null) {
                synchronized(GifDatabase::class.java) {
                    val factory =
                        SupportFactory(SQLiteDatabase.getBytes(Constants.DB_PASSWORD.toCharArray()))
                    dbInstance =
                        Room.databaseBuilder(context, GifDatabase::class.java, Constants.DB_NAME)
                            .openHelperFactory(factory).build()
                }
            }
            return dbInstance as GifDatabase
        }
    }
}