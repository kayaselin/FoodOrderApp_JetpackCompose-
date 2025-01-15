package com.example.foodorderapp.uix.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodorderapp.data.entity.Yemekler
import com.example.foodorderapp.viewmodel.AnasayfaViewModel
import com.skydoves.landscapist.glide.GlideImage
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.navigation.NavController
import com.example.foodorderapp.viewmodel.SepetViewModel
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.foodorderapp.ui.theme.CardBackground
import com.example.foodorderapp.ui.theme.SearchBarColor
import com.example.foodorderapp.ui.theme.TopBarColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController




@Composable
fun Anasayfa(
    anasayfaViewModel: AnasayfaViewModel,
    navController: NavController,
    sepetViewModel: SepetViewModel,
) {
    val yemeklerList = anasayfaViewModel.yemekler.observeAsState(emptyList()).value
    var searchQuery by remember { mutableStateOf("") }
    var sortOption by remember { mutableStateOf("Varsayılan") }

    val filteredYemekler = if (searchQuery.isEmpty()) {
        yemeklerList
    } else {
        yemeklerList.filter { it.yemek_adi.contains(searchQuery, ignoreCase = true) }
    }.let { sortedList ->
        when (sortOption) {
            "Artan Fiyat" -> sortedList.sortedBy { it.yemek_fiyat }
            "Azalan Fiyat" -> sortedList.sortedByDescending { it.yemek_fiyat }
            else -> sortedList
        }
    }

    val topBarColor = Color(TopBarColor.value)

    SetStatusBarColor(color = topBarColor)

    Scaffold(
        topBar = { TopBar(topBarColor) },
        bottomBar = {
            BottomBar(
                navController = navController,
                viewModel = sepetViewModel
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = Color.White
        ) {
            Column {

                SearchBar(
                    searchQuery = searchQuery,
                    onSearchQueryChange = { query -> searchQuery = query },
                    onSortOptionSelected = { option -> sortOption = option }
                )
                YemekListesi(
                    yemekler = filteredYemekler,
                    onYemekClick = { yemek ->
                        navController.navigate("yemek_detay/${yemek.yemek_id}")
                    }
                )
            }
        }
    }
}

@Composable
fun SetStatusBarColor(color: Color) {
    val systemUiController = rememberSystemUiController()
    LaunchedEffect(color) {
        systemUiController.setStatusBarColor(color = color, darkIcons = false)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(topBarColor: Color) {

    val waveShape = GenericShape { size, _ ->
        moveTo(0f, size.height)
        cubicTo(
            size.width * 0.10f, size.height - 40f,
            size.width * 0.40f, size.height + 40f,
            size.width * 0.60f, size.height - 40f
        )
        cubicTo(
            size.width * 0.85f, size.height + 40f,
            size.width, size.height - 40f,
            size.width, size.height
        )
        lineTo(size.width, 0f)
        lineTo(0f, 0f)
        close()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(waveShape)
    ) {
        TopAppBar(
            title = { Text("Yemek Sipariş", fontWeight = FontWeight.Bold) },
            actions = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Teslimat Adresi")
                    Text("Ev", fontWeight = FontWeight.Bold)
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.Home, contentDescription = "Home")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = topBarColor)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSortOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = searchQuery,
            onValueChange = { query -> onSearchQueryChange(query) },
            placeholder = { Text("Ara") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(SearchBarColor.value),
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Box {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.FilterList, contentDescription = "Filtrele")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                DropdownMenuItem(
                    text = { Text("Varsayılan") },
                    onClick = {
                        onSortOptionSelected("Varsayılan")
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Artan Fiyat") },
                    onClick = {
                        onSortOptionSelected("Artan Fiyat")
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Azalan Fiyat") },
                    onClick = {
                        onSortOptionSelected("Azalan Fiyat")
                        expanded = false
                    }
                )
            }
        }
    }
}


@Composable
fun YemekCard(
    yemek: Yemekler,
    onClick: (Yemekler) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(160.dp)
            .clickable { onClick(yemek) },
        elevation = CardDefaults.cardElevation(5.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(CardBackground.value))
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            val imageUrl = "http://kasimadalan.pe.hu/yemekler/resimler/${yemek.yemek_resim_adi}"

            GlideImage(
                imageModel = imageUrl,
                contentDescription = yemek.yemek_adi,
                modifier = Modifier
                    .size(130.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Text(
                text = yemek.yemek_adi ?: "",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 14.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Text(
                text = "Kargo Ücretsiz",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "₺${yemek.yemek_fiyat}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}

@Composable
fun YemekListesi(
    yemekler: List<Yemekler>,
    onYemekClick: (Yemekler) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp),
        horizontalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        items(yemekler) { yemek ->
            YemekCard(yemek = yemek, onClick = onYemekClick)
        }
    }
}

@Composable
fun BottomBar(navController: NavController, viewModel: SepetViewModel) {
    BottomAppBar(
        modifier = Modifier.height(56.dp),
        containerColor = Color(SearchBarColor.value)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { navController.navigate("anasayfa") }) {
            Icon(Icons.Default.Home, contentDescription = "Anasayfa")
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { navController.navigate("favoriler") }) {
            Icon(Icons.Default.Favorite, contentDescription = "Favorilerim")
        }

        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = {
            viewModel.sepettekiYemekleriGetir("selin")
            navController.navigate("sepet")
        }) {
            Icon(Icons.Default.ShoppingCart, contentDescription = "Sepetim")
        }

        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { navController.navigate("profil") }) {
            Icon(Icons.Default.Person, contentDescription = "Profilim")
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}