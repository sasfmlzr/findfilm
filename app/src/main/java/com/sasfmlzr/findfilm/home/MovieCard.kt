package com.sasfmlzr.findfilm.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState

@Preview
@Composable
fun MovieCard(@PreviewParameter(StringClassProvider::class) url: String
) {
    val painter = rememberCoilPainter(url,  fadeIn = true)
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(250.dp),
        shape = RoundedCornerShape(16.dp)) {
        Image(painter= painter,
            contentDescription =null)
        Text(text = "blablable")
    }

    when (painter.loadState) {
        is ImageLoadState.Loading -> {
            // Display a circular progress indicator whilst loading
            CircularProgressIndicator(Modifier.wrapContentSize(Alignment.Center))
        }
        is ImageLoadState.Error -> {
            // If you wish to display some content if the request fails
        }
}

   }

class StringClassProvider: PreviewParameterProvider<String> {
    override val values = sequenceOf("test1","test2")
    override val count: Int = values.count()
}