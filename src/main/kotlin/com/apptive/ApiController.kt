package com.apptive

import com.apptive.model.Generation
import com.apptive.gameservice.GameService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
open class GomController @Autowired constructor(val gameService: GameService) {
    @RequestMapping(value = "/generation/next", method = arrayOf(RequestMethod.POST))
    fun getNextGeneration(@RequestBody generation: Generation): Generation {
        return gameService.getNextGeneration(generation)
    }

}
