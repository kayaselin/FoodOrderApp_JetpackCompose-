package com.example.foodorderapp.retrofit

import androidx.room.*
import com.example.foodorderapp.data.entity.FavoriEntity

@Dao
interface FavorilerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun favoriEkle(favoriYemek: FavoriEntity)

    @Query("DELETE FROM favoriler WHERE yemek_id = :yemekId")
    suspend fun favoriSil(yemekId: Int)

    @Query("SELECT * FROM favoriler")
    suspend fun tumFavorileriGetir(): List<FavoriEntity>
}
