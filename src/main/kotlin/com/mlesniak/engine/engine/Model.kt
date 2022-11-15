package com.mlesniak.engine.engine

import java.nio.file.Files
import java.nio.file.Path
import java.util.regex.Pattern
import kotlin.random.Random

// This is not a good name...
class Model(
    val vertices: List<Vector>,
    val faces: List<List<Int>>,
    val normals: List<Vector>,
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

            val normals = lines
                .filter { line -> line.startsWith("vn ") }
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

            return Model(vertices, faces, normals)
        }
    }
}

fun Engine.model(model: Model, projection: Matrix, wireframe: Boolean = false) {
    // Direction of the light source. In this case,
    // going into the scene.
    val light = Vector(0f, 0f, -1f)


    for (idx in model.faces.indices) {
        val face = model.faces[idx]

        val p1 = model.vertices[face[0] - 1]
        val p2 = model.vertices[face[1] - 1]
        val p3 = model.vertices[face[2] - 1]

        val p1h = projection * p1
        val p2h = projection * p2
        val p3h = projection * p3


        val normal = (p3h - p1h).cross(p2h - p1h).normalize()
        val intensity = normal * light
        if (intensity > 0) {
            val color = (255.0 * intensity).toInt()
            val rgb = (color shl 16) or (color shl 8) or color
            if (wireframe) {
                wireTriangle(p1h, p2h, p3h, rgb)
            } else {
                triangle(p1h, p2h, p3h, rgb)
            }
        }
    }
}
