package com.mlesniak.engine

import com.badlogic.gdx.Gdx
import com.mlesniak.engine.core.Renderer
import com.mlesniak.engine.engine.BaseMatrix
import com.mlesniak.engine.engine.Engine
import com.mlesniak.engine.engine.Model
import com.mlesniak.engine.engine.Vector
import com.mlesniak.engine.engine.model
import com.mlesniak.engine.engine.times
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.sin

class EngineDemo : Renderer {
    private var tick = 0

    private var angle = 0.0f

    private lateinit var model: Model

    override fun setup() {
        model = Model.load("models/head.obj")
    }

    // TODO(mlesniak) Non-orthogonal matrix
    override fun draw(engine: Engine) {
        engine.clear()

        val cx = engine.width() / 2
        val cy = engine.height() / 2

        // val scale = ((sin(angle * PI/180.0) * 200)).toFloat().absoluteValue + 100f
        // val projection =
        //     BaseMatrix.translate(400f, 300f) *
        //         BaseMatrix.scale(scale, scale, scale) *
        //         BaseMatrix.rotateZ(angle + 180f) *
        //         BaseMatrix.rotateX(angle) *
        //         BaseMatrix.rotateY(angle)
        // engine.model(model, projection)
        // angle += 1f

        engine.triangle(
            Vector(tick % 800, 300),
            Vector(500, tick % 600),
            Vector(450, (tick % 600) / 2),
        )

        if (tick % 60 == 0) {
            println("FPS:" + Gdx.graphics.framesPerSecond)
        }
        tick++
    }
}
