package com.mlesniak.engine.core

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mlesniak.engine.Engine

class Core(private val renderer: Renderer) : ApplicationAdapter() {
    private lateinit var batch: SpriteBatch
    private lateinit var pixmap: Pixmap
    private lateinit var canvas: Canvas
    private lateinit var texture: Texture
    private lateinit var engine: Engine

    override fun create() {
        batch = SpriteBatch()
        pixmap = Pixmap(Gdx.graphics.width, Gdx.graphics.height, Pixmap.Format.RGB888)
        canvas = Canvas(pixmap)
        engine = Engine(canvas)
    }

    override fun render() {
        // Later on we will collect all keys and/or mouse events
        // and pass them to the update function.
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }

        renderer.update()
        renderer.draw(engine)

        texture = Texture(pixmap)
        batch.begin()
        batch.draw(texture, 0f, 0f)
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
    }
}