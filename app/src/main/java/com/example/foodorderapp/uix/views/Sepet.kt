package com.example.foodorderapp.uix.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.example.foodorderapp.data.entity.SepetYemekler
import com.skydoves.landscapist.glide.GlideImage
import com.example.foodorderapp.R
import com.example.foodorderapp.ui.theme.ButtonColor
import com.example.foodorderapp.ui.theme.CardBackground
import com.example.foodorderapp.ui.theme.SearchBarColor
import com.example.foodorderapp.ui.theme.TopBarColor
import com.example.foodorderapp.viewmodel.SepetViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SepetEkrani(
    sepetListesi: List<SepetYemekler>,
    navController: NavController,
    sepetViewModel: SepetViewModel,
    toplamTutar: Int,
    onRemoveItem: (Int) -> Unit
) {
    var showAnimation by remember { mutableStateOf(false) }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.order_animation))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = showAnimation
    )

    val birlesmisSepetListesi = sepetListesi
        .groupBy { it.yemek_adi }
        .map { entry ->
            val toplamAdet = entry.value.sumOf { it.yemek_siparis_adet }
            val ilkYemek = entry.value[0]
            ilkYemek.copy(yemek_siparis_adet = toplamAdet)
        }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Sepetim") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri")
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors
                        (containerColor = Color(TopBarColor.value))
                )
            },
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier.height(84.dp),
                    containerColor = Color(SearchBarColor.value)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "Toplam:",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "₺$toplamTutar",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = {
                                showAnimation = true
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(ButtonColor.value)
                            )
                        ) {
                            Text(text = "SEPETİ ONAYLA")
                        }

                        if (showAnimation) {
                            LaunchedEffect(Unit) {
                                delay(2000)
                                showAnimation = false
                                navController.popBackStack()
                            }
                        }
                    }
                }
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(birlesmisSepetListesi) { sepetItem ->
                    SepetItemCard(
                        sepetItem = sepetItem,
                        sepetViewModel = sepetViewModel
                    )
                }
            }
        }

        if (showAnimation) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
            ) {
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.size(300.dp)
                )
            }
        }
    }
}

@Composable
fun SepetItemCard(
    sepetItem: SepetYemekler,
    sepetViewModel: SepetViewModel,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(CardBackground.value)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            GlideImage(
                imageModel = "http://kasimadalan.pe.hu/yemekler/resimler/${sepetItem.yemek_resim_adi}",
                contentDescription = sepetItem.yemek_adi,
                modifier = Modifier.size(72.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = sepetItem.yemek_adi,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Fiyat: ₺${sepetItem.yemek_fiyat}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Adet: ${sepetItem.yemek_siparis_adet}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "₺${sepetItem.yemek_fiyat * sepetItem.yemek_siparis_adet}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(16.dp))

            IconButton(
                onClick = {
                    sepetViewModel.removeAllItemsFromCartByName(sepetItem.yemek_adi, "selin")
                },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Ürünü sil",
                    modifier = Modifier.size(32.dp),
                    tint = Color(ButtonColor.value)
                )
            }
        }
    }
}