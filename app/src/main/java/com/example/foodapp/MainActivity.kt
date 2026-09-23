package com.example.foodapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.foodapp.ui.theme.FoodappTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    // Käyttäjän kirjoittama ruokatuote
    var foodName by remember { mutableStateOf("") }

    // Käyttäjän kirjoittama viimeinen käyttöpäivä
    var expirationDate by remember { mutableStateOf("") }

    // Tänne tallennetaan tuotteet
    var foodList by remember {
        mutableStateOf(listOf<Pair<String, String>>())
    }

    var showSavedProducts by remember {
        mutableStateOf(false)
    }
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 300.dp, start = 16.dp, end = 16.dp)
        ) {

            // TALLENNETTUJEN TUOTTEIDEN NÄKYMÄ
            if (showSavedProducts) {

                Text("Saved products")

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                if (foodList.isEmpty()) {
                    Text("No saved products")
                }

                foodList.forEach { food ->

                    Text(
                        text = "${food.first} - ${food.second}"
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                Button(
                    onClick = {
                        showSavedProducts = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Back")
                }


            } else {

                // TUOTTEEN LISÄYSNÄKYMÄ

                Text("Add product")

                Spacer(
                    modifier = Modifier.height(16.dp)
                )
                // Ruokatuotteen tekstikenttä
                OutlinedTextField(
                    value = foodName,
                    onValueChange = { foodName = it },
                    label = {
                        Text("Food product")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )
                // Päivämäärän tekstikenttä
                OutlinedTextField(
                    value = expirationDate,
                    onValueChange = { expirationDate = it },
                    label = {
                        Text("Expiration date")
                    },
                    placeholder = {
                        Text("DD.MM.YYYY")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                // TALLENNUSNAPPI
                Button(
                    onClick = {

                        if (
                            foodName.isNotBlank() &&
                            expirationDate.isNotBlank()
                        ) {

                            foodList = foodList + Pair(
                                foodName,
                                expirationDate
                            )

                            foodName = ""
                            expirationDate = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save")
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                // AVAA TALLENNETUT TUOTTEET
                Button(
                    onClick = {
                        showSavedProducts = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Saved products")
                }
            }
        }
    }
}