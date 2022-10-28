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
        // for (y in 0..engine.height()) {
        //     for (x in 0..engine.width()) {
        //         engine.pixel(x, y, Random.nextInt())
        //     }
        // }

        engine.clear(0x000000)

        val cx = engine.width() / 2
        val cy = engine.height() / 2

        engine.circle(cx, cy, radius, 0xFF0000)
        radius += 1 * dir
        if (radius > 100 || radius < 0) {
            dir *= -1
        }

        val dx = cx + (cos(angle * 180.0/PI) * radius).toInt()
        val dy = cy + (sin(angle * 180.0/PI) * radius).toInt()
        engine.line(Point(cx, cy), Point(dx, dy), 0xFFFF00)
        angle += 0.001
    }
}