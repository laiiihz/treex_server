package tech.laihz.treex_server.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import tech.laihz.treex_server.utils.FileInit
import javax.annotation.PostConstruct

@Component
class InitService {
    val logger: Logger = LoggerFactory.getLogger(InitService::class.java)
    @PostConstruct
    fun init() {
        logger.info("""INIT PROJECT FILE""")
        FileInit.initFile()
    }
}