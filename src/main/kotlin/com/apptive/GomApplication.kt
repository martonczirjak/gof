package com.apptive

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class GomApplication

fun main(args: Array<String>) {
    SpringApplication.run(GomApplication::class.java, *args)
}