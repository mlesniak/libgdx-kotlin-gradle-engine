package com.mlesniak.engine

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration




fun main() {
    val config = Lwjgl3ApplicationConfiguration()
    config.setForegroundFPS(60)
    config.setTitle("LibGDX Template")
    Lwjgl3Application(Core(), config)
}
