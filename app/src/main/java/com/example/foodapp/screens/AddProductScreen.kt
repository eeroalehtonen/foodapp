package com.example.foodapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodapp.model.FoodProduct
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    onSave: (FoodProduct) -> Unit,
    onShowSavedProducts: () -> Unit
) {

    // Käyttäjän kirjoittama tuotteen nimi
    var foodName by remember {
        mutableStateOf("")
    }

    // Valittu päivämäärä tekstinä
    var expirationDate by remember {
        mutableStateOf("")
    }

    // Määrittää näkyykö kalenteri
    var showDatePicker by remember {
        mutableStateOf(false)
    }


    // Koko lisäysnäkymän sisältö
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        // Sovelluksen nimi
        Text(
            text = "FoodApp",
            fontSize = 32.sp
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        // Lyhyt kuvaus
        Text(
            text = "Keep track of your food and reduce waste",
            fontSize = 14.sp
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )


        // Kortti, jonka sisällä tuotteen lisääminen tapahtuu
        Card(
            modifier = Modifier.fillMaxWidth(),

            shape = RoundedCornerShape(20.dp),

            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                // Kortin otsikko
                Text(
                    text = "Add a product",
                    fontSize = 22.sp
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )


                // TUOTTEEN NIMI

                Text(
                    text = "Product name"
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                OutlinedTextField(
                    value = foodName,

                    onValueChange = {
                        foodName = it
                    },

                    placeholder = {
                        Text("e.g. Milk")
                    },

                    singleLine = true,

                    shape = RoundedCornerShape(12.dp),

                    modifier = Modifier.fillMaxWidth()
                )


                Spacer(
                    modifier = Modifier.height(20.dp)
                )


                // PÄIVÄMÄÄRÄ

                Text(
                    text = "Expiry / Best-before date"
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )


                // Box mahdollistaa koko kentän klikkaamisen
                Box {

                    OutlinedTextField(

                        value = expirationDate,

                        // Käyttäjä ei kirjoita päivämäärää itse
                        onValueChange = {},

                        readOnly = true,

                        placeholder = {
                            Text("Select date")
                        },

                        // Kalenteri-ikoni kentän oikeassa reunassa
                        trailingIcon = {

                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Select date"
                            )
                        },

                        shape = RoundedCornerShape(12.dp),

                        modifier = Modifier.fillMaxWidth()
                    )


                    // Läpinäkyvä klikattava alue kentän päällä
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable {
                                showDatePicker = true
                            }
                    )
                }


                Spacer(
                    modifier = Modifier.height(28.dp)
                )


                // SAVE-NAPPI

                Button(

                    onClick = {

                        // Tarkistetaan että nimi ja päivämäärä löytyvät
                        if (
                            foodName.isNotBlank() &&
                            expirationDate.isNotBlank()
                        ) {

                            // Luodaan uusi tuote
                            val product = FoodProduct(
                                name = foodName,
                                expirationDate = expirationDate
                            )

                            // Tallennetaan tuote
                            onSave(product)

                            // Tyhjennetään kentät
                            foodName = ""
                            expirationDate = ""
                        }
                    },

                    shape = RoundedCornerShape(12.dp),

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {

                    Text("Save product")
                }


                Spacer(
                    modifier = Modifier.height(12.dp)
                )


                // TALLENNETTUJEN TUOTTEIDEN NAPPI

                OutlinedButton(

                    onClick = onShowSavedProducts,

                    shape = RoundedCornerShape(12.dp),

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {

                    Text("View saved products")
                }
            }
        }
    }


    // ------------------------------------------------
    // KALENTERI
    // ------------------------------------------------

    if (showDatePicker) {

        // Kalenterin tila
        val datePickerState = rememberDatePickerState()


        DatePickerDialog(

            // Suljetaan kalenteri jos käyttäjä painaa sen ulkopuolelle
            onDismissRequest = {
                showDatePicker = false
            },


            // OK-nappi
            confirmButton = {

                TextButton(

                    onClick = {

                        // Haetaan käyttäjän valitsema päivämäärä
                        datePickerState.selectedDateMillis?.let { millis ->

                            // Muutetaan päivämäärä muotoon DD.MM.YYYY
                            val formatter =
                                SimpleDateFormat(
                                    "dd.MM.yyyy",
                                    Locale.getDefault()
                                )

                            expirationDate =
                                formatter.format(
                                    Date(millis)
                                )
                        }

                        // Suljetaan kalenteri
                        showDatePicker = false
                    }

                ) {

                    Text("OK")
                }
            },


            // Cancel-nappi
            dismissButton = {

                TextButton(

                    onClick = {
                        showDatePicker = false
                    }

                ) {

                    Text("Cancel")
                }
            }

        ) {

            // Varsinainen kalenteri
            DatePicker(
                state = datePickerState
            )
        }
    }
}