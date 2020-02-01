package tech.laihz.treex_server.utils

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import redis.clients.jedis.Jedis

@Component
class TokenUtil {
    private val logger = LoggerFactory.getLogger(TokenUtil::class.java)
    private val jedis = Jedis("127.0.0.1", 6379)
    fun createToken(): String {
        return UUIDUtil().UUID
    }

    fun checkToken(token: String): String? {
        return jedis.get(token)
    }

}