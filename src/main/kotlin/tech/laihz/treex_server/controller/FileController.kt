package tech.laihz.treex_server.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.laihz.treex_server.utils.R

@RestController
@RequestMapping(value = ["/api/treex"])
class FileController {
    @GetMapping("file")
    fun fileMapping(): R {
        return R.removeUser(200)
    }
}