package com.mlesniak.engine

import com.mlesniak.engine.core.Renderer
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class EngineDemo : Renderer {
    private var radius = 0
    private var dir = 1
    private var angle = 0.0

    override fun update() {
        // Empty.
    }

    override fun draw(engine: Engine) {
        engine.clear()

        val cx = engine.width() / 2
        val cy = engine.height() / 2

        engine.circle(cx, cy, radius)
        radius += 1 * dir
        if (radius > 100 || radius < 0) {
            dir *= -1
        }

        val dx = cx + (cos(angle * 180.0/PI) * radius).toInt()
        val dy = cy + (sin(angle * 180.0/PI) * radius).toInt()
        engine.line(Point(cx, cy), Point(dx, dy))
        angle += 0.001
    }
}