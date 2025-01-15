package com.example.foodorderapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoriler")
data class FavoriEntity(
    @PrimaryKey(autoGenerate = true)
    var yemek_id: Int = 0,
    var yemek_adi: String,
    var yemek_resim_adi: String,
    var yemek_fiyat: Int
)
