package com.skufbet.core.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration

@SpringBootApplication
class CoreApiApplication

fun main(args: Array<String>) {
	runApplication<CoreApiApplication>(*args)
}
