package tech.laihz.treex_server.controller

import org.springframework.core.io.UrlResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import tech.laihz.treex_server.utils.FileRenameResult
import tech.laihz.treex_server.utils.FileResult
import tech.laihz.treex_server.utils.PathUtil
import tech.laihz.treex_server.utils.R
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

@RestController
@RequestMapping(value = ["/api/treex"])

class FileController {
    /**
     * @api {get} /treex/file 获取文件列表
     * @apiGroup Files
     * @apiName Get File List
     * @apiHeader {String} Authorization token
     * @apiParam {String} path
     * @apiSuccessExample {json} SUCCESS
     * {
     *  "path": ".",
     *  "fileResult": {
     *      "code": 1,
     *      "name": "SUCCESS"
     *  },
     *  "files": [
     *      {
     *          "date": 1580444117897,
     *          "name": "FILE.exe",
     *          "length": 22473144,
     *          "isDir": false
     *      },
     *      {
     *         "date": 1578815565806,
     *          "name": "FILE.jpg",
     *          "length": 10492028,
     *          "isDir": false
     *      },
     *      {
     *          "date": 1580531117053,
     *          "name": "TEST",
     *          "isDir": true,
     *          "child": 3
     *      }
     *  ],
     *  "status": 200
     * }
     * @apiSuccessExample {json} WRONG-OPERATION
     *{
     *  "fileResult": {
     *      "code": 0,
     *      "name": "WRONG_OPERATION"
     *  },
     *  "status": 200
     *}
     */
    @GetMapping("file")
    fun fileMapping(
            @RequestAttribute("name") name: String,
            @RequestParam("path") path: String
    ): R {
        val prefix = PathUtil.prefix(name)
        if (path.contains("..")) {
            return R.fileResultDefault(code = 200, result = FileResult.WRONG_OPERATION)
        }
        return R.fileResultDefault(
                code = 200,
                prefix = prefix,
                path = path,
                result = FileResult.SUCCESS
        )
    }

    /**
     * @api {get} /treex/share 获取公共文件列表
     * @apiGroup Files
     * @apiName Get Share File List
     * @apiHeader {String} Authorization token
     * @apiParam {String} path
     * @apiSuccessExample {json} SUCCESS
     * {
     *  "path": ".",
     *  "fileResult": {
     *      "code": 1,
     *      "name": "SUCCESS"
     *  },
     *  "files": [],
     *  "status": 200
     * }
     * @apiSuccessExample {json} WRONG-OPERATION
     *{
     *  "fileResult": {
     *      "code": 0,
     *      "name": "WRONG_OPERATION"
     *  },
     *  "status": 200
     *}
     */
    @GetMapping("share")
    fun shareMapping(@RequestParam("path") path: String): R {
        val prefix = PathUtil.sharedPrefix()
        if (path.contains("..")) {
            return R.fileResultDefault(code = 200, result = FileResult.WRONG_OPERATION)
        }
        return R.fileResultDefault(code = 200, prefix = prefix, path = path, result = FileResult.SUCCESS)
    }

    /** @api {get} /treex/share/download 获取共享文件列表
     * @apiGroup Files
     *
     */
    @GetMapping("share/download")
    fun shareDownloadMapping(@RequestParam("path") path: String): ResponseEntity<UrlResource> {
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(UrlResource(File("FILESYSTEM/SHARE/${path}").toURI()))
    }

    /**@api {post} /treex/share
     *
     */
    @PostMapping("share")
    fun postShareMapping(
            @RequestParam("file") file: MultipartFile,
            @RequestParam("path") path: String,
            @RequestParam("name") name: String
    ): R {
        if(path.contains(".."))return R.noPermission()
        File("FILESYSTEM/SHARE/${path}").mkdirs()
        Files.copy(file.inputStream, File("FILESYSTEM/SHARE/${path}/${name}").toPath(), StandardCopyOption.REPLACE_EXISTING)
        return R.successResult()
    }

    /**
     * @api {put} /treex/file/rename 文件重命名
     * @apiName File Rename
     * @apiGroup Files
     * @apiHeader {String} Authorization token
     * @apiParam {String} file 需要修改的文件路径
     * @apiParam {String} new 新文件名
     * @apiSuccessExample {json} SUCCESS
     * {
     *  "result": {
     *      "code": 1,
     *      "name": "SUCCESS"
     *  },
     *  "status": 200
     * }
     * @apiSuccessExample {json} NOT_FOUND
     * {
     *  "result": {
     *      "code": 0,
     *      "name": "NOT_FOUND"
     *  },
     *  "status": 404
     * }
     *
     */
    @PutMapping("file/rename")
    fun fileRenameMapping(
            @RequestAttribute("name") name: String,
            @RequestParam("file") file: String,
            @RequestParam("new") new: String
    ): R {
        val tempFile = File(PathUtil.prefix(name) + file)
        if (!tempFile.exists()) {
            return R.fileRename(result = FileRenameResult.NOT_FOUND)
        }
        val parentFile = File(PathUtil.prefix(name) + file).parentFile
        tempFile.renameTo(File(parentFile.path + File.separator + new))
        return R.fileRename(result = FileRenameResult.SUCCESS)
    }

    /** @api {delete} /treex/file/delete 文件删除
     * @apiGroup Files
     * @apiParam {String} path
     */
    @DeleteMapping("file/delete")
    fun deleteMapping(path: String): R {
        return R.removeUser(code = 200)
    }
}