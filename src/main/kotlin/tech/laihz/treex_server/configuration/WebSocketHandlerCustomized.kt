package tech.laihz.treex_server.configuration

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import org.springframework.web.socket.*
import org.springframework.web.socket.handler.AbstractWebSocketHandler
import tech.laihz.treex_server.service.UserService
import tech.laihz.treex_server.utils.R

@Service
class WebSocketHandlerCustomized : AbstractWebSocketHandler() {
    @Autowired
    lateinit var stringRedisTemplate: StringRedisTemplate

    @Autowired
    lateinit var userService: UserService

    private val logger: Logger = LoggerFactory.getLogger(WebSocketHandlerCustomized::class.java)
    private val sessionList = ArrayList<WebSocketSession>()
    private val sessionUserMap = HashMap<String, ArrayList<WebSocketSession>>()

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val json = JSON.parseObject(message.payload)
        val tag = json["tag"] as String
        when (tag) {
            "handshake" -> {
                val token = json["token"] as String
                val userName = stringRedisTemplate.opsForValue().get(token) as String
                var tokens = sessionUserMap[userName]
                if (tokens.isNullOrEmpty())
                    tokens = ArrayList()
                tokens.add(session)
                sessionUserMap[userName] = tokens
                tokens.forEach {
                    logger.info(it.id)
                }
                session.sendMessage(TextMessage(userName))
            }
            "users" ->{
                val friendsList = ArrayList<String>()
                sessionUserMap.forEach { (key, _) -> friendsList.add(key)}
                val messageSend= JSON.toJSONString(R.friendList(result = friendsList))
                session.sendMessage(TextMessage(messageSend))
            }
        }
        session.sendMessage(TextMessage("welcome"))
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessionList.remove(session)

        sessionList.forEach {
            it.sendMessage(TextMessage("${session.id}离开"))
        }
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        sessionList.add(session)
        sessionList.forEach {
            it.sendMessage(TextMessage("当前频道have${sessionList.size}"))
        }
    }
}