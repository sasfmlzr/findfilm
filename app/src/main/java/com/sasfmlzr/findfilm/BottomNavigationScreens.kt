package com.sasfmlzr.findfilm

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Accessible
import androidx.compose.material.icons.sharp.ChildCare
import androidx.compose.material.icons.sharp.FavoriteBorder
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavigationScreens(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector
) {
    object First :
        BottomNavigationScreens("Frankendroid", R.string.first_route, Icons.Sharp.ChildCare)

    object Second :
        BottomNavigationScreens("Pumpkin", R.string.second_route, Icons.Sharp.FavoriteBorder)

    object Third : BottomNavigationScreens("Ghost", R.string.third_route, Icons.Sharp.Accessible)
}