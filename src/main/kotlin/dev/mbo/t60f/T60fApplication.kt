package dev.mbo.t60f

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.transaction.annotation.EnableTransactionManagement

@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@SpringBootApplication
class T60fApplication

fun main(args: Array<String>) {
    runApplication<T60fApplication>(*args)
}
