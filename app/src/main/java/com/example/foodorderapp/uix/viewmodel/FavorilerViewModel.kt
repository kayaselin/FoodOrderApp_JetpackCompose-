package com.example.foodorderapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodorderapp.data.entity.FavoriEntity
import com.example.foodorderapp.data.repository.FavorilerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavorilerViewModel @Inject constructor(
    private val repository: FavorilerRepository
) : ViewModel() {

    private val _favorilerListesi = MutableStateFlow<List<FavoriEntity>>(emptyList())
    val favorilerListesi: StateFlow<List<FavoriEntity>> get() = _favorilerListesi

    fun tumFavorileriGetir() {
        viewModelScope.launch {
            val favoriler = repository.tumFavorileriGetir()
            _favorilerListesi.value = favoriler
        }
    }

    fun favoriEkle(yemek: FavoriEntity) {
        viewModelScope.launch {
            repository.favoriEkle(yemek)
            tumFavorileriGetir()
        }
    }

    fun favoriSil(yemekId: Int) {
        viewModelScope.launch {
            repository.favoriSil(yemekId)
            tumFavorileriGetir()
        }
    }

    fun isFavoriYemek(yemekId: String): Boolean {
        return _favorilerListesi.value.any { it.yemek_id.toString() == yemekId }
    }


}
