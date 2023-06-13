package com.hieuluu.cardreader.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.hieuluu.cardreader.R

@OptIn(ExperimentalTextApi::class)
private val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

@OptIn(ExperimentalTextApi::class)
private val RubikFontName = GoogleFont(name = "Rubik")

@OptIn(ExperimentalTextApi::class)
private val Rubik = FontFamily(Font(googleFont = RubikFontName, fontProvider = provider))

val Typography = Typography(
    displayLarge = TextStyle(fontFamily = Rubik),
    displayMedium = TextStyle(fontFamily = Rubik),
    displaySmall = TextStyle(fontFamily = Rubik),
    headlineLarge = TextStyle(fontFamily = Rubik),
    headlineMedium = TextStyle(fontFamily = Rubik),
    headlineSmall = TextStyle(fontFamily = Rubik),
    titleLarge = TextStyle(fontFamily = Rubik),
    titleMedium = TextStyle(fontFamily = Rubik),
    titleSmall = TextStyle(fontFamily = Rubik),
    bodyLarge = TextStyle(fontFamily = Rubik),
    bodyMedium = TextStyle(fontFamily = Rubik),
    bodySmall = TextStyle(fontFamily = Rubik),
    labelLarge = TextStyle(fontFamily = Rubik),
    labelMedium = TextStyle(fontFamily = Rubik),
    labelSmall = TextStyle(fontFamily = Rubik),
)

// Set of Material typography styles to start with
//val Typography = Typography(
//    bodyLarge = TextStyle(
//        fontFamily = FontFamily.Default,
//        fontWeight = FontWeight.Normal,
//        fontSize = 16.sp,
//        lineHeight = 24.sp,
//        letterSpacing = 0.5.sp
//    )
//    /* Other default text styles to override
//    titleLarge = TextStyle(
//        fontFamily = FontFamily.Default,
//        fontWeight = FontWeight.Normal,
//        fontSize = 22.sp,
//        lineHeight = 28.sp,
//        letterSpacing = 0.sp
//    ),
//    labelSmall = TextStyle(
//        fontFamily = FontFamily.Default,
//        fontWeight = FontWeight.Medium,
//        fontSize = 11.sp,
//        lineHeight = 16.sp,
//        letterSpacing = 0.5.sp
//    )
//    */
//)