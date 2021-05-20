package com.more.sandboxapp.ui.widgets

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.more.sandboxapp.ui.theme.mtgForestPrimary
import com.more.sandboxapp.ui.theme.mtgIslandPrimary
import com.more.sandboxapp.ui.theme.mtgMountainPrimary
import com.more.sandboxapp.ui.theme.mtgPlainPrimary
import com.more.sandboxapp.ui.theme.mtgPlainSecondary
import com.more.sandboxapp.ui.theme.mtgSwampPrimary
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun MagicLoadingIndicator(size: Dp = 75.dp, modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(
        animation = tween(2000, easing = LinearEasing),
        repeatMode = RepeatMode.Restart
    ))

    Canvas(modifier = modifier
        .size(size)
        .graphicsLayer { rotationZ = rotation }) {
        val (width) = this.size
        val radius = width * .1f
        val getCenter = getCenterFunc(this.center, width * .4f)

        drawCircle(
            color = mtgPlainPrimary,
            center = getCenter(180f),
            radius = radius)
        drawCircle(
            color = mtgForestPrimary,
            center = getCenter(252f),
            radius = radius)
        drawCircle(
            color = mtgIslandPrimary,
            center = getCenter(108f),
            radius = radius)
        drawCircle(
            color = mtgMountainPrimary,
            center = getCenter(324f),
            radius = radius)
        drawCircle(
            color = mtgSwampPrimary,
            center = getCenter(36f),
            radius = radius)
    }
}

private fun getCenterFunc(center: Offset, radius: Float): (angle: Float) -> Offset {
    return { angle ->
        val radians = angle.toRadians()
        val y = (radius * cos(radians)) + center.y
        val x = (radius * sin(radians)) + center.x
        Offset(x, y)
    }
}

fun Float.toRadians() = (this * (Math.PI/180)).toFloat()

@Preview
@Composable
fun PreviewMagicLoadingIndicator() {
    MagicLoadingIndicator(100.dp)
}