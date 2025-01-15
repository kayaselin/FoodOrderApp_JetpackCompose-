package com.example.foodorderapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodorderapp.data.entity.Yemekler
import com.example.foodorderapp.data.repository.FoodsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnasayfaViewModel @Inject constructor(
    private val repository: FoodsRepository
) : ViewModel() {
    private val _yemekler = MutableLiveData<List<Yemekler>>()
    val yemekler: LiveData<List<Yemekler>> get() = _yemekler

    init {
        loadYemekler()
    }

    private fun loadYemekler() {
        viewModelScope.launch {
            try {
                val response = repository.getFoods()
                _yemekler.value = response
                Log.d("AnasayfaViewModel", "Yemekler: $response")
            } catch (e: Exception) {
                Log.e("AnasayfaViewModel", "Hata: ${e.message}")
                e.printStackTrace()
            }
        }
    }


}
