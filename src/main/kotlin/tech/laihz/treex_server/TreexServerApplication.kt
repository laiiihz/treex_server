package tech.laihz.treex_server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import javax.annotation.PostConstruct

@SpringBootApplication
@ServletComponentScan
class TreexServerApplication

fun main(args: Array<String>) {
    runApplication<TreexServerApplication>(*args)
}
