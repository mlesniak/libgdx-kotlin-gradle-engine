package com.mlesniak.engine

import com.mlesniak.engine.core.Canvas
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// The only function we need is to draw single pixels
// on a canvas. Everything else will be implemented
// by our own functions and algorithms.
class Engine(private val canvas: Canvas) {
    fun height() = canvas.height()
    fun width() = canvas.width()

    fun pixel(x: Int, y: Int, rgb: Int) {
        canvas.pixel(x, y, rgb)
    }

    fun circle(x: Int, y: Int, r: Int, rgb: Int) {
        for (angle in 0..360) {
            val dx = cos(angle * PI / 180.0)
            val dy = sin(angle * PI / 180.0)

            pixel((x + dx * r).toInt(), (y + dy * r).toInt(), rgb)
        }
    }

    fun clear(rgb: Int) {
        for (y in 0..height()) {
            for (x in 0..width()) {
                pixel(x, y, rgb)
            }
        }
    }
}