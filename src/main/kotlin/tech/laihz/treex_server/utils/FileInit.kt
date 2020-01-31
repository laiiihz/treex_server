package tech.laihz.treex_server.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

class FileInit {
    private val logger: Logger = LoggerFactory.getLogger(FileInit::class.java)

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(FileInit::class.java)
        fun initFileFolder() {
            logger.warn("FXXXK")
            val files: List<String> = listOf(
                    "FILESYSTEM${File.separator}${File.separator}AVATAR",
                    "FILESYSTEM${File.separator}FILES",
                    "FILESYSTEM${File.separator}SHARE"
                    )
            for (file in files){
                if (!File(file).exists()) {
                    File(file).mkdirs()
                }
            }
        }
        fun initNamedFileFolder(name:String){
            val file = "FILESYSTEM${File.separator}FILES${File.separator}${name}"
            logger.warn(file)
            File(file).mkdir()
        }
    }

}