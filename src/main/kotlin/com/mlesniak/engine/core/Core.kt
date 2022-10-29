package com.mlesniak.engine.core

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mlesniak.engine.engine.Engine
import com.mlesniak.engine.engine.Matrix
import com.mlesniak.engine.engine.Vector

class Core(private val renderer: Renderer) : ApplicationAdapter() {
    private lateinit var batch: SpriteBatch
    private lateinit var pixmap: Pixmap
    private lateinit var engine: Engine

    override fun create() {
        batch = SpriteBatch()
        pixmap = Pixmap(Gdx.graphics.width, Gdx.graphics.height, Pixmap.Format.RGB888)
        engine = Engine(Canvas(pixmap))

        val v = Matrix(
            1f, 0f, 0f,
            0f, 1f, 0f,
            0f, 0f, 1f,
        ) * Vector(1f, 2f, 3f)
        println(v)

        renderer.setup()
    }

    override fun render() { // Later on we will collect all keys and/or mouse events
        // and pass them to the update function.
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }

        renderer.update()
        renderer.draw(engine)

        val texture = Texture(pixmap)
        batch.begin()
        batch.draw(texture, 0f, 0f)
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
    }
}