package com.mlesniak.engine.engine

data class Point(val x: Float, val y: Float, val z: Float = 0.0f) {
    operator fun times(f: Float): Point = Point(x * f, y * f, z * f)

    operator fun plus(p: Point): Point = Point(x + p.x, y + p.y, z + p.z)

    constructor(x: Int, y: Int, z: Int = 0) : this(x.toFloat(), y.toFloat(), z.toFloat())
}
