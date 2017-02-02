package com.apptive

import com.apptive.gameservice.GameService
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

/**
 * Created by apptive on 2017. 02. 02..
 */

@Configuration
@ComponentScan(basePackageClasses = arrayOf(GameService::class))
open class SpringConfiguration{

}