package com.mlesniak.engine

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.mlesniak.engine.core.Core

fun main() {
    val renderer = EngineDemo()

    val config = Lwjgl3ApplicationConfiguration().apply {
        setWindowedMode(800, 600)
        setForegroundFPS(60)
        setTitle("Engine Demo")
    }
    Lwjgl3Application(Core(renderer), config)
}
