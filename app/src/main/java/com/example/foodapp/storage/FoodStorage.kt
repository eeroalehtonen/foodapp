package com.example.foodapp.storage

import android.content.Context
import com.example.foodapp.model.FoodProduct


// Object tarkoittaa, että FoodStoragesta tarvitaan vain yksi yhteinen versio
object FoodStorage {

    // SharedPreferences-tiedoston nimi
    private const val PREFS_NAME = "food_app"

    // Avain, jonka alle tuotteet tallennetaan
    private const val PRODUCTS_KEY = "products"


    // Tallentaa kaikki tuotteet puhelimen muistiin
    fun saveProducts(
        context: Context,
        products: List<FoodProduct>
    ) {

        // Avataan sovelluksen SharedPreferences
        val prefs = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )


        // Muutetaan FoodProduct-oliot tekstiksi tallennusta varten
        //
        // Esimerkiksi:
        // FoodProduct("Milk", "30.09.2026")
        //
        // muuttuu muotoon:
        // "Milk|30.09.2026"

        val productStrings = products.map {

            "${it.name}|${it.expirationDate}"

        }.toSet()


        // Tallennetaan tuotteet SharedPreferencesiin
        prefs.edit()
            .putStringSet(PRODUCTS_KEY, productStrings)
            .apply()
    }


    // Hakee aikaisemmin tallennetut tuotteet
    fun loadProducts(
        context: Context
    ): List<FoodProduct> {


        // Avataan sama SharedPreferences
        val prefs = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )


        // Haetaan tallennetut tuotteet
        val productStrings =
            prefs.getStringSet(
                PRODUCTS_KEY,
                emptySet()
            ) ?: emptySet()


        // Muutetaan tallennetut tekstit takaisin FoodProduct-olioiksi
        return productStrings.mapNotNull { product ->

            // Erotetaan nimi ja päivämäärä |-merkin kohdalta
            val parts = product.split("|")


            // Tarkistetaan, että molemmat tiedot löytyvät
            if (parts.size == 2) {

                // Luodaan FoodProduct uudelleen
                FoodProduct(
                    name = parts[0],
                    expirationDate = parts[1]
                )

            } else {

                // Virheellistä tietoa ei lisätä listaan
                null
            }
        }
    }
}
