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
    var paused = false

    private var zbuffer = Array(canvas.height * canvas.width) { -Float.MAX_VALUE }

    var projectionMatrix: Matrix =
        Matrices.scale(1.0f, 1.0f, 1.0f) *
            Matrices.translate((canvas.width / 2).toFloat(), (canvas.height / 2).toFloat())

    fun height() = canvas.height
    fun width() = canvas.width

    fun pixel(p: Vector, rgb: Int = 0xFFFFFF) {
        val py = p.y.roundToInt()
        val px = p.x.roundToInt()
        if (px < 0 || px > canvas.width || py < 0 || py > canvas.height) {
            return
        }

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

            pixel(Vector((x + dx * r).roundToInt(), (y + dy * r).roundToInt()), rgb)
        }
    }

    // Source: https://en.wikipedia.org/wiki/Bresenham%27s_line_algorithm
    // Not sure if passing Ints here already looses precision for the z buffer coordinate?
    fun line(p0: Vector, p1: Vector, rgb_: Int = 0x555555) {
        var rgb = rgb_
        if (p0.x > 700 || p1.x > 700) {
            // println("$p0 $p1")
            rgb = 0xFF0000
        }

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
            // pixel(Vector(x0, y0), rgb)
            // Hack: We use average before, always the same value.
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

    // Explanation of the algorithm at http://www.sunshine2k.de/coding/java/TriangleRasterization/TriangleRasterization.html#algo2
    private fun fillBottomFlatTriangle(p0: Vector, p1: Vector, p2: Vector, rgb: Int) {
        // println("BOTTOM $p0 $p1 $p2")
        // Let's not draw triangles where the y delta is less than 1
        // if ((p1.y - p0.y).absoluteValue < 1) {
        //     return
        // }

        val invslope1 = (p1.x - p0.x) / (p1.y - p0.y)
        val invslope2 = (p2.x - p0.x) / (p2.y - p0.y)
        var curx1 = p0.x
        var curx2 = p0.x

        val zslope1 = (p1.z - p0.z) / (p1.y - p0.y)
        val zslope2 = (p2.z - p0.z) / (p2.y - p0.y)
        var curz1 = p0.z
        var curz2 = p0.z

        for (y in p0.y.roundToInt()..p1.y.roundToInt()) {
            if (y == p1.y.roundToInt()) {
                // println("Fixed")
                curx1 = p1.x
                curx2 = p2.x
            }

            // line(Vector(curx1.roundToInt(), y), Vector(curx2.roundToInt(), y), rgb)
            // Hack, not perfect.
            if (curx1 < curx2) {
                // println("$y $curx1 $curx2")
                for (x in curx1.roundToInt()..curx2.roundToInt()) {
                    pixel(Vector(x.toFloat(), y.toFloat(), curz1), rgb)
                }
            } else {
                // println("$y $curx2 $curx1")
                for (x in curx2.roundToInt()..curx1.roundToInt()) {
                    pixel(Vector(x.toFloat(), y.toFloat(), curz1), rgb)
                }
            }

            // line(Vector(curx1, y.toFloat(), curz1), Vector(curx2, y.toFloat(), curz2), rgb)

            // line(Vector(curx1, y.toFloat() , curz1), Vector(curx2, y.toFloat(), curz2), 0xFF0000)
            curx1 += invslope1
            curx2 += invslope2
            curz1 += zslope1
            curz2 += zslope2
        }
    }

    private fun fillTopFlatTriangle(p0: Vector, p1: Vector, p2: Vector, rgb: Int) {
        // Let's not draw triangles where the y delta is less than 1
        // println("$p0 $p1 $p2")
        if ((p2.y - p0.y).absoluteValue < 1) {
            return
        }

        val invslope1 = ((p2.x - p0.x) / (p2.y - p0.y))
        val invslope2 = (p2.x - p1.x) / (p2.y - p1.y)
        var curx1 = p2.x
        var curx2 = p2.x

        val zslope1 = (p2.z - p0.z) / (p2.y - p0.y)
        val zslope2 = (p2.z - p1.z) / (p2.y - p1.y)
        var curz1 = p2.z
        var curz2 = p2.z

        // println(p0.y)
        // println(p0.y.roundToInt())
        // println()
        for (y in p2.y.roundToInt() downTo p0.y.roundToInt()) {
            // println("$curx1 $curx2")
            // if (y == 314) {
            //     System.exit(1)
            // }
            //
            // Prevent rounding issues
            if (y == p0.y.roundToInt()) {
                // println("Fixed")
                curx1 = p0.x
                curx2 = p1.x
            }

            // println("$y: $curx1..$curx2")
            if (curx1 < curx2) {
                for (x in curx1.roundToInt()..curx2.roundToInt()) {
                    pixel(Vector(x.toFloat(), y.toFloat(), curz1), rgb)
                }
            } else {
                for (x in curx2.roundToInt()..curx1.roundToInt()) {
                    pixel(Vector(x.toFloat(), y.toFloat(), curz1), rgb)
                }
            }

            // line(Vector(curx1, y.toFloat(), curz1), Vector(curx2, y.toFloat(), curz2), rgb)
            // line(Vector(curx1, y.toFloat() , curz1), Vector(curx2, y.toFloat(), curz2), 0x00FF00)
            curx1 -= invslope1
            curx2 -= invslope2
            curz1 -= zslope1
            curz2 -= zslope2
        }

        // System.exit(0)
    }

    var c = 0
    var colors = arrayOf(
        // 0xFF0000,
        // 0x00FF00,
        0x0000FF,
        0x00FFFF,
    )

    /*
    Interpolate (i0, d0, i1, d1) {
    values = []
    a = (d1 - d0) / (i1 - i0)
    d = d0
    for i = i0 to i1 {
        values.append(d)
        d = d + a
    }
    return values
     */

    // https://www.gabrielgambetta.com/computer-graphics-from-scratch/06-lines.html
    // Based on d = f(i) in R2 with d as the dependent and i as the independent
    // variable; also f is linear.
    fun interpolate(i0: Float, d0: Float, i1: Float, d1: Float): List<Float> {
        val arr = mutableListOf<Float>()
        val s = (d1 - d0) / (i1 - i0)
        var d = d0
        for (i in i0.roundToInt()..i1.roundToInt()) {
            arr.add(d)
            d += s
        }

        return arr
    }

    // https://www.gabrielgambetta.com/computer-graphics-from-scratch/07-filled-triangles.html
    fun triangle(p0_: Vector, p1_: Vector, p2_: Vector, rgb: Int = 0x555555) {
        // TODO(mlesniak) Don'// TODO(mlesniak) draw very small triangles


        // Idiomatic kotlin vs if-else statements for performance?
        // Compare generated bytecode and do some micro-benchmarks?
        val (p0, p1, p2) = listOf(p0_, p1_, p2_).sortedBy { it.y }

        val minX = minOf(p0.x, minOf(p1.x, p2.x)).toInt()
        val maxX = maxOf(p0.x, maxOf(p1.x, p2.x)).toInt()

        val x01 = interpolate(p0.y, p0.x, p1.y, p1.x)
        val x12 = interpolate(p1.y, p1.x, p2.y, p2.x)
        val x02 = interpolate(p0.y, p0.x, p2.y, p2.x)
        val x012 = x01 + x12

        val z01 = interpolate(p0.y, p0.z, p1.y, p1.z)
        val z12 = interpolate(p1.y, p1.z, p2.y, p2.z)
        val z02 = interpolate(p0.y, p0.z, p2.y, p2.z)
        val z012 = z01 + z12
        // TODO(mlesniak) correct?
        val zLeft = z02
        val zRight = z012

        if (x02.size < 2) {
            return
        }

        val m = x012.size / 2
        var xLeft: List<Float>
        var xRight: List<Float>
        if (x02[m] < x012[m]) {
            xLeft = x02
            xRight = x012
        } else {
            xLeft = x012
            xRight = x02
        }

        val p0y = p0.y.roundToInt()
        for (y in p0y..p2.y.roundToInt()) {
            val zinc = zLeft[y-p0y]/zRight[y-p0y]
            var z = zLeft[y-p0y]

            for (x in xLeft[y - p0y].roundToInt()..xRight[y-p0y].roundToInt()) {
                var xx = x
                if (xx < minX) {
                   xx = minX
                } else if (xx > maxX) {
                    xx = maxX
                }

                pixel(Vector(xx.toFloat(), y.toFloat(), z), rgb)
                z += zinc
            }
        }
    }

    fun _triangle(p0_: Vector, p1_: Vector, p2_: Vector, rgb: Int = 0x555555) {
        // println("--- TRIANGLE ---")
        // Idiomatic kotlin vs if-else statements for performance?
        // Compare generated bytecode and do some micro-benchmarks?
        val (p0, p1, p2) = listOf(p0_, p1_, p2_).sortedBy { it.y }
        // println("p0 $p0")
        // println("p1 $p1")
        // println("p2 $p2")

        if (p1.y == p2.y) {
            // fillBottomFlatTriangle(p0, p1, p2, rgb)
        } else if (p0.y == p1.y) {
            // fillTopFlatTriangle(p0, p1, p2, rgb)
        } else {
            val middle = Vector(
                p0.x + ((p1.y - p0.y) / (p2.y - p0.y)) * (p2.x - p0.x),
                p1.y,
                p1.z,
            )
            // println("mi $middle")
            // var rr = colors[c++ % colors.size]
            // println("TOP p0 p1 mi")
            // fillBottomFlatTriangle(p0, p1, middle, rr)
            // rr = colors[c++ % colors.size]
            // fillTopFlatTriangle(p1, middle, p2, rr)

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
