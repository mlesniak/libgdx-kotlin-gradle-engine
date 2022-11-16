package com.mlesniak.engine.engine

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class Vector(
    val x: Float,
    val y: Float,
    val z: Float = 0.0f,
    val w: Float = 1.0f,
) {
    constructor(x: Int, y: Int, z: Int = 0, w: Int = 1) : this(x.toFloat(), y.toFloat(), z.toFloat(), w.toFloat())

    operator fun times(f: Float): Vector = Vector(x * f, y * f, z * f, w * f)

    // Dot product.
    operator fun times(v: Vector): Float = x * v.x + y * v.y + z * v.z

    // Cross product
    // Source: https://www.khronos.org/opengl/wiki/Calculating_a_Surface_Normal
    fun cross(v: Vector): Vector {
        return Vector(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x)
    }

    operator fun plus(v: Vector): Vector = Vector(x + v.x, y + v.y, z + v.z + w + v.w)

    fun normalize(): Vector {
        return this * (1.0f / length())
    }

    private fun length(): Float {
        return sqrt(x * x + y * y + z * z)
    }

    operator fun minus(v: Vector): Vector = Vector(x - v.x, y - v.y, z - v.z)
}

typealias Matrix = Array<Array<Float>>

// Is an unrolled loop faster?
operator fun Matrix.times(m: Matrix): Matrix =
    Array(4) { y ->
        Array(4) { x ->
            this[y][0] * m[0][x] + this[y][1] * m[1][x] + this[y][2] * m[2][x] + this[y][3] * m[3][x]
        }
    }

operator fun Matrix.times(v: Vector): Vector {
    return Vector(
        x = this[0][0] * v.x + this[0][1] * v.y + this[0][2] * v.z + this[0][3] * v.w,
        y = this[1][0] * v.x + this[1][1] * v.y + this[1][2] * v.z + this[1][3] * v.w,
        z = this[2][0] * v.x + this[2][1] * v.y + this[2][2] * v.z + this[2][3] * v.w,
        w = this[3][0] * v.x + this[3][1] * v.y + this[3][2] * v.z + this[3][3] * v.w,
    )
}

fun Matrix.debug(): String {
    val sb = StringBuilder()
    this.forEach { row ->
        sb.append(row.joinToString(" "))
        sb.append('\n')
    }
    return sb.toString()
}

object Matrices {
    fun identity(): Matrix =
        arrayOf(
            arrayOf(1f, 0f, 0f, 0f),
            arrayOf(0f, 1f, 0f, 0f),
            arrayOf(0f, 0f, 1f, 0f),
            arrayOf(0f, 0f, 0f, 1f),
        )

    fun translate(dx: Float, dy: Float, dz: Float = 0.0f): Matrix =
        arrayOf(
            arrayOf(1f, 0f, 0f, dx),
            arrayOf(0f, 1f, 0f, dy),
            arrayOf(0f, 0f, 1f, dz),
            arrayOf(0f, 0f, 0f, 1f),
        )

    fun scale(sx: Float, sy: Float, sz: Float): Matrix =
        arrayOf(
            arrayOf(sx, 0f, 0f, 0f),
            arrayOf(0f, sy, 0f, 0f),
            arrayOf(0f, 0f, sz, 0f),
            arrayOf(0f, 0f, 0f, 1f),
        )

    fun rotateY(deg: Float): Matrix {
        val rad = (deg * PI / 180f)
        return arrayOf(
            arrayOf(cos(rad).toFloat(), 0f, sin(rad).toFloat(), 0f),
            arrayOf(0f, 1f, 0f, 0f),
            arrayOf(-sin(rad).toFloat(), 0f, cos(rad).toFloat(), 0f),
            arrayOf(0f, 0f, 0f, 1f),
        )
    }

    fun rotateZ(deg: Float): Matrix {
        val rad = (deg * PI / 180f)
        return arrayOf(
            arrayOf(cos(rad).toFloat(), -sin(rad).toFloat(), 0f, 0f),
            arrayOf(sin(rad).toFloat(), cos(rad).toFloat(), 0f, 0f),
            arrayOf(0f, 0f, 1f, 0f),
            arrayOf(0f, 0f, 0f, 1f),
        )
    }

    fun rotateX(deg: Float): Matrix {
        val rad = (deg * PI / 180f)
        return arrayOf(
            arrayOf(1f, 0f, 0f, 0f),
            arrayOf(0f, cos(rad).toFloat(), -sin(rad).toFloat(), 0f),
            arrayOf(0f, sin(rad).toFloat(), cos(rad).toFloat(), 0f),
            arrayOf(0f, 0f, 0f, 1f),
        )
    }
}
