package com.apptive.gameservice

import com.apptive.model.Generation
import org.springframework.stereotype.Service

import java.awt.*
import java.util.ArrayList

/**
 * Created by apptive on 2017. 02. 02..
 */
@Service
class SimpleGameService : GameService {
    @Throws(IllegalArgumentException::class)
    fun getNextGeneration(firstGenerations: Array<CharArray>): Array<CharArray> {

        //TODO some stricter validation would be nice
        if (firstGenerations.size > 90 || firstGenerations[0].size > 180) {
            throw IllegalArgumentException()
        }
        val instance = firstGenerations
        val babies = ArrayList<Point>()

        var deadCells = ArrayList<Point>()

        for (i in firstGenerations.indices) {
            for (j in 0..firstGenerations[i].size - 1) {
                if (getNeighourCount(instance, i, j) < 2 || getNeighourCount(instance, i, j) > 3) {
                    deadCells.add(Point(i, j))
                } else {
                    babies.add(Point(i, j))
                }

            }
        }

        for (i in babies.indices) {
            val x = babies[i].x
            val y = babies[i].y
            firstGenerations[x][y] = 'x'
        }
        for (i in deadCells.indices) {
            val x = babies[i].x
            val y = babies[i].y
            firstGenerations[x][y] = ' '
        }

        return firstGenerations
    }

    private fun getNeighourCount(instance: Array<CharArray>, i: Int, j: Int): Int {
        var neighbors = 0

        if (i > 0 && j > 0 && i < instance.size && j < instance[i].size) { //inside the square
            for (k in i - 1..i + 1 - 1) {
                for (l in j - 1..j + 1 - 1) {
                    if (instance[k][l] == 'x') {
                        neighbors++
                    }
                }
            }
        } else if (i == 0 && j > 0 && j < instance[i].size) { // first line without corners
            for (k in i..i + 1 - 1) {
                for (l in j - 1..j + 1 - 1) {
                    if (instance[k][l] == 'x') {
                        neighbors++
                    }
                }
            }

        } else if (i == instance.size && j > 0 && j < instance[i].size) { // last line without corners
            for (k in i - 1..i - 1) {
                for (l in j - 1..j + 1 - 1) {
                    if (instance[k][l] == 'x') {
                        neighbors++
                    }
                }
            }
        } else if (j == 0 && i > 0 && i < instance.size) { // first column without corners
            for (k in i - 1..i + 1 - 1) {
                for (l in j..j + 1 - 1) {
                    if (instance[k][l] == 'x') {
                        neighbors++
                    }
                }
            }


        } else if (j == instance[i].size && i > 0 && i < instance.size) { // last column without corners
            for (k in i - 1..i + 1 - 1) {
                for (l in j - 1..j - 1) {
                    if (instance[k][l] == 'x') {
                        neighbors++
                    }
                }
            }

        }


        if (i == 0 && j == 0) {     //left&up corner
            for (k in i..i + 1 - 1) {
                for (l in j..j + 1 - 1) {
                    if (instance[k][l] == 'x') {
                        neighbors++
                    }
                }
            }
        }

        if (i == 0 && j == instance[i].size) { //right&up corner
            for (k in i..i + 1 - 1) {
                for (l in j - 1..j - 1) {
                    if (instance[k][l] == 'x') {
                        neighbors++
                    }
                }
            }
        }

        if (i == instance.size && j == 0) { //down&left corner
            for (k in i - 1..i - 1) {
                for (l in j..j + 1 - 1) {
                    if (instance[k][l] == 'x') {
                        neighbors++
                    }
                }
            }
        }

        if (i == instance.size && j == instance[i].size) { //right&down corner
            for (k in i - 1..i - 1) {
                for (l in j - 1..j - 1) {
                    if (instance[k][l] == 'x') {
                        neighbors++
                    }
                }
            }
        }

        return neighbors


    }

    @Throws(IllegalArgumentException::class)
    override fun getNextGeneration(generation: Generation): Generation {

        val a = Array(generation.x) { CharArray(generation.y) }
        var b = Array(generation.x) { CharArray(generation.y) }

        val cells = generation.cells
        val newCells = ArrayList<Point>()


        for (i in cells.indices) {     // TODO refactor(getNextGeneration should  handle these for cycles
            a[cells[i].x][cells[i].y] = 'x'
        }
        for (i in 0..generation.x - 1) {
            for (j in 0..generation.y - 1) {
                if (a[i][j] != 'x') {
                    a[i][j] = ' '
                }
            }
        }
        b = getNextGeneration(a)
        for (i in 0..generation.x - 1) {
            for (j in 0..generation.y - 1) {
                if (b[i][j] == 'x') {
                    newCells.add(Point(i, j))
                }
            }
        }
        return Generation(newCells, generation.x, generation.y)
    }
}
