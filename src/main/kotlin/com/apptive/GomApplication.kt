package com.apptive

import com.apptive.gameservice.GameService
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@SpringBootApplication

open class GomApplication

fun main(args: Array<String>) {
    SpringApplication.run(GomApplication::class.java, *args)
    val context = AnnotationConfigApplicationContext()
    context.register(SpringConfiguration::class.java)
    context.register(GameService::class.java)
    context.refresh()
    context.close()

}