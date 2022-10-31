package com.mlesniak.engine.engine

data class Vector(
    val x: Float,
    val y: Float,
    val z: Float = 0.0f,
    val w: Float = 1.0f,
) {
    constructor(x: Int, y: Int, z: Int = 0, w: Int = 0) : this(x.toFloat(), y.toFloat(), z.toFloat(), w.toFloat())

    operator fun times(f: Float): Vector = Vector(x * f, y * f, z * f, w * f)

    operator fun plus(v: Vector): Vector = Vector(this.x + v.x, this.y + v.y, this.z + v.z + this.w + v.w)
}

// TODO(mlesniak) remove me
fun main() {
    val m1 = arrayOf(
        arrayOf(5f, 2f, 6f, 1f),
        arrayOf(0f, 6f, 2f, 0f),
        arrayOf(3f, 8f, 1f, 4f),
        arrayOf(1f, 8f, 5f, 6f),
    )
    val m2 = arrayOf(
        arrayOf(7f, 5f, 8f, 0f),
        arrayOf(1f, 8f, 2f, 6f),
        arrayOf(9f, 4f, 3f, 8f),
        arrayOf(5f, 3f, 7f, 9f),
    )

    println((m1 * m2).debug())

    val m3 = arrayOf(
        arrayOf(1f, 0f, 2f, 0f),
        arrayOf(0f, 3f, 0f, 4f),
        arrayOf(0f, 0f, 5f, 0f),
        arrayOf(6f, 0f, 0f, 7f),
    )
    println(m3 * Vector(2, 5, 1, 8))
}

typealias Matrix = Array<Array<Float>>

operator fun Matrix.times(m: Matrix): Matrix {
    val k: Array<Array<Float>> = Array(4) { y ->
        Array(4) { x ->
            println("x=$x, y=$y")
            this[y][0] * m[0][x] + this[y][1] * m[1][x] + this[y][2] * m[2][x] + this[y][3] * m[3][x]
        }
    }
    return k
}

operator fun Matrix.times(v: Vector): Vector {
    return Vector(
        x = this[0][0] * v.x + this[0][1] * v.y + this[0][2] * v.z + this[0][3] * v.w,
        y = this[1][0] * v.x + this[1][1] * v.y + this[1][2] * v.z + this[1][3] * v.w,
        z = this[2][0] * v.x + this[2][1] * v.y + this[2][2] * v.z + this[2][3] * v.w,
        w = this[3][0] * v.x + this[3][1] * v.y + this[3][2] * v.z + this[3][3] * v.w,
    )
}

fun Array<Array<Float>>.debug(): String {
    val sb = StringBuilder()
    for (row in this) {
        sb.append(row.joinToString(" "))
        sb.append('\n')
    }
    return sb.toString()
}

fun identity(): Matrix = arrayOf(
    arrayOf(1f, 0f, 0f, 0f),
    arrayOf(0f, 1f, 0f, 0f),
    arrayOf(0f, 0f, 1f, 0f),
    arrayOf(0f, 0f, 0f, 1f),
)

