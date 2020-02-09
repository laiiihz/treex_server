package tech.laihz.treex_server.utils

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class TokenUtil {
    private val logger = LoggerFactory.getLogger(TokenUtil::class.java)
    companion object{
        fun createToken(): String {
            return UUIDUtil().UUID
        }
    }

}