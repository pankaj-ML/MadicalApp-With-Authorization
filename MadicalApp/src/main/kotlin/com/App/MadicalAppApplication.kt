package com.App

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.config.EnableMongoAuditing

@EnableMongoAuditing
@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class MadicalAppApplication

fun main(args: Array<String>) {
    runApplication<MadicalAppApplication>(*args)
}