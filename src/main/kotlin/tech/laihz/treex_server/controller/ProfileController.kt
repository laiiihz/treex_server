package tech.laihz.treex_server.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.UrlResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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
     * @apiVersion 1.0.0
     * @apiGroup Profile
     * @apiHeader {String} authorization token
     */
    @GetMapping("profile")
    fun profileMapping(@RequestAttribute("name") name:String): R {
        val user = userService.getUserByName(name)
        return R.userGen(user)
    }

    /**
     * @api {get} /treex/profile/avatar 获取头像(文件)
     * @apiVersion 1.0.0
     * @apiGroup Profile
     * @apiHeader {String} authorization token
     */
    @GetMapping("profile/avatar")
    fun avatarMapping(): ResponseEntity<UrlResource> {
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(UrlResource(File("FILESYSTEM/AVATAR/1.jpg").toURI()))
    }

    /**
     * @api {get} /treex/profile/background 获取背景(文件)
     * @apiVersion 1.0.0
     * @apiGroup Profile
     */
    @GetMapping("profile/background")
    fun backgroundMapping(): ResponseEntity<UrlResource> {
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(UrlResource(File("FILESYSTEM/BACKGROUND/1.jpg").toURI()))
    }

    /**
     * @api {put} /treex/profile-color 设置用户界面背景
     * @apiVersion 1.0.0
     * @apiGroup Profile
     */
    @PutMapping("profile/background-color")
    fun backgroundMapping(@RequestParam("color") color: Int): Int {
        return color
    }

    /**
     * @api {put} /treex/profile/phone 设置手机号码
     * @apiVersion 1.0.0
     * @apiGroup Profile
     * @apiHeader {String} authorization token
     * @apiParam {String} phone
     */
    @PutMapping("profile/phone")
    fun phoneMapping(
            @RequestParam("phone") phone: String,
            @RequestAttribute("name") name: String
    ): R {
        userService.updateUserPhone(name,phone)
        return R.successResult()
    }

    /**
     * @api {put} /treex/profile/email 设置邮箱
     * @apiVersion 1.0.0
     * @apiGroup Profile
     * @apiHeader {String} authorization token
     * @apiParam {String} email
     */
    @PutMapping("profile/email")
    fun emailMapping(
            @RequestParam("email") email: String,
            @RequestAttribute("name") name: String
    ):R{
        userService.updateUserEmail(name,email)
        return R.successResult()
    }
}