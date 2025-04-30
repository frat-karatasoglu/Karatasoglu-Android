package com.example.myapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.models.Movie

@Composable
fun MovieItem(movie: Movie, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {

        Image(
            painter = rememberAsyncImagePainter(movie.poster?.url),
            contentDescription = movie.name,
            modifier = Modifier
                .size(100.dp)
                .padding(end = 8.dp)
        )

        Column {
            Text(text = movie.name ?: "Bilinmiyor", fontSize = 20.sp)
            Text(text = "Yıl: ${movie.year ?: "Bilinmiyor"}", fontSize = 14.sp)
            Text(text = "Puan: ${movie.rating?.kp ?: "Bilinmiyor"}", fontSize = 14.sp)
        }
    }
}
