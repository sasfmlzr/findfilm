package com.sasfmlzr.findfilm

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val Red200 = Color(0xfff297a2)
val Red300 = Color(0xffea6d7e)
val Red700 = Color(0xffdd0d3c)
val Red800 = Color(0xffd00036)
val Red900 = Color(0xffc20029)
val colorPrimaryDark = Color(0xFF0A186E)
val colorAccent = Color(0xFFFF4081)
val colorPrimary = Color(0xff0e08b7)
val colorWhite = Color(0xffffffff)
val colorLightGray = Color(0xffeaeaee)
val colorLightGrayWithAlpha = Color(0x80eaeaee)
val colorBlack = Color(0xFF000000)
val colorBlackWithAlpha  = Color(0x80000000)
val colorFavoriteIcon = Color(0xffad563b)

private val LightThemeColors = lightColors(
    primary = colorPrimary,

    onPrimary = Color.White,
    secondary = Red700,
    secondaryVariant = Red900,
    onSecondary = Color.White,
    error = Red800
)

private val DarkThemeColors = darkColors(
    primary = Red300,
    primaryVariant = Red700,
    onPrimary = Color.Black,
    secondary = Red300,
    onSecondary = Color.Black,
    error = Red200
)

val JetnewsShapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(8.dp)
)

@Composable
fun JetnewsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (darkTheme) DarkThemeColors else LightThemeColors,
        shapes = JetnewsShapes,
        content = content
    )
}
