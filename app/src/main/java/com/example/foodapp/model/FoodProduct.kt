package com.example.foodapp.model

// FoodProduct kuvaa yhtä sovellukseen tallennettua ruokatuotetta
data class FoodProduct(

    // Tuotteen nimi, esimerkiksi "Milk"
    val name: String,

    // Tuotteen viimeinen käyttöpäivä / parasta ennen
    // Esimerkiksi "30.09.2026"
    val expirationDate: String
)

