package com.mlesniak.engine

import com.badlogic.gdx.Gdx
import com.mlesniak.engine.core.Renderer
import com.mlesniak.engine.engine.Engine
import com.mlesniak.engine.engine.Model
import com.mlesniak.engine.engine.Vector
import com.mlesniak.engine.engine.model

class EngineDemo : Renderer {
    private var tick = 0

    private var radius = 0
    private var dir = 1
    private var angle = 0.0

    private lateinit var model: Model

    override fun setup() {
        model = Model.load("models/lamp.obj")
        // model = Model.load("models/teapot.obj")
        // model = Model.load("models/skyscraper.obj")
    }

    override fun draw(engine: Engine) {
        engine.clear()

        val cx = engine.width() / 2
        val cy = engine.height() / 2

        engine.triangle(
            Vector(400, 200),
            Vector(410, 410),
            Vector(270, 90),
            0xFF0000,
        )

        if (tick % 60 == 0) {
            println(Gdx.graphics.framesPerSecond)
        }
        tick++
    }
}

