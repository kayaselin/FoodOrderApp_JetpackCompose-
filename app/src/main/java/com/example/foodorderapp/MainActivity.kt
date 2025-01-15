package com.example.foodorderapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.example.foodorderapp.ui.theme.FoodOrderAppTheme
import com.example.foodorderapp.uix.navigation.SayfaGecisleri
import com.example.foodorderapp.viewmodel.AnasayfaViewModel
import com.example.foodorderapp.viewmodel.FavorilerViewModel
import com.example.foodorderapp.viewmodel.SepetViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val anasayfaViewModel: AnasayfaViewModel by viewModels()
    private val sepetViewModel: SepetViewModel by viewModels()
    private val favorilerViewModel: FavorilerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodOrderAppTheme {
                val navController = rememberNavController()
                SayfaGecisleri(
                    anasayfaViewModel = anasayfaViewModel,
                    navController = navController,
                    favorilerViewModel = favorilerViewModel,
                    sepetViewModel = sepetViewModel,

                )
            }
        }
    }
}
