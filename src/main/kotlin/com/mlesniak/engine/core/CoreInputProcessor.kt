package com.mlesniak.engine.core

import com.badlogic.gdx.InputProcessor

typealias KeyCode = Int

class CoreInputProcessor : InputProcessor {
    // Minimal time before a key can be released
    private val minPressedDuration = 250

    // Collect keycodes from com.badlogic.gdx.Input.Keys.*
    private val pressedKeys = mutableMapOf<KeyCode, Long>()

    fun pressedKeys(): Set<KeyCode> {
        return pressedKeys.keys
    }

    // TODO(mlesniak) correctl handle key events
    override fun keyDown(keycode: Int): Boolean {
        // if (keycode in pressedKeys) {
        //     return true
        // }
        pressedKeys[keycode] = System.currentTimeMillis()
        return true
    }

    override fun keyUp(keycode: Int): Boolean {
        // val lastPressed = pressedKeys[keycode]!!
        // if (System.currentTimeMillis() - lastPressed > minPressedDuration) {
            pressedKeys.remove(keycode)
        // }

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