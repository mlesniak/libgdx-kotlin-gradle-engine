package com.mlesniak.engine.engine

import java.nio.file.Files
import java.nio.file.Path
import java.util.regex.Pattern

// This is not a good name...
class Model(val vertices: List<Point>) {
    companion object {
        fun load(filename: String): Model {
            val lines = Files.readAllLines(Path.of(filename))
            val vertices = lines
                .filter { line -> line.startsWith("v ") }
                .map { it.split(Pattern.compile(" +")) }
                .map {
                    it.subList(1, 4)
                        .map { v -> v.toFloat() }
                }
                .map { plist -> Point(plist[0], plist[1], plist[2]) }

            return Model(vertices)
        }
    }
}

fun Engine.model(model: Model, scale: Float) {
    for (point in model.vertices) {
        val p = point * scale + Point(width() / 2, height() / 2, 0)
        pixel(p)
    }
}
