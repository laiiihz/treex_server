package tech.laihz.treex_server.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.laihz.treex_server.utils.R

@RestController
@RequestMapping("/api/treex")
class ProfileController {

    @GetMapping("/profile")
    fun profileMapping():R {
        return R.logoutResult(code = 200,result = false)
    }
}