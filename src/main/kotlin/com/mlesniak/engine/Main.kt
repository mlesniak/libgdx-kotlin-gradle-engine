package com.mlesniak.engine

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.mlesniak.engine.core.Core

fun main() {
    val renderer = EngineDemo()

    // This could later be abstracted as well, i.e. we want to
    // have just an Engine which wants an object implementing
    // update() and a draw().
    val config = Lwjgl3ApplicationConfiguration().apply {
        setWindowedMode(800, 600)
        setForegroundFPS(60)
        setTitle("Engine Demo")
    }
    Lwjgl3Application(Core(renderer), config)
}
