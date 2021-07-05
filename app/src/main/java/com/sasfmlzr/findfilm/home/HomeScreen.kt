package com.sasfmlzr.findfilm.home

import android.widget.Toast
import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationInstance
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.statusBarsHeight
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import com.sasfmlzr.findfilm.*
import com.sasfmlzr.findfilm.colorLightGray
import com.sasfmlzr.findfilm.request.FindFilmRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.selects.select

/**
 * Stateful HomeScreen which manages state using [produceUiState]
 *
 * @param postsRepository data source for this screen
 * @param navigateToArticle (event) request navigation to Article screen
 * @param openDrawer (event) request opening the app drawer
 * @param scaffoldState (state) state for the [Scaffold] component on this screen
 */
@Composable
fun HomeScreen(
    //findFilmRepository: FindFilmRepository,
  //    navigateToArticle: (String) -> Unit,
    //  openDrawer: () -> Unit,
    //  scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    // val modifier = Modifier.padding(innerPadding)

    val viewModel = viewModel(HomeViewModel::class.java)

    val viewState by viewModel.state.collectAsState()



    Column(Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(text = "TMDB")
            }
        )
        CreateSelector()
        Box(Modifier.fillMaxSize()) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(colorPrimary))

           Row() {

           }
        }
    }
}

@Composable
fun CreateSelector() {
    val isLeftSelected= remember {  mutableStateOf(true)}
    val leftColor = if (isLeftSelected.value ) colorLightGray else colorLightGrayWithAlpha
    val rightColor = if (!isLeftSelected.value) colorLightGray else colorLightGrayWithAlpha
    val leftTextColor = if (isLeftSelected.value ) colorBlack else colorBlackWithAlpha
    val rightTextColor = if (!isLeftSelected.value ) colorBlack else colorBlackWithAlpha
    val context = LocalContext.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primarySurface)
            .padding(horizontal = 20.dp)
    ) {
        Button(
            onClick = {
                isLeftSelected.value = true
                Toast.makeText(context, "Now clicked", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .padding(
                    top = 10.dp,
                    bottom = 25.dp
                )
                .weight(1f)
                .defaultMinSize(minHeight = 40.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = leftColor
            ),
            shape = RoundedCornerShape(topStart = 40f,
                topEnd = 0f,
                bottomStart = 40f,
                bottomEnd = 0f)
        ) {
            Text(text = "Now", color = leftTextColor)
        }
        Button(
            onClick = {
                isLeftSelected.value = false
                Toast.makeText(context, "Soon clicked", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .padding(
                    top = 10.dp,
                    bottom = 25.dp
                )
                .weight(1f)
                .defaultMinSize(minHeight = 40.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = rightColor
            ),
            shape = RoundedCornerShape(topStart = 0f,
            topEnd = 40f,
            bottomStart = 0f,
            bottomEnd = 40f)
        ) {
            Text(text = "Soon", color = rightTextColor)
        }
    }
}