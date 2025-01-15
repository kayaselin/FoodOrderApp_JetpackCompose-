package com.example.foodorderapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodorderapp.data.entity.SepetYemekler
import com.example.foodorderapp.data.repository.FoodsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SepetViewModel @Inject constructor(
    private val repository: FoodsRepository
) : ViewModel() {

    private val _sepetListesi = MutableStateFlow<List<SepetYemekler>>(emptyList())
    val sepetListesi: StateFlow<List<SepetYemekler>> get() = _sepetListesi

    private val _toplamTutar = MutableStateFlow(0)
    val toplamTutar: StateFlow<Int> get() = _toplamTutar

    init {
        sepettekiYemekleriGetir("selin")
    }

    fun sepettekiYemekleriGetir(kullaniciAdi: String) {
        viewModelScope.launch {
            try {
                val response = repository.getSepetItems(kullaniciAdi)
                _sepetListesi.value = response
                _toplamTutar.value = response.sumOf { it.yemek_fiyat * it.yemek_siparis_adet }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun removeItemFromCart(sepetYemekId: Int, kullaniciAdi: String) {
        viewModelScope.launch {
            try {
                repository.removeItemFromCart(sepetYemekId, kullaniciAdi)
                sepettekiYemekleriGetir(kullaniciAdi)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun removeAllItemsFromCartByName(yemekAdi: String, kullaniciAdi: String) {
        viewModelScope.launch {
            try {
                repository.removeAllItemsFromCartByName(yemekAdi, kullaniciAdi)
                sepettekiYemekleriGetir(kullaniciAdi)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}
