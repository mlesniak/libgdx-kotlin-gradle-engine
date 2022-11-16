package com.mlesniak.engine.engine

import com.mlesniak.engine.engine.Model.Companion.forObject
import java.nio.file.Files
import java.nio.file.Path
import java.util.regex.Pattern

class Model(
    val vertices: List<Vector>,
    val faces: List<List<Int>>,
) {
    companion object {
        fun load(filename: String): Model {
            val lines = Files.readAllLines(Path.of(filename))
            val vertices = lines
                .forObject("v")
                .map {
                    it.subList(1, 4)
                        .map { v -> v.toFloat() }
                }
                .map { plist -> Vector(plist[0], plist[1], plist[2]) }

            val faces = lines
                .forObject("f")
                .map {
                    it.subList(1, it.size).map { i -> i.split("/")[0].toInt() }
                }

            return Model(vertices, faces)
        }

        private fun List<String>.forObject(prefix: String) =
            this
                .filter { line -> line.startsWith("$prefix ") }
                .map { it.split(Pattern.compile(" +")) }
    }
}

fun Engine.model(model: Model, projection: Matrix, wireframe: Boolean = false) {
    // Direction of the light source. In this case,
    // going into the scene. Later on, this will be
    // a property of the engine, for now this is
    // sufficient.
    val light = Vector(0f, 0f, -1f)

    for (idx in model.faces.indices) {
        val face = model.faces[idx]

        val p1 = projection * model.vertices[face[0] - 1]
        val p2 = projection * model.vertices[face[1] - 1]
        val p3 = projection * model.vertices[face[2] - 1]

        val normal = (p3 - p1).cross(p2 - p1).normalize()
        val intensity = normal * light
        if (intensity > 0) {
            val color = (255.0 * intensity).toInt()
            val rgb = (color shl 16) or (color shl 8) or color
            if (wireframe) {
                wireTriangle(p1, p2, p3, rgb)
            } else {
                triangle(p1, p2, p3, rgb)
            }
        }
    }
}
