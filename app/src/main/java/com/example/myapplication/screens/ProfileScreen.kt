package com.example.myapplication.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.myapplication.viewmodel.ProfileViewModel
import com.example.myapplication.viewmodel.ProfileViewModelFactory
import coil.compose.rememberAsyncImagePainter

@Composable
fun ProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(LocalContext.current))
) {
    val userProfile by profileViewModel.userProfile.collectAsState()
    val context = LocalContext.current

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(50.dp)
    ) {
        IconButton(
            onClick = { navController.navigate("edit_profile") },
            modifier = Modifier.align(Alignment.End)
        ) {
            Icon(Icons.Default.Edit, contentDescription = "Редактировать")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (userProfile.avatarUri.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(userProfile.avatarUri),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        Text("ФИО: ${userProfile.name}")
        Text("Должность: ${userProfile.jobTitle}")
        Text("Резюме: ${userProfile.resumeUrl}")

        Spacer(modifier = Modifier.height(25.dp))

        Button(onClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(userProfile.resumeUrl))
            context.startActivity(intent)
        }) {
            Text("Открыть резюме")
        }
    }
}
