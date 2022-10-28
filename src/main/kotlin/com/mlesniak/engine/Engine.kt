package com.mlesniak.engine

import com.mlesniak.engine.core.Canvas
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.sin

data class Point(val x: Int, val y: Int, val z: Int = 0)

// The only function we need is to draw single pixels
// on a canvas. Everything else will be implemented
// by our own functions and algorithms.
@Suppress("MemberVisibilityCanBePrivate")
class Engine(private val canvas: Canvas) {
    fun height() = canvas.height()
    fun width() = canvas.width()

    fun pixel(p: Point, rgb: Int = 0xFFFFFF) {
        canvas.pixel(p.x, p.y, rgb)
    }

    fun circle(x: Int, y: Int, r: Int, rgb: Int = 0xFFFFFF) {
        for (angle in 0..360) {
            val dx = cos(angle * PI / 180.0)
            val dy = sin(angle * PI / 180.0)

            pixel(Point((x + dx * r).toInt(), (y + dy * r).toInt()), rgb)
        }
    }

    // Source: https://en.wikipedia.org/wiki/Bresenham%27s_line_algorithm
    fun line(p0: Point, p1: Point, rgb: Int = 0xFFFFFF) {
        val dx = (p0.x - p1.x).absoluteValue
        val sx = if (p0.x < p1.x) 1 else -1
        val dy = -(p1.y - p0.y).absoluteValue
        val sy = if (p0.y < p1.y) 1 else -1

        var x0 = p0.x
        var y0 = p0.y
        var error = dx + dy

        while (true) {
            pixel(Point(x0, y0), rgb)
            if (x0 == p1.x && y0 == p1.y) {
                break
            }
            val e2 = 2 * error
            if (e2 >= dy) {
                if (x0 == p1.x) {
                    break
                }
                error += dy
                x0 += sx
            }
            if (e2 <= dx) {
                if (y0 == p1.y) {
                    break
                }
                error += dx
                y0 += sy
            }
        }
    }

    fun clear(rgb: Int = 0x000000) {
        for (y in 0..height()) {
            for (x in 0..width()) {
                pixel(Point(x, y), rgb)
            }
        }
    }
}