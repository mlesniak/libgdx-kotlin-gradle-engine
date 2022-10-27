package com.mlesniak.engine

import com.mlesniak.engine.core.Canvas
import com.mlesniak.engine.core.Renderer
import kotlin.random.Random

class DemoRenderer: Renderer {
    override fun update() {
        // Empty.
    }

    override fun draw(canvas: Canvas) {
        for (y in 0..canvas.height()) {
            for (x in 0..canvas.width()) {
                canvas.pixel(x, y, Random.nextInt())
            }
        }
    }
}