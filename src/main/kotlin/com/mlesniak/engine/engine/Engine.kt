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
//
// TODO?
// - we're mixing perspective functions and pure drawing functions; should
//   this be split up (pixel, circle, line and triangle are pure, model
//   is not).
@Suppress("MemberVisibilityCanBePrivate")
class Engine(private val canvas: Canvas) {
    private var zbuffer = Array(canvas.height * canvas.width) { -Float.MAX_VALUE }

    var projectionMatrix: Matrix =
        BaseMatrix.scale(1.0f, 1.0f, 1.0f) *
            BaseMatrix.translate((canvas.width / 2).toFloat(), (canvas.height / 2).toFloat())

    fun height() = canvas.height
    fun width() = canvas.width

    fun pixel(p: Vector, rgb: Int = 0xFFFFFF) {
        val py = p.y.toInt()
        val px = p.x.toInt()
        val curz = zbuffer[py * canvas.height + px]
        // println(p.z)
        if (p.z > curz) {
            canvas.pixel(px, py, rgb)
            zbuffer[py * canvas.height + px] = p.z
        }
    }

    fun clear(rgb: Int = 0xCCCCCC) {
        canvas.clear(rgb)
        zbuffer = Array(canvas.height * canvas.width) { -Float.MAX_VALUE }
    }

    fun circle(x: Int, y: Int, r: Int, rgb: Int = 0xFFFFFF) {
        for (angle in 0..360) {
            val dx = cos(angle * PI / 180.0)
            val dy = sin(angle * PI / 180.0)

            pixel(Vector((x + dx * r).toInt(), (y + dy * r).toInt()), rgb)
        }
    }

    // Source: https://en.wikipedia.org/wiki/Bresenham%27s_line_algorithm
    // Not sure if passing Ints here already looses precision for the z buffer coordinate?
    fun line(p0: Vector, p1: Vector, rgb: Int = 0x555555) {
        val dx = (p0.x - p1.x).absoluteValue
        val sx = if (p0.x < p1.x) 1 else -1
        val dy = -(p1.y - p0.y).absoluteValue
        val sy = if (p0.y < p1.y) 1 else -1

        // TODO(mlesniak) Interpolate z coordinate here
        var curz = p0.z
        var dz = (p1.z - p0.z).absoluteValue / dx
        var sz = if (p1.z > p0.z) 1 else -1

        var x0 = p0.x.roundToInt()
        var y0 = p0.y.roundToInt()
        val p1x = p1.x.roundToInt()
        val p1y = p1.y.roundToInt()
        var error = dx + dy

        while (true) {
            // pixel(Vector(x0, y0), rgb)
            // Hack: We use average before, always the same value.
            pixel(Vector(x0.toFloat(), y0.toFloat(), curz), rgb)
            curz = curz + dz*sz
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

    // Explanation of the algorithm at http://www.sunshine2k.de/coding/java/TriangleRasterization/TriangleRasterization.html#algo2
    private fun fillBottomFlatTriangle(p0: Vector, p1: Vector, p2: Vector, rgb: Int) {
        val invslope1 = (p1.x - p0.x) / (p1.y - p0.y)
        val invslope2 = (p2.x - p0.x) / (p2.y - p0.y)
        var curx1 = p0.x
        var curx2 = p0.x

        val zslope1 = (p1.z - p0.z) / (p1.y - p0.y)
        val zslope2 = (p2.z - p0.z) / (p2.y - p0.y)
        var curz1 = p0.z
        var curz2 = p0.z

        // Hack
        val zAverage = ((p0.z + p1.z + p2.z) / 3.0f).toInt()

        for (y in p0.y.toInt() until p1.y.toInt()) {
            // line(Vector(curx1.toInt(), y), Vector(curx2.toInt(), y), rgb)
            // Hack, not perfect.
            line(Vector(curx1, y.toFloat() , curz1), Vector(curx2, y.toFloat(), curz2), rgb)
            curx1 += invslope1
            curx2 += invslope2
            curz1 += zslope1
            curz2 += zslope2
        }
    }

    private fun fillTopFlatTriangle(p0: Vector, p1: Vector, p2: Vector, rgb: Int) {
        val invslope1 = (p2.x - p0.x) / (p2.y - p0.y)
        val invslope2 = (p2.x - p1.x) / (p2.y - p1.y)
        var curx1 = p2.x
        var curx2 = p2.x

        val zslope1 = (p1.z - p0.z) / (p1.y - p0.y)
        val zslope2 = (p2.z - p0.z) / (p2.y - p0.y)
        var curz1 = p0.z
        var curz2 = p0.z

        for (y in p2.y.toInt() downTo p0.y.toInt()) {
            // line(Vector(curx1.toInt(), y), Vector(curx2.toInt(), y), rgb)
            // Hack, not perfect.
            line(Vector(curx1, y.toFloat() , curz1), Vector(curx2, y.toFloat(), curz2), rgb)
            curx1 -= invslope1
            curx2 -= invslope2
            curz1 -= zslope1
            curz2 -= zslope2
        }
    }

    fun triangle(p0_: Vector, p1_: Vector, p2_: Vector, rgb: Int = 0x555555) {
        // Idiomatic kotlin vs if-else statements for performance?
        // Compare generated bytecode and do some micro-benchmarks?
        val (p0, p1, p2) = listOf(p0_, p1_, p2_).sortedBy { it.y }

        if (p1.y == p2.y) {
            fillBottomFlatTriangle(p0, p1, p2, rgb)
        } else if (p0.y == p1.y) {
            fillTopFlatTriangle(p0, p1, p2, rgb)
        } else {
            val middle = Vector(
                p0.x + ((p1.y - p0.y) / (p2.y - p0.y)) * (p2.x - p0.x),
                p1.y,
                p1.z,
            )
            // println("$middle, $p0, $p1, $p2")
            fillBottomFlatTriangle(p0, p1, middle, rgb)
            fillTopFlatTriangle(p1, middle, p2, rgb)
        }
    }

    fun wireTriangle(p0: Vector, p1: Vector, p2: Vector, rgb: Int = 0x555555) {
        line(p0, p1, rgb)
        line(p1, p2, rgb)
        line(p2, p0, rgb)
    }
}
