package tech.laihz.treex_server.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import redis.clients.jedis.Jedis
import tech.laihz.treex_server.entity.User
import tech.laihz.treex_server.utils.LoginResult
import tech.laihz.treex_server.utils.R

@RestController
class TestController {
    private val jedis = Jedis("127.0.0.1",6379)

}