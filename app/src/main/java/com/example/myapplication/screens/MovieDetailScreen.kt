package com.example.myapplication.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

@Composable
fun MovieDetailScreen(
    title: String,
    year: String,
    rating: String,
    posterUrl: String,
    description: String
) {
    var userRating by remember { mutableStateOf(0f) } // Kullanıcı puanı

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(posterUrl),
            contentDescription = title,
            modifier = Modifier.size(200.dp).padding(bottom = 16.dp)
        )

        Text(text = title, fontSize = 24.sp)
        Text(text = "год: $year", fontSize = 16.sp)
        Text(text = "IMDb : $rating", fontSize = 16.sp)
        Text(text = description, fontSize = 14.sp, modifier = Modifier.padding(vertical = 8.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Оценить фильмы", fontSize = 18.sp)

        Slider(
            value = userRating,
            onValueChange = { userRating = it },
            valueRange = 0f..5f,
            steps = 4,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Text(text = "Твоя Оценка: ${userRating.toInt()} / 5", fontSize = 18.sp)
    }
}
