package com.mlesniak.engine.engine

import com.mlesniak.engine.core.Canvas
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

// TODO(mlesniak) Use bytebuffer instead of single pixels which is even cooler

// The only function we need is to draw single pixels
// on a canvas. Everything else will be implemented
// by our own functions and algorithms.
@Suppress("MemberVisibilityCanBePrivate")
class Engine(private val canvas: Canvas) {
    fun height() = canvas.height()
    fun width() = canvas.width()

    fun pixel(p: Point, rgb: Int = 0xFFFFFF) {
        // TODO(mlesniak) Apply projection matrix to every point here
        canvas.pixel(p.x.toInt(), p.y.toInt(), rgb)
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

        var x0 = p0.x.roundToInt()
        var y0 = p0.y.roundToInt()
        val p1x = p1.x.roundToInt()
        val p1y = p1.y.roundToInt()
        var error = dx + dy

        while (true) {
            pixel(Point(x0, y0), rgb)
            if (x0 == p1x && y0 == p1y) {
                break
            }
            val e2 = 2 * error
            if (e2 >= dy) {
                if (x0 == p1x) {
                    break
                }
                error += dy
                x0 += sx
            }
            if (e2 <= dx) {
                if (y0 == p1y) {
                    break
                }
                error += dx
                y0 += sy
            }
        }
    }

    fun clear(rgb: Int = 0x000000) {
        canvas.clear(rgb)
    }
}

