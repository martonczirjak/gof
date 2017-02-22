package com.apptive.gameservice

import com.apptive.model.Generation

/**
 * Created by apptive on 2017. 02. 02..
 */
interface GameService {

    fun getNextGeneration(generation: Generation): Generation
}
