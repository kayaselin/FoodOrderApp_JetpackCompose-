package com.example.foodorderapp.data.datasource

import com.example.foodorderapp.data.entity.SepetYemeklerResponse
import com.example.foodorderapp.data.entity.Yemekler
import com.example.foodorderapp.retrofit.FoodsDao
import javax.inject.Inject

class RemoteFoodsDataSource @Inject constructor(
    private val foodsDao: FoodsDao) : FoodsDataSource {

    override suspend fun getFoods(): List<Yemekler> {
        val response = foodsDao.getFoods()
        return response.yemekler
    }


    override suspend fun addToCart(
        yemekAdi: String,
        yemekResimAdi: String,
        yemekFiyat: Int,
        yemekSiparisAdet: Int,
        kullaniciAdi: String
    ) {
        foodsDao.addToCart(yemekAdi, yemekResimAdi, yemekFiyat, yemekSiparisAdet, kullaniciAdi)
    }

    override suspend fun getSepetItems(kullaniciAdi: String): SepetYemeklerResponse {
        return foodsDao.getSepetItems(kullaniciAdi)
    }

    override suspend fun removeItemFromCart(sepetYemekId: Int, kullaniciAdi: String) {
        foodsDao.removeItemFromCart(sepetYemekId, kullaniciAdi)
    }

}
