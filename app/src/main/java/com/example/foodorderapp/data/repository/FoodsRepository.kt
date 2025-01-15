package com.example.foodorderapp.data.repository

import com.example.foodorderapp.data.datasource.FoodsDataSource
import com.example.foodorderapp.data.entity.SepetYemekler
import com.example.foodorderapp.data.entity.Yemekler
import javax.inject.Inject

class FoodsRepository @Inject constructor(
    private val dataSource: FoodsDataSource
) {
    suspend fun getFoods(): List<Yemekler> {
        return dataSource.getFoods()
    }


    suspend fun addToCart(
        yemekAdi: String,
        yemekResimAdi: String,
        yemekFiyat: Int,
        yemekSiparisAdet: Int,
        kullaniciAdi: String
    ) {
        dataSource.addToCart(yemekAdi, yemekResimAdi, yemekFiyat, yemekSiparisAdet, kullaniciAdi)
    }

    suspend fun getSepetItems(kullaniciAdi: String): List<SepetYemekler> {
        val response = dataSource.getSepetItems(kullaniciAdi)
        return response.sepet_yemekler
    }

    suspend fun removeItemFromCart(sepetYemekId: Int, kullaniciAdi: String) {
        dataSource.removeItemFromCart(sepetYemekId, kullaniciAdi)
    }

    suspend fun removeAllItemsFromCartByName(yemekAdi: String, kullaniciAdi: String) {
        val itemsToRemove = dataSource.getSepetItems(kullaniciAdi).sepet_yemekler
            .filter { it.yemek_adi == yemekAdi }

        for (item in itemsToRemove) {
            dataSource.removeItemFromCart(item.sepet_yemek_id, kullaniciAdi)
        }
    }

}
