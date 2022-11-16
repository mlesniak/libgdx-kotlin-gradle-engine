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
        val modelName = "head.obj"
        model = Model.load("models/$modelName")
    }

    override fun update(pressedKeys: Set<KeyCode>) {
        when {
            Keys.ESCAPE in pressedKeys -> Gdx.app.exit()
            Keys.SPACE in pressedKeys -> paused = !paused
            Keys.W in pressedKeys -> wireframe = !wireframe
        }

        if (!paused) {
            angle += 1f
        }
    }

    // TODO(mlesniak) Non-orthogonal matrix
    override fun draw(engine: Engine) {
        engine.clear()

        val scale = 300f
        val projection =
            Matrices.translate(400f, 300f) *
                Matrices.scale(scale, scale, scale) *
                Matrices.rotateZ(180f) *
                Matrices.rotateX(30f) *
                Matrices.rotateY(angle)
        engine.model(model, projection, wireframe)

        if (tick % 60 == 0) {
            println("FPS:" + Gdx.graphics.framesPerSecond)
        }
        tick++
    }
}
