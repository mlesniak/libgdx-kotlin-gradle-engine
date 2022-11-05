package com.mlesniak.engine.engine

import java.nio.file.Files
import java.nio.file.Path
import java.util.regex.Pattern

// This is not a good name...
class Model(
    val vertices: List<Vector>,
    val faces: List<List<Int>>,
) {
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
                .map { plist -> Vector(plist[0], plist[1], plist[2]) }

            val faces = lines
                .filter { line -> line.startsWith("f ") }
                .map { it.split(Pattern.compile(" +")) }
                .map {
                    it.subList(1, it.size).map { i -> i.split("/")[0].toInt() }
                }

            return Model(vertices, faces)
        }
    }
}

fun Engine.model(model: Model, projection: Matrix) {
    for (face in model.faces) {
        val p1 = model.vertices[face[0] - 1]
        val p2 = model.vertices[face[1] - 1]
        val p3 = model.vertices[face[2] - 1]

        val p1h = projection * p1
        val p2h = projection * p2
        val p3h = projection * p3

        triangle(p1h, p2h, p3h)
    }
}
