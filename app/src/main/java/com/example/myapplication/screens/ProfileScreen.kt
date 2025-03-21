package com.example.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ProfileScreen(navController: NavController) {
    var isLoggedIn by remember { mutableStateOf(false) }

    if (!isLoggedIn) {
        navController.navigate("login")
    } else {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Hoşgeldin!", style = MaterialTheme.typography.h5)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    isLoggedIn = false
                    navController.navigate("login")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Çıkış Yap")
            }
        }
    }
}
