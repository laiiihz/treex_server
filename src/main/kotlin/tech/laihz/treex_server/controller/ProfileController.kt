package tech.laihz.treex_server.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.core.io.UrlResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import redis.clients.jedis.Jedis
import tech.laihz.treex_server.service.UserService
import tech.laihz.treex_server.utils.R
import java.io.File

@RestController
@RequestMapping("/api/treex")
class ProfileController {
    @Autowired
    lateinit var userService: UserService

    val logger: Logger = LoggerFactory.getLogger(ProfileController::class.java)

    /**
     * @api {get} /treex/profile 获取用户信息
     * @apiGroup Profile
     */
    @GetMapping("profile")
    fun profileMapping(@RequestHeader("Authorization") token: String): R {
        val jedis = Jedis("127.0.0.1", 6379)
        val user = userService.getUserByName(jedis.get(token))
        return R.userGen(user)
    }

    @GetMapping("profile/avatar")
    fun avatarMapping(): ResponseEntity<UrlResource> {
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(UrlResource(File("FILESYSTEM/AVATAR/1.jpg").toURI()))
    }

    @GetMapping("profile/background")
    fun backgroundMapping(): ResponseEntity<UrlResource> {
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(UrlResource(File("FILESYSTEM/BACKGROUND/1.jpg").toURI()))
    }

    /**
     * @api {put} /treex/profile-color 设置用户界面背景
     * @apiGroup Profile
     */
    @PutMapping("profile/background-color")
    fun backgroundMapping(@RequestParam("color") color: Int): Int {
        return color
    }
}