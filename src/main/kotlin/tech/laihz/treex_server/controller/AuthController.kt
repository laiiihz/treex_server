package tech.laihz.treex_server.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import redis.clients.jedis.Jedis
import tech.laihz.treex_server.entity.User
import tech.laihz.treex_server.service.UserService
import tech.laihz.treex_server.utils.*
import javax.servlet.http.HttpServletRequest
import javax.websocket.server.PathParam

@RestController
@RequestMapping(value = ["/api"])
class AuthController {
    @Autowired
    lateinit var userService: UserService
    val jedis = Jedis("127.0.0.1", 6379)

    /**
     * @api {get} /login 登录接口
     * @apiGroup auth
     * @apiName login
     * @apiParam {String} name
     * @apiParam {String} password
     * @apiParam {int} ttl OPTIONAL
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
     *          "name": "Alice"
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
    fun authLogin(user: User, @PathParam("ttl") ttl: Int?): R {
        val tempUser: User? = userService.getUserByName(user.name)
        return if (tempUser == null) {
            R.loginResult(code = 200, loginResultEnum = LoginResult.NO_USER)
        } else {
            if (tempUser.password != user.password) {
                R.loginResult(code = 200, loginResultEnum = LoginResult.PASSWORD_WRONG)
            } else {
                val token = TokenUtil().createToken()
                jedis.setex(token, ttl ?: 15 * 60 * 60 * 24, user.name) // default 15 day TTL (redis)
                R.loginResult(code = 200, loginResultEnum = LoginResult.SUCCESS, user = tempUser, token = token)
            }
        }
    }

    /**
     * @api {put} /signup 注册接口
     * @apiGroup auth
     * @apiParam name
     * @apiParam password
     * @apiSuccessExample {json} SUCCESS
     * {
     *  "signupResult":{
     *      "code":0,
     *      "name":"SUCCESS"
     *  },
     *  "status":200
     * }
     * @apiSuccessExample {json} HAVE_USER
     * {
     *  "signupResult":{
     *      "code":1,
     *      "name":"HAVE_USER"
     *  },
     *  "status":200
     * }
     * @apiSuccessExample {json} PASSWORD_NULL
     * {
     *  "signupResult":{
     *      "code":3,
     *      "name":"PASSWORD_NULL"
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
     * @apiGroup auth
     * @apiParam {String} token
     * @apiSuccessExample {json} SuccessLogout-Response
     * {
     *  "logout":true,
     *  "status":200
     * }
     */
    @DeleteMapping("logout")
    fun authLogout(@PathParam("token") token: String): R {
        jedis.del(token)
        return R.logoutResult(code = 200, result = true)
    }

    /**
     * @api {delete} /treex/remove 注销账号接口
     * @apiGroup auth
     */
    @DeleteMapping("/treex/remove")
    fun authRemove(request: HttpServletRequest): R {
        val token = request.getHeader("authorization")
        val tokenName = TokenUtil().checkToken(token)
        userService.deleteUserByName(tokenName)
        jedis.del(token)
        //TODO DELETE ALL FILES
        return  R.removeUser(200)
    }
}