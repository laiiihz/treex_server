package tech.laihz.treex_server.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import tech.laihz.treex_server.entity.User
import java.io.File
import kotlin.collections.HashMap

typealias R = ResultUtil

enum class LoginResult {
    NO_USER,
    SUCCESS,
    PASSWORD_WRONG,
}

enum class SignupResult {
    SUCCESS,
    PASSWORD_NULL,
    FAIL,
    HAVE_USER
}

class ResultUtil : HashMap<String, Any>() {
    companion object {

        val logger: Logger = LoggerFactory.getLogger(ResultUtil::class.java)

        fun loginResult(code: Int, loginResultEnum: LoginResult, user: User = User(), token: String = ""): R {
            val r: R = R()
            r["status"] = code
            r["loginResult"] = loginResultCodeGen(loginResultEnum)
            if (loginResultEnum == LoginResult.SUCCESS) {
                r["user"] = userGen(user)
                r["token"] = token
            }

            return r
        }

        private fun loginResultCodeGen(loginResult: LoginResult): R {
            val r: R = R()
            r["code"] = loginResult.ordinal
            r["name"] = loginResult.name
            return r
        }

        private fun userGen(user: User): R {
            val r: R = R()
            r["name"] = user.name
            return r
        }

        fun logoutResult(code: Int, result: Boolean): R {
            val r = R()
            r["status"] = code
            r["logout"] = result
            return r
        }

        fun noPermission(): R {
            val r = R()
            r["status"] = 403
            r["message"] = "NO PERMISSION"
            return r
        }

        fun signupResult(code: Int, signUpResultEnum: SignupResult): R {
            val r = R()
            r["status"] = code
            r["signupResult"] = signupResultCodeGen(signUpResultEnum)
            return r
        }

        private fun signupResultCodeGen(signUpResultEnum: SignupResult): R {
            val r = R()
            r["code"] = signUpResultEnum.ordinal
            r["name"] = signUpResultEnum.name
            return r
        }

        fun removeUser(code: Int): R {
            val r = R()
            r["status"] = code
            r["remove"] = true
            return r
        }

        fun fileResultDefault(code: Int, file: File): R {
            val r = R()
            r["status"] = code
            r["path"] = file.path.replace(File.separator,"/")
            r["files"] = filesList(file.listFiles())
            return r
        }

        private fun filesList(files: Array<File>?): List<R> {
            val r = ArrayList<R>()
            for (file in files!!) {
                val singleR = R()
                singleR["name"] = file.name
                r.add(singleR)

            }
            return r
        }
    }
}
