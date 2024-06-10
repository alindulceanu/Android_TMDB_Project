package com.example.movie.ui.tools

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProgressBar (
    percentage: Float,
    fontSize: TextUnit = 35.sp,
    radius: Dp = 50.dp,
    strokeWidth: Dp = 4.dp,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(radius * 2.5f)
    ){
        Canvas(
            modifier = Modifier
                .size(radius * 1.8f)
        ) {
            drawCircle(
                color = Color(0xFF230C42),
                (radius * 1.2f).toPx(),
            )
        }

        Canvas(
            modifier = Modifier
                .size(radius * 2f)
        ) {
            drawArc(
                color = getProgressColor(percentage, true),
                -90f,
                360f,
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }
        Canvas(
            modifier = Modifier
            .size(radius * 2f)

        ) {
            drawArc(
                color = getProgressColor(percentage, false),
                -90f,
                3.6f * percentage,
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round),
            )
        }
        Text(
            text = percentage.toInt().toString() + "%",
            color = Color.White,
            fontSize = fontSize,
        )
    }
}

private fun getProgressColor(progress: Float, forBackground: Boolean): Color {
    return when (forBackground) {
        true -> {
            when (progress) {
                in 0f..39f -> Color(0xFF500000) // Equivalent to android.graphics.Color.rgb(80, 0, 0)
                in 40f..59f -> Color(0xFF505000) // Equivalent to android.graphics.Color.rgb(80, 80, 0)
                else -> Color(0xFF005000)      // Equivalent to android.graphics.Color.rgb(0, 80, 0)
            }
        }
        false -> {
            when (progress) {
                in 0f..39f -> Color(0xFFC80000) // Equivalent to android.graphics.Color.rgb(200, 0, 0)
                in 40f..59f -> Color(0xFFC8C800) // Equivalent to android.graphics.Color.rgb(200, 200, 0)
                else -> Color(0xFF00C800)      // Equivalent to android.graphics.Color.rgb(0, 200, 0)
            }
        }
    }
}

@Preview
@Composable
fun PreviewProgressBar(){
    ProgressBar(percentage = 80f)
}