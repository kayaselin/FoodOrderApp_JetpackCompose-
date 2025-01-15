package com.example.foodorderapp.retrofit


import com.example.foodorderapp.data.entity.SepetYemeklerResponse
import com.example.foodorderapp.data.entity.Yemekler
import com.example.foodorderapp.data.entity.YemeklerResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FoodsDao {
    @GET("yemekler/tumYemekleriGetir.php")
    suspend fun getFoods(): YemeklerResponse

    @GET("yemekler/yemekById.php")
    suspend fun getYemekById(@Query("yemek_id") yemekId: String): Yemekler

    @FormUrlEncoded
    @POST("yemekler/sepeteYemekEkle.php")
    suspend fun addToCart(
        @Field("yemek_adi") yemekAdi: String,
        @Field("yemek_resim_adi") yemekResimAdi: String,
        @Field("yemek_fiyat") yemekFiyat: Int,
        @Field("yemek_siparis_adet") yemekSiparisAdet: Int,
        @Field("kullanici_adi") kullaniciAdi: String
    )

    @FormUrlEncoded
    @POST("yemekler/sepettekiYemekleriGetir.php")
    suspend fun getSepetItems(
        @Field("kullanici_adi") kullaniciAdi: String): SepetYemeklerResponse


    @FormUrlEncoded
    @POST("yemekler/sepettenYemekSil.php")
    suspend fun removeItemFromCart(
        @Field("sepet_yemek_id") sepetYemekId: Int,
        @Field("kullanici_adi") kullaniciAdi: String
    )

}

