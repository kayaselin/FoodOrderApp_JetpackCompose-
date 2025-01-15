package com.example.foodorderapp.uix.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.skydoves.landscapist.glide.GlideImage
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.graphics.Color
import com.example.foodorderapp.data.entity.FavoriEntity
import com.example.foodorderapp.ui.theme.TopBarColor
import com.example.foodorderapp.viewmodel.FavorilerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavorilerEkrani(
    favorilerViewModel: FavorilerViewModel = hiltViewModel(),
    navController: NavController
) {
    val favorilerListesi by favorilerViewModel.favorilerListesi.collectAsState()

    LaunchedEffect(Unit) {
        favorilerViewModel.tumFavorileriGetir()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favorilerim", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(TopBarColor.value))
            )
        }
    ) { paddingValues ->
        if (favorilerListesi.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Henüz favori eklemediniz",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(favorilerListesi) { favoriYemek ->
                    FavoriYemekCard(
                        favoriYemek = favoriYemek,
                        favorilerViewModel = favorilerViewModel,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriYemekCard(
    favoriYemek: FavoriEntity,
    favorilerViewModel: FavorilerViewModel,
    navController: NavController
) {
    var isFavorite by remember { mutableStateOf(true) }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate("yemek_detay/${favoriYemek.yemek_id}")
            },
        elevation = CardDefaults.cardElevation(5.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val imageUrl = "http://kasimadalan.pe.hu/yemekler/resimler/${favoriYemek.yemek_resim_adi}"

                GlideImage(
                    imageModel = imageUrl,
                    contentDescription = favoriYemek.yemek_adi,
                    modifier = Modifier
                        .size(130.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = favoriYemek.yemek_adi,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "₺${favoriYemek.yemek_fiyat}",
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            IconButton(
                onClick = {
                    if (isFavorite) {
                        favorilerViewModel.favoriSil(favoriYemek.yemek_id)
                    } else {
                        favorilerViewModel.favoriEkle(favoriYemek)
                    }
                    isFavorite = !isFavorite
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorilerden Kaldır",
                    tint = if (isFavorite) Color.Blue else Color.Gray
                )
            }
        }
    }
}
