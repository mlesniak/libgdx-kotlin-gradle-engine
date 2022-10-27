package com.mlesniak.engine.core

import com.badlogic.gdx.graphics.Pixmap

class Canvas(private val pixmap: Pixmap) {
    fun width(): Int = pixmap.width

    fun height(): Int = pixmap.height

    fun pixel(x: Int, y: Int, rgb: Int) {
        val rgba = rgb shl 8 or 0xFF
        pixmap.drawPixel(x, y, rgba)
    }
}
