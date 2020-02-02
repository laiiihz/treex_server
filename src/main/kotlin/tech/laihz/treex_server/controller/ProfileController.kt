package tech.laihz.treex_server.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import tech.laihz.treex_server.service.UserService
import tech.laihz.treex_server.utils.R

@RestController
@RequestMapping("/api/treex")
class ProfileController {
    @Autowired
    lateinit var userService:UserService

    /**
     * @api {get} /treex/profile 获取用户信息
     * @apiGroup Profile
     */
    @GetMapping("profile")
    fun profileMapping(@RequestAttribute("name") name: String): R {
        val user = userService.getUserByName(name)
        return R.userGen(user)
    }
    /**
     * @api {put} /treex/profile-color 获取用户界面背景
     * @apiGroup Profile
     */
    @PutMapping("profile/background-color")
    fun backgroundMapping(@RequestParam("color") color:Int): Int {
        return color
    }
}