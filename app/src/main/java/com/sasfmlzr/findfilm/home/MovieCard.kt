package com.sasfmlzr.findfilm.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState
import com.sasfmlzr.findfilm.colorLightGray

@Preview
@Composable
fun MovieCard(
    @PreviewParameter(StringClassProvider::class) url: String,
    titleName: String = "sss",
    score: String = "ddd"
) {
    val painter = rememberCoilPainter(url, fadeIn = true)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp).padding(vertical = 16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
        Column(verticalArrangement = Arrangement.Bottom) {
            Text(
                text = titleName,
                color = colorLightGray,
                modifier = Modifier.padding(start = 20.dp, bottom = 20.dp),
            )
        }
        Column(
            modifier = Modifier.padding(end = 20.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End

        ) {
            Text(
                text = score,
                color = colorLightGray,
                modifier = Modifier.shadow(5.dp),
            )
        }

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

class StringClassProvider : PreviewParameterProvider<String> {
    override val values = sequenceOf("test1", "test2")
    override val count: Int = values.count()
}