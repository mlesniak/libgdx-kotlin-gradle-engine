package com.mlesniak.engine

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

// TODO(mlesniak) Keyboard handling to close the window
// TODO(mlesniak) Single Pixel drawing

// TODO(mlesniak) For later...:
// TODO(mlesniak) Line drawing via Bresenham
// TODO(mlesniak) Basic 3D Vector classes
// TODO(mlesniak) Basic Matrix classes
// TODO(mlesniak) Simple projection and rotations
fun main() {
    val config = Lwjgl3ApplicationConfiguration()
    config.setForegroundFPS(60)
    config.setTitle("LibGDX Template")
    Lwjgl3Application(Core(), config)
}
