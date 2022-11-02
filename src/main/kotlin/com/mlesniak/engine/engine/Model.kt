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

            println("parsed model")
            return Model(vertices, faces)
        }
    }
}

// TODO(mlesniak) Model matrix should be part of the model?
fun Engine.model(model: Model, projection: Matrix) {
    for (face in model.faces) {
        for (vi in face.indices) {
            val p1 = model.vertices[face[vi] - 1]
            val p2 = model.vertices[face[(vi + 1) % face.size] - 1]

            val p1h = projection * p1
            val p2h = projection * p2

            line(p1h, p2h)
        }
    }
}
