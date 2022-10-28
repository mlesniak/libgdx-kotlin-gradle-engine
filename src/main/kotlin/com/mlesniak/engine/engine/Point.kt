package com.mlesniak.engine.engine

data class Point(val x: Float, val y: Float, val z: Float = 0.0f) {
    constructor(x: Int, y: Int, z: Int = 0) : this(x.toFloat(), y.toFloat(), z.toFloat())
}
