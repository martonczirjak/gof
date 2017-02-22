package com.apptive.model


import java.awt.*
import java.io.Serializable
import java.util.*

/**
 * Created by apptive on 2017. 02. 02..
 */
class Generation(var cells: MutableList<Point>, val x: Int, val y: Int) : Serializable {
    constructor(): this(ArrayList(), 0, 0) {}

    val cellCount: Int
        get() = cells.size

    fun addPoint(p: Point) {
        cells.add(p)
    }
}
