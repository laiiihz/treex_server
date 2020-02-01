package tech.laihz.treex_server.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.laihz.treex_server.service.UserService
import tech.laihz.treex_server.utils.R

@RestController
@RequestMapping("/api/treex")
class ProfileController {
    @Autowired
    lateinit var userService:UserService
    @GetMapping("profile")
    fun profileMapping(@RequestAttribute("name") name: String): R {
        val user = userService.getUserByName(name)
        return R.profileResult(user)
    }
}