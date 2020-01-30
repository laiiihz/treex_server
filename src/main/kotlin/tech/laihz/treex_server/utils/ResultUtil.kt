package tech.laihz.treex_server.utils

import tech.laihz.treex_server.entity.User
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
    }
}