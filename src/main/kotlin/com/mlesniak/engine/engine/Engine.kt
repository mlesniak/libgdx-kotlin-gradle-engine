package com.mlesniak.engine.engine

import com.mlesniak.engine.core.Canvas
import java.util.Arrays
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

// The only function we need is to draw single pixels
// on a canvas. Everything else will be implemented
// by our own functions and algorithms.
@Suppress("MemberVisibilityCanBePrivate")
class Engine(private val canvas: Canvas) {
    private var zbuffer: FloatArray = FloatArray(canvas.height * canvas.width) { -Float.MAX_VALUE }

    val height: Int
        get() = canvas.height

    val width: Int
        get() = canvas.width

    fun pixel(p: Vector, rgb: Int = 0xFFFFFF) {
        val py = p.y.roundToInt()
        val px = p.x.roundToInt()
        if (px < 0 || px > width || py < 0 || py > height) {
            return
        }

        val curz = zbuffer[py * canvas.height + px]
        if (p.z > curz) {
            canvas.pixel(px, py, rgb)
            zbuffer[py * canvas.height + px] = p.z
        }
    }

    fun clear(rgb: Int = 0xCCCCCC) {
        canvas.clear(rgb)
        Arrays.fill(zbuffer, -Float.MAX_VALUE)
    }

    fun circle(x: Int, y: Int, r: Int, rgb: Int = 0xFFFFFF) {
        for (angle in 0..360) {
            val dx = cos(angle * PI / 180.0)
            val dy = sin(angle * PI / 180.0)

            pixel(Vector((x + dx * r).roundToInt(), (y + dy * r).roundToInt()), rgb)
        }
    }

    // Source: https://en.wikipedia.org/wiki/Bresenham%27s_line_algorithm
    fun line(p0: Vector, p1: Vector, rgb: Int = 0x555555) {
        val dx = (p0.x - p1.x).absoluteValue
        val sx = if (p0.x < p1.x) 1 else -1
        val dy = -(p1.y - p0.y).absoluteValue
        val sy = if (p0.y < p1.y) 1 else -1

        var curz = p0.z
        var dz = (p1.z - p0.z).absoluteValue / dx
        var sz = if (p1.z > p0.z) 1 else -1

        var x0 = p0.x.roundToInt()
        var y0 = p0.y.roundToInt()
        val p1x = p1.x.roundToInt()
        val p1y = p1.y.roundToInt()
        var error = dx + dy

        while (true) {
            pixel(Vector(x0.toFloat(), y0.toFloat(), curz), rgb)
            curz = curz + dz * sz
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

    // https://www.gabrielgambetta.com/computer-graphics-from-scratch/06-lines.html
    //
    // Based on d = f(i) in R2 with d as the dependent and i as the independent
    // variable; also f is linear.
    fun interpolate(i0: Float, d0: Float, i1: Float, d1: Float): FloatArray {
        val s = (d1 - d0) / (i1 - i0)
        var d = d0
        val arr = FloatArray((i1.roundToInt()) - i0.roundToInt() + 1)

        for (i in 0..((i1.roundToInt()) - i0.roundToInt())) {
            arr[i] = d
            d += s
        }

        return arr
    }

    // https://www.gabrielgambetta.com/computer-graphics-from-scratch/07-filled-triangles.html
    //
    // We could presort the faces while loading them, but this wouldn't work for dynamic models.
    // Still, might be an interesting idea to follow up to cache the sorted vectors even for
    // dynamic scenarios.
    fun triangle(p0_: Vector, p1_: Vector, p2_: Vector, rgb: Int = 0x555555) {
        val (p0, p1, p2) = sort(p0_, p1_, p2_)

        val minX = minOf(p0.x, minOf(p1.x, p2.x)).toInt()
        val maxX = maxOf(p0.x, maxOf(p1.x, p2.x)).toInt()

        val x01 = interpolate(p0.y, p0.x, p1.y, p1.x)
        val x12 = interpolate(p1.y, p1.x, p2.y, p2.x)
        val x02 = interpolate(p0.y, p0.x, p2.y, p2.x)
        val x012 = x01 + x12

        // Special case: if the distance between the largest
        // and smallest point on the y-axis is less than 1,
        // interpolate will return only a single point, thus
        // not allowing to draw an actual triangle. We can
        // ignore these very small triangles anyway.
        if (x02.size < 2) {
            return
        }

        val z01 = interpolate(p0.y, p0.z, p1.y, p1.z)
        val z12 = interpolate(p1.y, p1.z, p2.y, p2.z)
        val z02 = interpolate(p0.y, p0.z, p2.y, p2.z)
        val z012 = z01 + z12
        val zLeft = z02
        val zRight = z012

        // We don't know which side is the lower (left) one.
        // Since the list is sorted by the interpolation, we
        // can simply check the middle element to figure out
        // the smaller list.
        val m = x012.size / 2
        var xLeft: FloatArray
        var xRight: FloatArray
        if (x02[m] < x012[m]) {
            xLeft = x02
            xRight = x012
        } else {
            xLeft = x012
            xRight = x02
        }

        val p0y = p0.y.roundToInt()
        for (y in p0y..p2.y.roundToInt()) {
            val zinc = zLeft[y - p0y] / zRight[y - p0y]
            var z = zLeft[y - p0y]
            for (x in xLeft[y - p0y].roundToInt()..xRight[y - p0y].roundToInt()) {
                // Interpolation of very small triangles can lead to very steep
                // slopes and thus jumps which are outside the initlal coordinates.
                var boundedX = x.coerceIn(minX, maxX)
                pixel(Vector(boundedX.toFloat(), y.toFloat(), z), rgb)
                z += zinc
            }
        }
    }

    private inline fun sort(p0_: Vector, p1_: Vector, p2_: Vector): Triple<Vector, Vector, Vector> {
        lateinit var p0: Vector
        lateinit var p1: Vector
        lateinit var p2: Vector
        if (p0_.y > p1_.y) {
            p1 = p0_
            p0 = p1_
        } else {
            p1 = p1_
            p0 = p0_
        }
        if (p1.y > p2_.y) {
            p2 = p1
            if (p0.y > p2_.y) {
                p1 = p0
                p0 = p2_
            } else {
                p1 = p2_
            }
        } else {
            p2 = p2_
        }

        return Triple(p0, p1, p2)
    }

    fun wireTriangle(p0: Vector, p1: Vector, p2: Vector, rgb: Int = 0x555555) {
        line(p0, p1, rgb)
        line(p1, p2, rgb)
        line(p2, p0, rgb)
    }
}
