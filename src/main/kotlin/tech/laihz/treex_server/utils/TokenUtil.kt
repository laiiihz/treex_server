package tech.laihz.treex_server.utils

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import redis.clients.jedis.Jedis
import tech.laihz.treex_server.entity.User
import tech.laihz.treex_server.service.UserService

@Component
class TokenUtil {
    private val logger = LoggerFactory.getLogger(TokenUtil::class.java)
    private val jedis = Jedis("127.0.0.1", 6379)
    fun createToken(): String {
        logger.warn(UUIDUtil().UUID)
        return UUIDUtil().UUID
    }

    fun checkToken(token: String): String {
        return jedis.get(token)
    }

}