package com.mlesniak.engine

import com.mlesniak.engine.core.Renderer

class EngineDemo : Renderer {
    private var radius = 0
    private var dir = 1
    private var tick = 0

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

        engine.circle(engine.width() / 2, engine.height() / 2, radius, 0xFF0000)
        radius += 1 * dir
        if (radius > 100 || radius < 0) {
            dir *= -1
        }

        tick++
    }
}