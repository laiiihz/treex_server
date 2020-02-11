package tech.laihz.treex_server.controller

import io.undertow.server.handlers.resource.Resource
import org.slf4j.LoggerFactory
import org.springframework.core.io.UrlResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import tech.laihz.treex_server.utils.R
import java.io.File
import java.nio.file.Files
import java.util.*
import kotlin.time.milliseconds

@RestController
@RequestMapping("/api")
class TestController {
    val logger = LoggerFactory.getLogger(TestController::class.java)
    @GetMapping("test")
    fun testMapping(): String {
        return "TEST SUCCESS"
    }

    @GetMapping("checkConnection")
    fun checkConnectionMapping():R{
        return R.successResult()
    }

    @GetMapping("testFile")
    fun testFileMapping(@RequestParam("name") name:String):ResponseEntity<UrlResource>{
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(UrlResource(File("./FILESYSTEM/SHARE/${name}").normalize().toURI()))
    }


    @RequestMapping("count")
    fun getCountMapping(): String {
        var fxxk =0
        val nowDate =  Date().toString()
        Files.walk(File("../treex_app").toPath()).forEach{
            fxxk++
        }
        val okDate = Date().toString()
        return "start:${nowDate}\nend:${okDate}\ncount:${fxxk}"
    }
}