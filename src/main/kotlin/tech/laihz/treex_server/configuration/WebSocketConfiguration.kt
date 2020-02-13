package tech.laihz.treex_server.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
class WebSocketConfiguration :WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
    registry.addHandler(wsHandler(),"/api/ws")
            .setAllowedOrigins("*")
    }

    @Bean
    fun wsHandler(): WebSocketHandler {
        return WebSocketHandlerCustomized()
    }
}