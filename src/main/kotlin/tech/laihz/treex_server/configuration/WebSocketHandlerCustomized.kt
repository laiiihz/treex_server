package tech.laihz.treex_server.configuration

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.socket.*
import org.springframework.web.socket.handler.AbstractWebSocketHandler

class WebSocketHandlerCustomized : AbstractWebSocketHandler() {
    private val logger: Logger = LoggerFactory.getLogger(WebSocketHandlerCustomized::class.java)
    private val sessionList = ArrayList<WebSocketSession>()
    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        logger.warn(session.handshakeHeaders["Origin"].toString())
        session.sendMessage(TextMessage("welcome"))
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        logger.info("out")
        sessionList.remove(session)
        sessionList.forEach{
            it.sendMessage(TextMessage("${session.id}离开"))
        }
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        sessionList.add(session)
        sessionList.forEach{
            it.sendMessage(TextMessage("当前频道have${sessionList.size}"))
        }
    }
}