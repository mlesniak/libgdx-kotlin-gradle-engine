package com.mlesniak.engine.core

import com.mlesniak.engine.engine.Engine

interface Renderer {
    fun setup() {
        // Empty by default .
    }

    fun update(pressedKeys: Set<KeyCode>) {
        // Empty by default.
    }

    fun draw(engine: Engine)
}
