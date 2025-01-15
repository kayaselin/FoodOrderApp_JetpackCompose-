package com.example.foodorderapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodorderapp.data.entity.Yemekler
import com.example.foodorderapp.data.repository.FoodsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class YemekDetayViewModel @Inject constructor(
    private val repository: FoodsRepository
) : ViewModel() {

    private val _selectedYemek = MutableStateFlow<Yemekler?>(null)
    val selectedYemek: StateFlow<Yemekler?> get() = _selectedYemek

    fun loadYemekById(yemekId: String) {
        viewModelScope.launch {
            try {
                val yemek = repository.getFoods().find { it.yemek_id == yemekId.toIntOrNull() }
                _selectedYemek.value = yemek
            } catch (e: Exception) {
            }
        }
    }

    fun addToCart(yemek: Yemekler, adet: Int, kullaniciAdi: String) {
        viewModelScope.launch {
            try {
                repository.addToCart(
                    yemekAdi = yemek.yemek_adi,
                    yemekResimAdi = yemek.yemek_resim_adi,
                    yemekFiyat = yemek.yemek_fiyat,
                    yemekSiparisAdet = adet,
                    kullaniciAdi = kullaniciAdi
                )
            } catch (e: Exception) {
            }
        }
    }



}
