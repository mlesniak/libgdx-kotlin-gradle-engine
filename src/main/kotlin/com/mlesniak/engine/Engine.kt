package com.mlesniak.engine

import com.mlesniak.engine.core.Canvas
import kotlin.math.PI
import kotlin.math.absoluteValue
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

    // Source: https://en.wikipedia.org/wiki/Bresenham%27s_line_algorithm
    fun line(x0_: Int, y0_: Int, x1: Int, y1: Int, rgb: Int) {
        val dx = (x0_ - x1).absoluteValue
        val sx = if (x0_ < x1) 1 else -1
        val dy = -(y1 - y0_).absoluteValue
        val sy = if (y0_ < y1) 1 else -1

        var x0 = x0_
        var y0 = y0_
        var error = dx + dy

        while (true) {
            pixel(x0, y0, rgb)
            if (x0 == x1 && y0 == y1) {
                break
            }
            val e2 = 2 * error
            if (e2 >= dy) {
                if (x0 == x1) {
                    break
                }
                error += dy
                x0 += sx
            }
            if (e2 <= dx) {
                if (y0 == y1) {
                    break
                }
                error += dx
                y0 += sy
            }
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