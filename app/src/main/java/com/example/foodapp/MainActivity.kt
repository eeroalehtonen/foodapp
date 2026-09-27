package com.example.foodapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

import com.example.foodapp.notification.ExpirationScheduler
import com.example.foodapp.notification.NotificationHelper
import com.example.foodapp.screens.AddProductScreen
import com.example.foodapp.screens.SavedProductsScreen
import com.example.foodapp.storage.FoodStorage
import com.example.foodapp.ui.theme.FoodappTheme


// Sovelluksen pääaktiviteetti
class MainActivity : ComponentActivity() {

    // Tätä kutsutaan, kun sovellus käynnistyy
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // Luodaan ilmoituskanava
        NotificationHelper.createNotificationChannel(this)

        // Mahdollistaa käyttöliittymän piirtämisen
        // koko näytön alueelle
        enableEdgeToEdge()

        // Käynnistetään Jetpack Compose
        setContent {

            // Käytetään FoodAppin omaa teemaa
            FoodappTheme {

                // Käynnistetään sovelluksen pääkomponentti
                FoodApp()
            }
        }
    }
}


// Sovelluksen pääkomponentti
@Composable
fun FoodApp() {

    // Haetaan Android Context
    // Contextia tarvitaan tallennukseen ja ilmoituksiin
    val context = LocalContext.current


    // ------------------------------------------------
    // TUOTELISTA
    // ------------------------------------------------

    // Kun sovellus käynnistyy, haetaan aikaisemmin
    // tallennetut tuotteet FoodStoragesta
    var foodList by remember {

        mutableStateOf(
            FoodStorage.loadProducts(context)
        )
    }


    // ------------------------------------------------
    // NÄKYMÄN VAIHTAMINEN
    // ------------------------------------------------

    // false = tuotteen lisäämisnäkymä
    // true = tallennettujen tuotteiden näkymä
    var showSavedProducts by remember {
        mutableStateOf(false)
    }


    // ------------------------------------------------
    // SOVELLUKSEN POHJA
    // ------------------------------------------------

    Scaffold(

        // Sovellus käyttää koko näyttöä
        modifier = Modifier.fillMaxSize(),

        // Käytetään Fridgeifyn teemassa määriteltyä taustaväriä
        containerColor = MaterialTheme.colorScheme.background

    ) { innerPadding ->


        // Column asettaa elementit pystysuoraan
        Column(

            modifier = Modifier

                // Huomioidaan esimerkiksi puhelimen yläpalkki
                .padding(innerPadding)

                // Lisätään tyhjää tilaa reunoille
                .padding(
                    top = 40.dp,
                    start = 20.dp,
                    end = 20.dp
                )
        ) {


            // ------------------------------------------------
            // SAVED PRODUCTS -NÄKYMÄ
            // ------------------------------------------------

            if (showSavedProducts) {

                SavedProductsScreen(

                    // Lähetetään nykyinen tuotelista näkymälle
                    foodList = foodList,


                    // Suoritetaan kun käyttäjä painaa Delete
                    onDelete = { product ->

                        // Poistetaan tuote listasta
                        foodList = foodList - product

                        // Tallennetaan uusi lista puhelimeen
                        FoodStorage.saveProducts(
                            context,
                            foodList
                        )
                    },


                    // Back-nappi palauttaa lisäysnäkymään
                    onBack = {

                        showSavedProducts = false
                    }
                )


            } else {


                // ------------------------------------------------
                // ADD PRODUCT -NÄKYMÄ
                // ------------------------------------------------

                AddProductScreen(


                    // Suoritetaan kun käyttäjä painaa
                    // Save product
                    onSave = { product ->


                        // Lisätään uusi tuote listaan
                        foodList = foodList + product


                        // Tallennetaan päivitetty lista
                        // puhelimen muistiin
                        FoodStorage.saveProducts(
                            context,
                            foodList
                        )


                        // Ajastetaan ilmoitus päivää ennen
                        // tuotteen vanhenemispäivää
                        ExpirationScheduler
                            .scheduleExpirationNotification(

                                context = context,

                                productName = product.name,

                                expirationDate =
                                    product.expirationDate
                            )
                    },


                    // Saved products -nappi
                    onShowSavedProducts = {

                        // Vaihdetaan Saved products -näkymään
                        showSavedProducts = true
                    }
                )
            }
        }
    }
}