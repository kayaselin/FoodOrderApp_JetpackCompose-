package com.example.foodorderapp.data.datasource

import com.example.foodorderapp.data.entity.SepetYemeklerResponse
import com.example.foodorderapp.data.entity.Yemekler

interface FoodsDataSource {
    suspend fun getFoods(): List<Yemekler>

    suspend fun addToCart(
        yemekAdi: String,
        yemekResimAdi: String,
        yemekFiyat: Int,
        yemekSiparisAdet: Int,
        kullaniciAdi: String
    )

    suspend fun getSepetItems(
        kullaniciAdi: String): SepetYemeklerResponse


    suspend fun removeItemFromCart(sepetYemekId: Int, kullaniciAdi: String)


}
