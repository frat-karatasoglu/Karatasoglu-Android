package com.example.myapplication.screens

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.viewmodel.ProfileViewModel
import java.text.SimpleDateFormat
import java.util.*
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.graphics.Color

@Composable
fun EditProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel
) {
    val context = LocalContext.current
    val userProfile by profileViewModel.userProfile.collectAsState()

    var name by remember { mutableStateOf(userProfile.name) }
    var jobTitle by remember { mutableStateOf(userProfile.jobTitle) }
    var resumeUrl by remember { mutableStateOf(userProfile.resumeUrl) }
    var avatarBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var avatarUri by remember { mutableStateOf(userProfile.avatarUri) }

    var showDialog by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            avatarUri = it.toString()
            avatarBitmap = null
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let {
            avatarBitmap = it
            val savedUri = saveImageToGallery(context, it)
            avatarUri = savedUri
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(null)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterHorizontally)
                .clickable { showDialog = true },
            contentAlignment = Alignment.Center
        ) {
            when {
                avatarBitmap != null -> {
                    Image(bitmap = avatarBitmap!!.asImageBitmap(), contentDescription = "Avatar")
                }
                avatarUri.isNotEmpty() -> {
                    Image(
                        painter = rememberAsyncImagePainter(avatarUri),
                        contentDescription = "Avatar",
                        modifier = Modifier.fillMaxSize()
                    )
                }
                else -> Text("Выбрать")
            }
        }

        if (showDialog) {
            showImagePickerDialog(
                onCameraClick = {
                    showDialog = false
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                },
                onGalleryClick = {
                    showDialog = false
                    galleryLauncher.launch("image/*")
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("ФИО") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = jobTitle,
            onValueChange = { jobTitle = it },
            label = { Text("Должность") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = resumeUrl,
            onValueChange = { resumeUrl = it },
            label = { Text("Ссылка на резюме") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    profileViewModel.saveProfile(name, jobTitle, resumeUrl, avatarUri)
                    navController.popBackStack()
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6200EE))
            ) {
                Text("Сохранить")
            }

            Button(
                onClick = {
                    name = ""
                    jobTitle = ""
                    resumeUrl = ""
                    avatarUri = ""
                    avatarBitmap = null
                    profileViewModel.saveProfile("", "", "", "")
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
            ) {
                Text("Сброс", color = Color.White)
            }
        }

    }
}

fun saveImageToGallery(context: Context, bitmap: Bitmap): String {
    val filename = "IMG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.jpg"
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }
    }

    val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    uri?.let {
        context.contentResolver.openOutputStream(it)?.use { stream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        }
        return it.toString()
    }
    return ""
}

@Composable
fun showImagePickerDialog(onCameraClick: () -> Unit, onGalleryClick: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text("Выберите источник") },
        confirmButton = {
            Column {
                TextButton(onClick = onCameraClick) {
                    Text("Камера")
                }
                TextButton(onClick = onGalleryClick) {
                    Text("Галерея")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = {}) {
                Text("Отмена")
            }
        }
    )
}
