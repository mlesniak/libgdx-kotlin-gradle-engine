package com.mlesniak.engine

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.mlesniak.engine.core.KeyCode
import com.mlesniak.engine.core.Renderer
import com.mlesniak.engine.engine.Matrices
import com.mlesniak.engine.engine.Engine
import com.mlesniak.engine.engine.Model
import com.mlesniak.engine.engine.model
import com.mlesniak.engine.engine.times
import kotlin.math.sin

class EngineDemo : Renderer {
    private var paused = false
    private var wireframe = false

    private var tick = 0
    private var angle = 0.0f
    private lateinit var model: Model

    override fun setup() {
        val modelName = "alfa147.obj"
        model = Model.load("models/$modelName")
    }

    override fun update(pressedKeys: Set<KeyCode>) {
        when {
            Keys.ESCAPE in pressedKeys -> Gdx.app.exit()
            Keys.SPACE in pressedKeys -> paused = !paused
            Keys.W in pressedKeys -> wireframe = !wireframe
        }

        if (!paused) {
            angle += 2f
        }
    }

    override fun draw(engine: Engine) {
        engine.clear()

        val scale = 3f
        val projection =
            Matrices.translate(engine.width / 2f, engine.height / 2f + 100f) *
                Matrices.scale(scale, scale, scale) *
                Matrices.rotateZ(0f) *
                Matrices.rotateY(angle) *
                Matrices.rotateX(70f)
        engine.model(model, projection, wireframe)

        if (tick % 60 == 0) {
            println("FPS:" + Gdx.graphics.framesPerSecond)
        }
        tick++
    }
}
