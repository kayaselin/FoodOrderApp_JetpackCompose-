package com.example.foodorderapp.data.repository

import com.example.foodorderapp.data.entity.FavoriEntity
import com.example.foodorderapp.retrofit.FavorilerDao
import javax.inject.Inject

class FavorilerRepository @Inject constructor(
    private val favorilerDao: FavorilerDao
) {
    suspend fun favoriEkle(favoriYemek: FavoriEntity) {
        favorilerDao.favoriEkle(favoriYemek)
    }

    suspend fun favoriSil(yemekId: Int) {
        favorilerDao.favoriSil(yemekId)
    }

    suspend fun tumFavorileriGetir(): List<FavoriEntity> {
        return favorilerDao.tumFavorileriGetir()
    }

}

