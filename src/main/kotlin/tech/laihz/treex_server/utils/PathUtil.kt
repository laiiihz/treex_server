package tech.laihz.treex_server.utils

import java.io.File

class PathUtil {
    companion object {
        fun prefix(name: String): String {
            return ".${File.separator}" +
                    "FILESYSTEM${File.separator}" +
                    "FILES${File.separator}" +
                    "${name}${File.separator}"
        }
        fun sharedPrefix():String{
            return ".${File.separator}" +
                    "FILESYSTEM${File.separator}" +
                    "SHARE${File.separator}"
        }
    }
}