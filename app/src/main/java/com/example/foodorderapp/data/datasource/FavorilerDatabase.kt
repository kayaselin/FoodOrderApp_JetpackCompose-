package com.example.foodorderapp.data.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.foodorderapp.data.entity.FavoriEntity
import com.example.foodorderapp.retrofit.FavorilerDao

@Database(entities = [FavoriEntity::class], version = 1, exportSchema = false)
abstract class FavorilerDatabase : RoomDatabase() {
    abstract fun favorilerDao(): FavorilerDao
}
