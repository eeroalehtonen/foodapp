package com.example.foodapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


// ----------------------------------------------------
// TUMMA TEEMA
// ----------------------------------------------------

private val DarkColorScheme = darkColorScheme(

    // Sovelluksen pääväri
    primary = FridgeGreen,

    // Tekstin väri vihreän päällä
    onPrimary = Color.White,

    // Tumma taustaväri
    background = Color(0xFF182018),

    // Teksti tummalla taustalla
    onBackground = Color(0xFFF2F2E9),

    // Korttien väri
    surface = Color(0xFF243024),

    // Teksti korttien päällä
    onSurface = Color(0xFFF2F2E9),

    // Reunusten väri
    outline = FridgeOutline
)


// ----------------------------------------------------
// VAALEA TEEMA
// ----------------------------------------------------

private val LightColorScheme = lightColorScheme(

    // Pääväri
    // Esimerkiksi Save-napin väri
    primary = FridgeGreen,

    // Teksti vihreän napin päällä
    onPrimary = Color.White,

    // Sovelluksen lämmin vaalea tausta
    background = FridgeBackground,

    // Teksti taustan päällä
    onBackground = FridgeDark,

    // Korttien vaalea vihertävä tausta
    surface = FridgeCard,

    // Teksti korttien päällä
    onSurface = FridgeDark,

    // Reunusten väri
    outline = FridgeOutline
)


// ----------------------------------------------------
// SOVELLUKSEN TEEMA
// ----------------------------------------------------

@Composable
fun FoodappTheme(

    // Käyttää puhelimen tummaa teemaa,
    // jos käyttäjällä on dark mode käytössä
    darkTheme: Boolean = isSystemInDarkTheme(),

    content: @Composable () -> Unit

) {

    // Valitaan tumma tai vaalea värimaailma
    val colorScheme = if (darkTheme) {

        DarkColorScheme

    } else {

        LightColorScheme
    }


    // Annetaan valittu värimaailma koko sovellukselle
    MaterialTheme(

        colorScheme = colorScheme,

        // Käytetään projektin Typography-asetuksia
        typography = Typography,

        content = content
    )
}