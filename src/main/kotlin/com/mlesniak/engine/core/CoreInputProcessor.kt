package com.mlesniak.engine.core

import com.badlogic.gdx.InputProcessor

typealias KeyCode = Int

class CoreInputProcessor : InputProcessor {
    // Collect keycodes from com.badlogic.gdx.Input.Keys.* which
    // were pressed since the last clear -- usually, the last
    // frame.
    private val pressedKeys = mutableSetOf<KeyCode>()

    fun clear() {
        pressedKeys.clear()
    }

    fun pressedKeys(): Set<KeyCode> {
        return pressedKeys
    }

    override fun keyDown(keycode: Int): Boolean {
        pressedKeys += keycode
        return true
    }

    override fun keyUp(keycode: Int): Boolean {
        return true
    }

    override fun keyTyped(character: Char): Boolean {
        return true
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return true
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return true
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return true
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return true
    }
}