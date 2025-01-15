package com.example.foodorderapp.uix.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.foodorderapp.uix.views.Anasayfa
import com.example.foodorderapp.uix.views.FavorilerEkrani
import com.example.foodorderapp.uix.views.SepetEkrani
import com.example.foodorderapp.uix.views.YemekDetaySayfa
import com.example.foodorderapp.viewmodel.AnasayfaViewModel
import com.example.foodorderapp.viewmodel.FavorilerViewModel
import com.example.foodorderapp.viewmodel.SepetViewModel

@Composable
fun SayfaGecisleri(
    anasayfaViewModel: AnasayfaViewModel,
    navController: NavHostController,
    sepetViewModel: SepetViewModel,
    favorilerViewModel: FavorilerViewModel,


) {
    NavHost(
        navController = navController,
        startDestination = "anasayfa"
    ) {

        composable("anasayfa") {
            Anasayfa(
                anasayfaViewModel = anasayfaViewModel,
                navController = navController,
                sepetViewModel = sepetViewModel,

            )
        }

        composable(
            "yemek_detay/{yemekId}",
            arguments = listOf(navArgument("yemekId") { type = NavType.StringType })
        ) { backStackEntry ->
            val yemekId = backStackEntry.arguments?.getString("yemekId")
            YemekDetaySayfa(
                yemekId = yemekId ?: "",
                navController = navController,
                kullaniciAdi = "selin",

            )
        }


        composable("sepet") {
            val sepetListesi by sepetViewModel.sepetListesi.collectAsState(emptyList())
            val toplamTutar by sepetViewModel.toplamTutar.collectAsState(0)

            SepetEkrani(
                sepetListesi = sepetListesi,
                toplamTutar = toplamTutar,
                navController = navController,
                sepetViewModel = sepetViewModel,
                onRemoveItem = { sepetYemekId ->
                    sepetViewModel.removeItemFromCart(sepetYemekId, "selin")
                }
            )
        }

        composable("favoriler") {
            FavorilerEkrani(favorilerViewModel, navController = navController)
        }




    }
}
