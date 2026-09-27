package com.example.foodapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.foodapp.model.FoodProduct


// Näkymä, jossa näytetään kaikki tallennetut tuotteet
@Composable
fun SavedProductsScreen(

    // Lista kaikista tallennetuista tuotteista
    foodList: List<FoodProduct>,

    // Funktio tuotteen poistamista varten
    onDelete: (FoodProduct) -> Unit,

    // Funktio takaisin-nappia varten
    onBack: () -> Unit
) {

    // Näkymän otsikko
    Text("Saved products")


    Spacer(
        modifier = Modifier.height(16.dp)
    )


    // Tarkistetaan, onko lista tyhjä
    if (foodList.isEmpty()) {

        // Jos tuotteita ei ole, näytetään tämä teksti
        Text("No saved products")

    } else {

        // Käydään kaikki listan tuotteet yksi kerrallaan läpi
        foodList.forEach { product ->

            // Row asettaa sisällön vaakasuoraan
            Row(

                modifier = Modifier.fillMaxWidth(),

                // Tuotetiedot vasemmalle ja Delete oikealle
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                // Column asettaa tuotteen tiedot allekkain
                Column {

                    // Tuotteen nimi
                    Text(
                        text = product.name
                    )

                    // Tuotteen viimeinen käyttöpäivä
                    Text(
                        text = "Expires: ${product.expirationDate}"
                    )
                }


                // Tuotteen poistamiseen tarkoitettu nappi
                Button(

                    onClick = {

                        // Ilmoitetaan FoodAppille, mikä tuote poistetaan
                        onDelete(product)
                    }
                ) {

                    Text("Delete")
                }
            }


            // Väli tuotteiden välille
            Spacer(
                modifier = Modifier.height(16.dp)
            )
        }
    }


    Spacer(
        modifier = Modifier.height(24.dp)
    )


    // Palataan tuotteen lisäämisnäkymään
    Button(

        onClick = onBack,

        modifier = Modifier.fillMaxWidth()
    ) {

        Text("Back")
    }
}

