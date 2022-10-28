package com.mlesniak.engine.core

import com.mlesniak.engine.engine.Engine

interface Renderer {
    fun setup() {
        // Empty by default .
    }

    fun update() {
        // Empty by default.
    }

    fun draw(engine: Engine)
}

