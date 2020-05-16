package tech.laihz.treex_server.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.web.bind.annotation.*
import tech.laihz.treex_server.entity.User
import tech.laihz.treex_server.service.UserService
import tech.laihz.treex_server.utils.*
import java.util.concurrent.TimeUnit
import javax.servlet.http.HttpServletRequest
import javax.websocket.server.PathParam

@RestController
@RequestMapping(value = ["/api"])
class AuthController {
    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var stringRedisTemplate: StringRedisTemplate

    /**
     * @api {get} /login 登录接口
     * @apiVersion 1.0.0
     * @apiGroup Auth
     * @apiName login api
     * @apiParam {String} name 用户名
     * @apiParam {String} password 用户密码
     * @apiParam {int} ttl 登录token存活时间(默认15天)
     * @apiSuccessExample {json} NoUser-Responses:
     *     {
     *      "loginResult": {
     *          "code": 0,
     *          "name": "NO_USER"
     *      },
     *      "status": 200,
     *     }
     * @apiSuccessExample {json} Success-Response:
     *     {
     *      "loginResult": {
     *          "code": 1,
     *          "name": "SUCCESS"
     *      },
     *      "user": {
     *          "backgroundColor": 2201331,
     *          "phone": "",
     *          "background": "",
     *          "name": "userName",
     *          "avatar": "",
     *          "email": ""
     *      },
     *      "status": 200,
     *      "token": "06753856-d9a1-4ca9-83a1-5a94d045ed7e"
     *     }
     * @apiSuccessExample {json} PasswordWrong-Responses:
     *     {
     *      "loginResult": {
     *          "code": 2,
     *          "name": "PASSWORD_WRONG"
     *      },
     *      "status": 200,
     *     }
     */
    @GetMapping("login")
    fun authLogin(user: User, @PathParam("ttl") ttl: Long?): R {
        val tempUser: User? = userService.getUserByName(user.name)
        return if (tempUser == null) {
            R.loginResult(code = 200, loginResultEnum = LoginResult.NO_USER)
        } else {
            if (tempUser.password != user.password) {
                R.loginResult(code = 200, loginResultEnum = LoginResult.PASSWORD_WRONG)
            } else {
                val token = TokenUtil.createToken()
                stringRedisTemplate.opsForValue().set(token, user.name)
                stringRedisTemplate.expire(token, ttl ?: 15 * 60 * 60 * 24, TimeUnit.SECONDS)
                R.loginResult(code = 200, loginResultEnum = LoginResult.SUCCESS, user = tempUser, token = token)
            }
        }
    }

    /**
     * @api {put} /signup 注册接口
     * @apiVersion 1.0.0
     * @apiGroup Auth
     * @apiName Sign up api
     * @apiParam {String} name 用户名
     * @apiParam {String} password 密码,建议在客户端二次验证
     * @apiSuccessExample {json} SUCCESS
     * {
     *  "signupResult":{
     *      "code":0,
     *      "name":"SUCCESS"
     *  },
     *  "status":200
     * }
     * @apiSuccessExample {json} PASSWORD_NULL
     * {
     *  "signupResult":{
     *      "code":1,
     *      "name":"PASSWORD_NULL"
     *  },
     *  "status":200
     * }
     * @apiSuccessExample {json} FAIL
     * {
     *  "signupResult":{
     *      "code":2,
     *      "name":"FAIL"
     *  },
     *  "status":200
     * }
     * @apiSuccessExample {json} HAVE_USER
     * {
     *  "signupResult":{
     *      "code":3,
     *      "name":"HAVE_USER"
     *  },
     *  "status":200
     * }
     */
    @PutMapping("signup")
    fun authSignup(user: User): R {
        val tempUser: User? = userService.getUserByName(user.name)
        return if (tempUser == null) {
            if (user.password.isEmpty()) {
                R.signupResult(code = 200, signUpResultEnum = SignupResult.PASSWORD_NULL)
            } else {
                FileInit.initNamedFileFolder(user.name)
                userService.addUser(user)
                R.signupResult(code = 200, signUpResultEnum = SignupResult.SUCCESS)
            }
        } else {
            R.signupResult(code = 200, signUpResultEnum = SignupResult.HAVE_USER)
        }
    }

    /**
     * @api {delete} /logout 注销登录接口
     * @apiVersion 1.0.0
     * @apiGroup Auth
     * @apiName log out port
     * @apiSuccessExample {json} SuccessLogout-Response
     * {
     *  "logout":true,
     *  "status":200
     * }
     */
    @DeleteMapping("treex/logout")
    fun authLogout(@RequestHeader("Authorization") token: String): R {
        stringRedisTemplate.delete(token)
        return R.logoutResult(code = 200, result = true)
    }

    /**
     * @api {delete} /treex/remove 注销账号接口
     * @apiVersion 1.0.0
     * @apiGroup Auth
     * @apiName remove user from Server
     * @apiHeader {String} authorization token
     * @apiSuccessExample {json} success
     * {
     *  "remove": true,
     *  "status": 200
     * }
     */
    @DeleteMapping("/treex/remove")
    fun authRemove(@RequestHeader("authorization") token: String): R {
        val tokenName = stringRedisTemplate.opsForValue().get(token)
        userService.deleteUserByName(tokenName)
        stringRedisTemplate.delete(token)
        //TODO DELETE ALL FILES
        return R.removeUser(200)
    }


    /**
     * @api {put} /treex/password 修改密码
     * @apiVersion 1.0.0
     * @apiGroup Auth
     * @apiParam {String} old 旧密码
     * @apiParam {String} new 新密码
     * @apiHeader {String} authorization token
     * @apiSuccessExample {json} success
     *{
     *"password": true,
     *"status": 200
     *}
     * @apiSuccessExample {json} fail
     *{
     *"password": false,
     *"status": 200
     *}
     */
    @PutMapping("treex/password")
    fun changePasswordMapping(
            @RequestParam("old") old: String,
            @RequestParam("new") new: String,
            @RequestAttribute("name") name: String
    ): R {
        val truePassword = userService.getUserByName(name).password
        return if (truePassword == old) {
            userService.updateUserPassword(name, new)
            R.authPasswordResult(result = true)
        } else {
            R.authPasswordResult(result = false)
        }
    }
}