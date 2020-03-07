package tech.laihz.treex_server.utils

import tech.laihz.treex_server.entity.User
import java.io.File
import java.nio.file.Files
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

enum class FileResult {
    WRONG_OPERATION,
    SUCCESS,
}

enum class FileRenameResult {
    NOT_FOUND,
    SUCCESS,
}


class ResultUtil : HashMap<String, Any>() {
    companion object {

        fun loginResult(
                code: Int,
                loginResultEnum: LoginResult,
                user: User = User(),
                token: String = ""
        ): R {
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

        fun userGen(user: User): R {
            val r: R = R()
            r["name"] = user.name
            r["phone"] = user.phone
            r["avatar"] = user.avatar
            r["email"] = user.email
            r["background"] = user.background
            r["backgroundColor"] = user.backgroundColor
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

        fun fileResultDefault(code: Int, path: String = "", prefix: String = "", result: FileResult): R {
            val r = R()
            r["status"] = code
            r["fileResult"] = fileResultGen(result)
            if (path.isNotEmpty() && prefix.isNotEmpty()) {
                if (!(path == "." || path == "./")) {
                    r["parent"] = File(File(prefix + path).parentFile.path.replace(prefix, "")).invariantSeparatorsPath
                }
                r["path"] = path.replace(File.separator, "/")
                r["files"] = filesList(File(prefix + path).listFiles(), prefix)
            }
            return r
        }

        fun recycleBinResult(path: String): R {
            val r = R()
            r["status"] = 200
            val recycleList = ArrayList<File>()
            var count = 0
            Files.walk(File(path).toPath()).forEach {
                count++
                if (count != 1)//remove the top
                    recycleList.add(it.toFile())
            }
            r["recycleFiles"] = recycleBinList(files = recycleList, prefix = path)
            return r
        }

        fun searchResult(files: ArrayList<File>, prefix: String): R {
            val r = R()
            val rList = ArrayList<R>()
            r["status"] = 200
            for (item in files) {
                val rSingle = R()
                rSingle["name"] = item.name
                rSingle["path"] = File(item.path.replace(prefix, "./")).invariantSeparatorsPath
                rSingle["isDir"] = item.isDirectory
                rSingle["date"] = item.lastModified()
                if (!item.isDirectory) {
                    rSingle["length"] = item.length()
                }
                rList.add(rSingle)
            }
            r["search"] = rList
            return r
        }

        private fun fileResultGen(result: FileResult): R {
            val r = R()
            r["code"] = result.ordinal
            r["name"] = result.name
            return r
        }

        private fun filesList(files: Array<File>?, prefix: String): List<R> {
            val r = ArrayList<R>()
            for (file in files!!) {
                val singleR = R()
                singleR["name"] = file.name
                singleR["isDir"] = file.isDirectory
                //ONLY SUPPORT UNIX FILE MODIFIED (UNIX TIMESTAMP)
                singleR["date"] = file.lastModified()
                singleR["path"] = File(file.path.replace(prefix, "")).invariantSeparatorsPath
                if (!file.isDirectory) {
                    singleR["length"] = file.length()
                } else {
                    singleR["child"] = file.list()?.size ?: 0
                }

                r.add(singleR)

            }
            return r
        }

        private fun recycleBinList(files: ArrayList<File>, prefix: String): List<R> {
            val r = ArrayList<R>()
            for (file in files) {
                val rSingle = R()
                rSingle["name"] = file.name
                rSingle["path"] = File(file.path.replace(prefix, "./")).invariantSeparatorsPath
                r.add(rSingle)
            }
            return r
        }

        fun fileRename(result: FileRenameResult): R {
            val r = R()
            if (result == FileRenameResult.NOT_FOUND) {
                r["status"] = 404
                r["result"] = fileRenameResultGen(result)
            } else {
                r["status"] = 200
                r["result"] = fileRenameResultGen(result)
            }
            return r
        }

        private fun fileRenameResultGen(result: FileRenameResult): R {
            val r = R()
            r["code"] = result.ordinal
            r["name"] = result.name
            return r
        }

        fun authPasswordResult(result: Boolean): R {
            val r = R()
            r["status"] = 200
            r["password"] = result
            return r
        }

        fun friendList(result:ArrayList<String>):R{
            val r = R()
            r["tag"] = "friends"
            r["friends"] = friendsBuild(result)
            return r
        }

        private fun friendsBuild(result:ArrayList<String>):List<R>{
            val r = ArrayList<R>()
            result.forEach {
                val singleR = R()
                singleR["name"] = it
                r.add(singleR)
            }
            return r
        }

        fun successResult(): R {
            val r = R()
            r["status"] = 200
            r["result"] = "SUCCESS"
            return r
        }

        fun failResult(): R {
            val r = R()
            r["status"] = 0
            r["result"] = "FAIL"
            return r
        }

        fun typedResult(photo: Int, video: Int, music: Int, docs: Int): R {
            val r = R()
            r["photo"] = photo
            r["video"] = video
            r["music"] = music
            r["docs"] = docs
            r["status"] = 200
            return r
        }

        fun spaceResult(used: Long, all:Long): R {
            val r = R()
            r["used"] = used
            r["all"] = all
            r["status"] = 200
            return r
        }
    }
}
