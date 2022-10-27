package com.mlesniak.engine.core

import com.mlesniak.engine.Engine

interface Renderer {
    fun update()
    fun draw(engine: Engine)
}

