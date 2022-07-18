package eu.ecodex.ccdm

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CcdmApplication

fun main(args: Array<String>) {
	runApplication<CcdmApplication>(*args)
}
