package com.bogdan801.experiments

import android.graphics.PointF
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bogdan801.util_library.mapRange
import kotlin.math.pow
import kotlin.math.roundToInt

@Composable
fun CustomSlider(
    modifier: Modifier = Modifier,
    sliderValue: Double = 0.0,
    handleSize: Dp = 30.dp,
    handleColor: Color = MaterialTheme.colors.primary,
    handleBorderWidth: Dp = 2.dp,
    handleBorderColor: Color = MaterialTheme.colors.onPrimary,
    dashColor: Color = MaterialTheme.colors.onSecondary,
    lineStartColor: Color = MaterialTheme.colors.secondary,
    lineEndColor: Color = MaterialTheme.colors.primaryVariant,
    onValueUpdated: (newValue: Double) -> Unit = {},
    onNewValueSet: (newValue: Double) -> Unit = {}
) {
    val density = LocalDensity.current
    BoxWithConstraints(
        modifier = modifier
    ){
        val width = maxWidth
        val height = maxHeight
        var offsetX by remember {
            mutableStateOf(
                mapRange(
                    sliderValue,
                    0.0..1.0,
                    0.0..(width.value - handleSize.value).toDouble()
                ).dp
            )
        }

        val infiniteTransition = rememberInfiniteTransition()
        val phase by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = -20000f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 500000,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            )
        )

        Canvas(modifier = modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val canvasHandleSize = handleSize.toPx()
            val canvasHandleRadius = canvasHandleSize/2
            val canvasSliderPosX = mapRange(sliderValue.toFloat(), 0f..1f, (canvasHandleRadius..canvasWidth-canvasHandleRadius))

            val curveStartPoint = PointF(canvasHandleRadius, canvasHeight - canvasHandleRadius)
            val curveEndPoint = PointF(canvasWidth - canvasHandleRadius, canvasHeight - canvasHandleRadius)

            val dashPath = Path().apply {
                moveTo(curveStartPoint.x, curveStartPoint.y)
                quadraticBezierTo(canvasWidth/2, canvasHandleSize - curveStartPoint.y, curveEndPoint.x, curveEndPoint.y)
            }

            drawPath(
                path = dashPath,
                color = dashColor,
                style = Stroke(
                    width = 1.dp.toPx(),
                    cap = StrokeCap.Round,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(13f, 13f), phase = phase)
                )
            )



            val finishedPath = Path().apply {
                val resolution = (sliderValue * 20).roundToInt()
                moveTo(canvasHandleRadius, canvasHeight - canvasHandleRadius)
                val canvasStep = (canvasSliderPosX - curveStartPoint.x) / resolution
                (1 ..  resolution).forEach { i ->
                    val xInCanvasSpace = curveStartPoint.x + (i * canvasStep)
                    val ratio = i * (sliderValue.toFloat()/resolution)
                    val yCalculated = canvasHeight - (canvasHandleSize/2) - curveHeightOffset(ratio, canvasHeight-canvasHandleSize)
                    lineTo(xInCanvasSpace, yCalculated)
                    moveTo(xInCanvasSpace, yCalculated)
                }
            }

            drawPath(
                path = finishedPath,
                color = lerp(lineStartColor, lineEndColor, sliderValue.toFloat()),
                style = Stroke(
                    width = 5.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )
        }
        Box(
            modifier = Modifier
                .size(handleSize)
                .align(Alignment.BottomStart)
                .offset(x = offsetX, y = -curveHeightOffset(sliderValue, height - handleSize))
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        val newOffsetX = offsetX + with(density) { delta.toDp() }
                        if (newOffsetX in 0.dp..width - handleSize) {
                            offsetX = newOffsetX
                            onValueUpdated(
                                mapRange(
                                    offsetX,
                                    0.dp..width - handleSize,
                                    0.dp..1.dp
                                ).value.toDouble()
                            )
                        }
                    },
                    onDragStopped = {
                        onNewValueSet(
                            mapRange(
                                offsetX,
                                0.dp..width - handleSize,
                                0.dp..1.dp
                            ).value.toDouble()
                        )
                    }
                )
                .clip(CircleShape)
                .background(handleBorderColor),
        ){
            Box(
                modifier = Modifier
                    .padding(handleBorderWidth)
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(handleColor)
            )
        }
        LaunchedEffect(key1 = sliderValue){
            offsetX = mapRange(
                sliderValue,
                0.0..1.0,
                0.0..(width.value - handleSize.value).toDouble()
            ).dp
        }
    }
}

fun curveHeightOffset(x: Double, maxHeight: Dp): Dp = (((-4 * (x - 0.5).pow(2)) + 1) * maxHeight.value).dp
fun curveHeightOffset(x: Float, maxHeight: Float): Float = (((-4 * (x - 0.5).pow(2)) + 1) * maxHeight).toFloat()