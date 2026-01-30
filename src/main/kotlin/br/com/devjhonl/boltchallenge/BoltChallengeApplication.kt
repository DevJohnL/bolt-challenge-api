package br.com.devjhonl.boltchallenge

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class BoltChallengeApplication

fun main(args: Array<String>) {
    runApplication<BoltChallengeApplication>(*args)
}
