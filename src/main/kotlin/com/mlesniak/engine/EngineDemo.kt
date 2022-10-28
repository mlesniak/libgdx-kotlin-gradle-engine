package com.mlesniak.engine

import com.badlogic.gdx.Gdx
import com.mlesniak.engine.core.Renderer
import com.mlesniak.engine.engine.Engine
import com.mlesniak.engine.engine.Model
import com.mlesniak.engine.engine.Point
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class EngineDemo : Renderer {
    private var tick = 0;

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
        //
        // engine.circle(cx, cy, radius)
        // radius += 1 * dir
        // if (radius > 100 || radius < 0) {
        //     dir *= -1
        // }
        //
        // val dx = cx + (cos(angle * 180.0/PI) * radius).toInt()
        // val dy = cy + (sin(angle * 180.0/PI) * radius).toInt()
        // engine.line(Point(cx, cy), Point(dx, dy))
        // angle += 0.001

        // TODO(mlesniak) Use extension function
        val scale = 20.0f
        for (point in model.vertices) {
            val p = point * scale + Point(cx, cy, 0)
            engine.pixel(p)
        }

        if (tick % 30 == 0) {
            println(Gdx.graphics.framesPerSecond)
        }
        tick++
    }

    fun draw(model: Model, scale: Float) {
        // Ignore z-coordinate for now
    }

}

