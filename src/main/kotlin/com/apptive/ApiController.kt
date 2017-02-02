package com.apptive

import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@Service
open class GomController {
    @RequestMapping(value = "/", method = arrayOf(RequestMethod.GET))
    fun index(): String {
        return "apa"
    }
}