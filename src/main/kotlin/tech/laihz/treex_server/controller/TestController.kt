package tech.laihz.treex_server.controller

import io.undertow.server.handlers.resource.Resource
import org.springframework.core.io.UrlResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import tech.laihz.treex_server.utils.R
import java.io.File

@RestController
@RequestMapping("/api")
class TestController {
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

}