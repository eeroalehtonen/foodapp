package com.example.foodapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

import com.example.foodapp.notification.ExpirationScheduler
import com.example.foodapp.notification.NotificationHelper
import com.example.foodapp.screens.AddProductScreen
import com.example.foodapp.screens.SavedProductsScreen
import com.example.foodapp.storage.FoodStorage
import com.example.foodapp.ui.theme.FoodappTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Luodaan ilmoituskanava Androidille
        NotificationHelper.createNotificationChannel(this)

        enableEdgeToEdge()

        setContent {
            FoodappTheme {
                FoodApp()
            }
        }
    }
}


@Composable
fun FoodApp() {
    val context = LocalContext.current

    // Pyydetään NOTIFICATIONS-lupa
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {}
    )

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasPermission) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    var foodList by remember {
        mutableStateOf(FoodStorage.loadProducts(context))
    }

    var showSavedProducts by remember {
        mutableStateOf(false)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 40.dp, start = 20.dp, end = 20.dp)
        ) {
            // ------------------------------------------------
            // NÄKYMÄT
            // ------------------------------------------------

            if (showSavedProducts) {
                SavedProductsScreen(
                    foodList = foodList,
                    onDelete = { product ->
                        foodList = foodList - product
                        FoodStorage.saveProducts(context, foodList)
                    },
                    onBack = {
                        showSavedProducts = false
                    }
                )
            } else {
                AddProductScreen(
                    onSave = { product ->
                        foodList = foodList + product
                        FoodStorage.saveProducts(context, foodList)

                        ExpirationScheduler.scheduleExpirationNotification(
                            context = context,
                            productName = product.name,
                            expirationDate = product.expirationDate
                        )
                    },
                    onShowSavedProducts = {
                        showSavedProducts = true
                    }
                )
            }
        }
    }
}