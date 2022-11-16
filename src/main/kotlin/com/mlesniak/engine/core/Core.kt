package com.mlesniak.engine.core

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mlesniak.engine.engine.Engine

class Core(private val renderer: Renderer) : ApplicationAdapter() {
    private lateinit var batch: SpriteBatch
    private lateinit var pixmap: Pixmap
    private lateinit var engine: Engine
    private lateinit var inputProcessor: CoreInputProcessor

    override fun create() {
        batch = SpriteBatch()
        pixmap = Pixmap(Gdx.graphics.width, Gdx.graphics.height, Pixmap.Format.RGB888)
        engine = Engine(Canvas(pixmap))

        renderer.setup()
        inputProcessor = CoreInputProcessor()
        Gdx.input.inputProcessor = inputProcessor
    }

    override fun render() {
        renderer.update(inputProcessor.pressedKeys())
        inputProcessor.clear()
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
