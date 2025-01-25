package dev.mbo.t60f

import org.springframework.boot.fromApplication
import org.springframework.boot.with

fun main(args: Array<String>) {
    fromApplication<T60fApplication>().with(TestcontainersConfiguration::class).run(*args)
}
