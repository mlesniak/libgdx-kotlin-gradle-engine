package com.mlesniak.engine.core

import com.badlogic.gdx.graphics.Pixmap
import com.mlesniak.engine.core.Canvas

class PixmapCanvas(private val pixmap: Pixmap) : Canvas {
    override fun width(): Int = pixmap.width

    override fun height(): Int = pixmap.height

    override fun pixel(x: Int, y: Int, rgb: Int) {
        val rgba = rgb shl 8 or 0xFF
        pixmap.drawPixel(x, y, rgba)
    }
}
