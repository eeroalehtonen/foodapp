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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodapp.model.FoodProduct
import com.example.foodapp.notification.NotificationHelper
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    onSave: (FoodProduct) -> Unit,
    onShowSavedProducts: () -> Unit
) {
    // Haetaan konteksti ilmoituksen lähettämistä varten
    val context = LocalContext.current

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
                    text = "Lisää tuote",
                    fontSize = 22.sp
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )


                // TUOTTEEN NIMI

                Text(
                    text = "Tuotteen nimi"
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
                        Text("Esimerkiksi: Maito")
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
                    text = "Parasta ennen / Viimeinen käyttöpäivä"
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
                            Text("Valitse päivämäärä")
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

                    Text("Lisää tuote")
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

                    Text("Tallennetut tuotteet")
                }


                Spacer(
                    modifier = Modifier.height(12.dp)
                )


                // TESTINAPPULA ILMOITUKSELLE

                Button(

                    onClick = {
                        val nameToNotify = if (foodName.isNotBlank()) foodName else "Milk"

                        NotificationHelper.showNotification(
                            context = context,
                            productName = nameToNotify
                        )
                    },

                    shape = RoundedCornerShape(12.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    ),

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {

                    Text("Testi-ilmoitus")
                }
            }
        }
    }


    // ------------------------------------------------
    // KALENTERI
    // ------------------------------------------------

    if (showDatePicker) {

        val datePickerState = rememberDatePickerState()

        DatePickerDialog(

            onDismissRequest = {
                showDatePicker = false
            },

            confirmButton = {

                TextButton(

                    onClick = {

                        datePickerState.selectedDateMillis?.let { millis ->

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

                        showDatePicker = false
                    }

                ) {

                    Text("OK")
                }
            },

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

            DatePicker(
                state = datePickerState
            )
        }
    }
}