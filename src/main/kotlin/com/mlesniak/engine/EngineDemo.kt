package com.mlesniak.engine

import com.badlogic.gdx.Gdx
import com.mlesniak.engine.core.Renderer
import com.mlesniak.engine.engine.Engine
import com.mlesniak.engine.engine.Model
import com.mlesniak.engine.engine.model

class EngineDemo : Renderer {
    private var tick = 0

    private var angle = 0.0f

    private lateinit var model: Model

    override fun setup() {
        model = Model.load("models/head.obj")
    }

    override fun draw(engine: Engine) {
        engine.clear()

        val cx = engine.width() / 2
        val cy = engine.height() / 2

        engine.model(model, angle)
        angle += 1f

        if (tick % 60 == 0) {
            println(Gdx.graphics.framesPerSecond)
        }
        tick++
    }
}

