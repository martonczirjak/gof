package com.apptive.Model


import java.awt.*
import java.io.Serializable

/**
 * Created by apptive on 2017. 02. 02..
 */
class Generation(var cells: MutableList<Point>, val x: Int, val y: Int) : Serializable {
    val cellCount: Int
        get() = cells.size

    fun addPoint(p: Point) {
        cells.add(p)
    }
}
