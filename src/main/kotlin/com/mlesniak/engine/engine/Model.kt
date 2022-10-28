package com.mlesniak.engine.engine

import java.nio.file.Files
import java.nio.file.Path

// This is not a good name...
class Model(val vertices: List<Point>) {
    companion object {
        fun load(filename: String): Model {
            // val vertices = Files
            //     .readAllLines(Path.of(filename))
            //     .filter { line -> line.startsWith("v") }
            //     .map { it.split(" ")}
            //     // .map {
            //     //     it.subList(1, 4).map { Float.}
            //     // }
            //
            //
            return Model(emptyList())
        }
    }
}