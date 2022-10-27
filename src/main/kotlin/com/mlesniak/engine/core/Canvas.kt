package com.mlesniak.engine.core

interface Canvas {
    fun width(): Int
    fun height(): Int

    // Color in RGB format.
    fun pixel(x: Int, y: Int, rgb: Int)
}
