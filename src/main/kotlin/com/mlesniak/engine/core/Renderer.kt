package com.mlesniak.engine.core

import com.mlesniak.engine.engine.Engine

interface Renderer {
    fun setup() {
        // Empty by default.
    }

    // Receives a set of all pressed keys.
    fun update(pressedKeys: Set<KeyCode>) {
        // Empty by default.
    }

    fun draw(engine: Engine)
}
