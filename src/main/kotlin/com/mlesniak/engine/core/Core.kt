package com.mlesniak.engine.core

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Core(private val renderer: Renderer) : ApplicationAdapter() {
    private lateinit var batch: SpriteBatch
    private lateinit var pixmap: Pixmap
    private lateinit var canvas: PixmapCanvas
    private lateinit var texture: Texture

    override fun create() {
        batch = SpriteBatch()
        pixmap = Pixmap(Gdx.graphics.width, Gdx.graphics.height, Pixmap.Format.RGB888)
        canvas = PixmapCanvas(pixmap)
    }

    override fun render() {
        // Later on we will collect all keys and pass them
        // to the update function.
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }

        renderer.update()
        renderer.draw(canvas)

        texture = Texture(pixmap)
        batch.begin()
        batch.draw(texture, 0f, 0f)
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
    }
}