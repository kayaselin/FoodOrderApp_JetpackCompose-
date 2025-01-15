package com.example.foodorderapp.uix.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.foodorderapp.data.entity.Yemekler
import com.example.foodorderapp.viewmodel.YemekDetayViewModel
import com.skydoves.landscapist.glide.GlideImage
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.example.foodorderapp.data.entity.FavoriEntity
import com.example.foodorderapp.ui.theme.ButtonColor
import com.example.foodorderapp.ui.theme.SearchBarColor
import com.example.foodorderapp.viewmodel.FavorilerViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun YemekDetaySayfa(
    yemekId: String,
    navController: NavController,
    viewModel: YemekDetayViewModel = hiltViewModel(),
    favorilerViewModel: FavorilerViewModel = hiltViewModel(),
    kullaniciAdi: String,
) {
    val yemek by viewModel.selectedYemek.collectAsState(initial = null)
    val favorilerListesi by favorilerViewModel.favorilerListesi.collectAsState()

    var adet by remember { mutableStateOf(1) }
    var totalPrice by remember { mutableStateOf(0) }
    var isButtonEnabled by remember { mutableStateOf(true) }
    var showSnackbar by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val isFavorite by remember {
        derivedStateOf {
            favorilerListesi.any { it.yemek_id.toString() == yemekId }
        }
    }


    fun handleAddToCart(selectedYemek: Yemekler, adet: Int) {
        viewModel.addToCart(selectedYemek, adet, kullaniciAdi = kullaniciAdi)
        isButtonEnabled = false

        coroutineScope.launch {
            delay(700)
            isButtonEnabled = true
        }

        coroutineScope.launch {
            val snackbarJob = launch {
                snackbarHostState.showSnackbar(
                    message = "Sepete eklendi!",
                    duration = SnackbarDuration.Indefinite
                )
            }

            delay(1000)
            snackbarJob.cancel()
        }

    }

    LaunchedEffect(yemekId) {
        viewModel.loadYemekById(yemekId)
        favorilerViewModel.tumFavorileriGetir()
    }

    LaunchedEffect(adet, yemek) {
        yemek?.let { selectedYemek ->
            totalPrice = adet * selectedYemek.yemek_fiyat
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                color = Color.White
            ) {
                yemek?.let { selectedYemek ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(SearchBarColor.value))
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            IconButton(
                                onClick = { navController.popBackStack() },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Geri")
                            }

                            Text(
                                text = "Ürün Detayı",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.weight(6f),
                                textAlign = TextAlign.Center
                            )

                            IconButton(
                                onClick = {
                                    if (isFavorite) {
                                        favorilerViewModel.favoriSil(selectedYemek.yemek_id)
                                    } else {
                                        val favoriYemek = FavoriEntity(
                                            yemek_id = selectedYemek.yemek_id,
                                            yemek_adi = selectedYemek.yemek_adi ,
                                            yemek_resim_adi = selectedYemek.yemek_resim_adi ,
                                            yemek_fiyat = selectedYemek.yemek_fiyat
                                        )
                                        favorilerViewModel.favoriEkle(favoriYemek)
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Favorilere Ekle",
                                    tint = if (isFavorite) Color.Blue else Color.Gray,
                                    modifier = Modifier.size(35.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(15.dp))

                        GlideImage(
                            imageModel = "http://kasimadalan.pe.hu/yemekler/resimler/${selectedYemek.yemek_resim_adi}",
                            contentDescription = selectedYemek.yemek_adi,
                            modifier = Modifier.size(200.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = selectedYemek.yemek_adi ,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 26.sp
                            ),
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Fiyat: ₺${selectedYemek.yemek_fiyat}",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 25.sp
                            ),
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { if (adet > 1) adet-- },
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Azalt",
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Text(
                                text = adet.toString(),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            IconButton(
                                onClick = { adet++ },
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    Icons.Default.KeyboardArrowUp,
                                    contentDescription = "Arttır",
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(80.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Toplam: ₺$totalPrice",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp
                                ),
                                modifier = Modifier.weight(1f)
                            )

                            Button(
                                onClick = { handleAddToCart(selectedYemek, adet) },
                                modifier = Modifier
                                    .width(160.dp)
                                    .height(56.dp),
                                shape = RoundedCornerShape(8.dp),
                                enabled = isButtonEnabled,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(ButtonColor.value)),
                                elevation = ButtonDefaults.buttonElevation(8.dp),
                            ) {
                                Text(
                                    text = "SEPETE EKLE",
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                            }
                        }

                    }
                }
            }
        }
    )
}
