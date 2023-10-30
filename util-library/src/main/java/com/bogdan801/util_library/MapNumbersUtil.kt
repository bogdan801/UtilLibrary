package com.bogdan801.util_library

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun mapRange(number: Int, prevRange: IntRange, newRange: IntRange) : Int {
    val ratio = number.toFloat() / (prevRange.last - prevRange.first)
    return (ratio * (newRange.last - newRange.first) + newRange.first).toInt()
}

fun mapRange(number: Double, prevRange: ClosedRange<Double>, newRange: ClosedRange<Double>) : Double {
    val ratio = number / (prevRange.endInclusive - prevRange.start)
    return (ratio * (newRange.endInclusive - newRange.start) + newRange.start)
}

fun mapRange(number: Float, prevRange: ClosedRange<Float>, newRange: ClosedRange<Float>) : Float {
    return mapRange(number.toDouble(), prevRange.start.toDouble()..prevRange.endInclusive.toDouble(), newRange.start.toDouble()..newRange.endInclusive.toDouble()).toFloat()
}

fun mapRange(number: Dp, prevRange: ClosedRange<Dp>, newRange: ClosedRange<Dp>) : Dp {
    val ratio = number.value / (prevRange.endInclusive.value - prevRange.start.value)
    return (ratio * (newRange.endInclusive.value - newRange.start.value) + newRange.start.value).dp
}
