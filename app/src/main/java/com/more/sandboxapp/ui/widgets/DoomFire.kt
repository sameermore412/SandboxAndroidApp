package com.more.sandboxapp.ui.widgets

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val DoomFireTag = "DoomFire"
@Composable
fun DoomFire(modifier: Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasSize = this.size
        val pixelSize = 10f
        Log.i(DoomFireTag, "Measured ${canvasSize.width}, ${canvasSize.height}")
        val pixelArray = initFire(canvasSize)
        //spreadFire()

        val widthPixels = canvasSize.width.toInt()
        val heightPixels = canvasSize.height.toInt()

        drawRect(color = Color.Red, size = this.size)

        drawRoundRect(color = Color.Blue, topLeft = Offset(0f, 0f),
            size = Size(100.dp.toPx(), 100.dp.toPx()),
            cornerRadius = CornerRadius(5.dp.toPx())
        )

        drawIntoCanvas {
            for (column in 0 until widthPixels) {
                // Don't update the bottom fire source row
                for (row in 0 until heightPixels - 1) {
                    it.drawRect(
                        rect = Rect(
                            (column * pixelSize).toFloat(),
                            (row * pixelSize).toFloat(),
                            ((column + 1) * pixelSize).toFloat(),
                            ((row + 1) * pixelSize).toFloat()
                        ),
                        paint = Paint().apply {
                            val currentPixelIndex = column + (widthPixels * row)
                            val currentPixel = pixelArray[currentPixelIndex]
                            color = fireColors[currentPixel]
                        }
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewDoomFire() {
    DoomFire(modifier = Modifier.fillMaxSize())
}

fun initFire(canvasSize: Size): IntArray {
    val pixelArray = IntArray(canvasSize.width.toInt() * canvasSize.height.toInt()) { 0 }

    val overFlowFireIndex = canvasSize.width.toInt() * canvasSize.height.toInt()

    for (column in 0 until canvasSize.width.toInt()) {
        val pixelIndex = (overFlowFireIndex - canvasSize.width.toInt()) + column
        pixelArray[pixelIndex] = 36
    }
    //Log.i(DoomFireTag, "${pixelArray.slice((overFlowFireIndex - canvasSize.width.toInt()).. (overFlowFireIndex - canvasSize.width.toInt()) + canvasSize.width.toInt() -1).joinToString(",")}")
    return pixelArray
}

val fireColors = arrayOf(
    Color(7, 7, 7),
    Color(31, 7, 7),
    Color(47, 15, 7),
    Color(71, 15, 7),
    Color(87, 23, 7),
    Color(103, 31, 7),
    Color(119, 31, 7),
    Color(143, 39, 7),
    Color(159, 47, 7),
    Color(175, 63, 7),
    Color(191, 71, 7),
    Color(199, 71, 7),
    Color(223, 79, 7),
    Color(223, 87, 7),
    Color(223, 87, 7),
    Color(215, 95, 7),
    Color(215, 95, 7),
    Color(215, 95, 7),
    Color(215, 103, 15),
    Color(207, 111, 15),
    Color(207, 119, 15),
    Color(207, 127, 15),
    Color(207, 135, 23),
    Color(199, 135, 23),
    Color(199, 143, 23),
    Color(199, 151, 31),
    Color(191, 159, 31),
    Color(191, 159, 31),
    Color(191, 167, 39),
    Color(191, 167, 39),
    Color(191, 175, 47),
    Color(183, 175, 47),
    Color(183, 183, 47),
    Color(183, 183, 55),
    Color(207, 207, 111),
    Color(223, 223, 159),
    Color(239, 239, 199),
    Color(255, 255, 255)
)