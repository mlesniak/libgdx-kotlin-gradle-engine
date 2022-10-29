package com.mlesniak.engine.engine

data class Vector(val x: Float, val y: Float, val z: Float = 0.0f) {
    fun scalar(f: Float): Vector = Vector(x * f, y * f, z * f)

    operator fun plus(p: Vector): Vector = Vector(x + p.x, y + p.y, z + p.z)

    constructor(x: Int, y: Int, z: Int = 0) : this(x.toFloat(), y.toFloat(), z.toFloat())
}

data class Matrix(
    val v0: Float,
    val v1: Float,
    val v2: Float,
    val v3: Float,
    val v4: Float,
    val v5: Float,
    val v6: Float,
    val v7: Float,
    val v8: Float,
) {
    operator fun times(v: Vector): Vector {
        return Vector(
            x = v0 * v.x + v1 * v.y + v2 * v.z,
            y = v3 * v.x + v4 * v.y + v5 * v.z,
            z = v6 * v.x + v7 * v.y + v8 * v.z,
        )
    }

    companion object {
        fun foo(): Matrix {
            return Matrix(
                1f, 2f, 3f,
                4f, 5f, 6f,
                1f, 4f, 5f,
            )
        }
    }
}