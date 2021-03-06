package tech.laihz.treex_server.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.io.UrlResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import tech.laihz.treex_server.entity.FileType
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
    val logger: Logger = LoggerFactory.getLogger(FileController::class.java)

    /**
     * @api {get} /treex/file 获取文件列表
     * @apiGroup Files
     * @apiVersion 1.0.0
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
     * @apiVersion 1.0.0
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

    /**
     * @api {delete} /treex/share 删除公共文件(包含文件夹)
     * @apiVersion 1.0.0
     * @apiName delete shared file
     * @apiGroup Files
     * @apiParam {String} path 文件路径
     */
    @DeleteMapping("share")
    fun deleteShareMapping(@RequestParam("path") path: String): R {
        Files.walk(File(PathUtil.sharedPrefix() + path).toPath())
                .sorted(Comparator.reverseOrder())
                .forEach {
                    it.toFile().delete()
                }
        return R.successResult()
    }

    /**
     * @api {get} /treex/file/download 下载私有文件
     * @apiVersion 1.0.0
     * @apiName down private file
     * @apiGroup Files
     * @apiParam {String} path file
     * @apiParam {Boolean} share 
     * @apiHeader {String} authorization token
     */
    @GetMapping("file/download")
    fun privateDownloadMapping(
            @RequestAttribute("name") name: String,
            @RequestParam("path") path: String,
            @RequestParam("share") share: Boolean
    ): ResponseEntity<UrlResource> {
        val filePath = if (share) PathUtil.sharedPrefix() else PathUtil.prefix(name)
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(UrlResource(File("${filePath}${path}").toURI()))
    }

    /**
     * @api {post} /treex/upload 上传文件(单个小文件)
     * @apiVersion 1.0.0
     * @apiGroup Files
     * @apiHeader {String} authorization token
     * @apiParam {MultipartFile} file
     * @apiParam {String} file
     * @apiParam {String} path
     * @apiParam {Boolean} share
     */
    @PostMapping("upload")
    fun postFileMapping(
            @RequestParam("path") path: String,
            @RequestParam("name") fileName: String,
            @RequestParam("share") share: Boolean,
            @RequestParam("file") file: MultipartFile,
            @RequestAttribute("name") name: String
    ): R {
        if (path.contains("..")) return R.noPermission()
        val prefix =
                if (share) PathUtil.sharedPrefix()
                else PathUtil.prefix(name)
        File(prefix + path).mkdirs()
        Files.copy(file.inputStream, File(
                prefix + path + File.separator + fileName).toPath(),
                StandardCopyOption.REPLACE_EXISTING
        )
        return R.successResult()
    }

    /**
     * @api {put} /treex/file/rename 文件重命名
     * @apiVersion 1.0.0
     * @apiName File Rename
     * @apiGroup Files
     * @apiHeader {String} Authorization token
     * @apiParam {String} file 需要修改的文件路径
     * @apiParam {String} new 新文件名
     * @apiParam {Bool} share 共享文件夹
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
            @RequestParam("new") new: String,
            @RequestParam("share") share: Boolean
    ): R {
        val tempFile = File(PathUtil.prefix(name) + file)
        val shareFile = File(PathUtil.sharedPrefix() + file)
        if (share) {
            if (!shareFile.exists()) return R.fileRename(result = FileRenameResult.NOT_FOUND)
            val parentFile = shareFile.parentFile
            Files.move(shareFile.toPath(), File(parentFile.path + File.separator + new).toPath(), StandardCopyOption.ATOMIC_MOVE)
            return R.fileRename(result = FileRenameResult.SUCCESS)
        } else {
            when (!tempFile.exists()) {
                true -> return R.fileRename(result = FileRenameResult.NOT_FOUND)
            }
            val parentFile = tempFile.parentFile
            Files.move(tempFile.toPath(), File(parentFile.path + File.separator + new).toPath(), StandardCopyOption.ATOMIC_MOVE)
            return R.fileRename(result = FileRenameResult.SUCCESS)
        }

    }

    /**
     * @api {put} /treex/file/newFolder 新建文件夹
     * @apiVersion 1.0.0
     * @apiGroup Files
     * @apiName new folder
     * @apiHeader {String} authorization token
     * @apiParam {String} path
     * @apiParam {String} folder
     * @apiParam {Boolean} share
     */
    @PutMapping("file/newFolder")
    fun newFolderMapping(
            @RequestParam("path") path: String,
            @RequestParam("folder") folder: String,
            @RequestParam("share") share: Boolean,
            @RequestAttribute("name") name: String

    ): R {
        val prefixPath = if (share) PathUtil.sharedPrefix() else PathUtil.prefix(name)
        Files.createDirectories(File(
                "${prefixPath}${File.separator}${path}${File.separator}${folder}"
        ).toPath())
        return R.successResult()
    }

    /**
     * @api {delete} /treex/file 文件删除(移动到回收站)
     * @apiVersion 1.0.0
     * @apiName delete file
     * @apiGroup Files
     * @apiParam {String} path 文件路径
     * @apiHeader {String} authorization token
     */
    @DeleteMapping("file")
    fun deleteMapping(
            @RequestParam("path") path: String,
            @RequestAttribute("name") name: String
    ): R {
        val tempFile = File(PathUtil.prefix(name) + path)
        val tempFileBin = File(PathUtil.prefixBin(name) + path)
        Files.move(tempFile.toPath(), tempFileBin.toPath(), StandardCopyOption.ATOMIC_MOVE)
        return R.successResult()
    }

    /**
     * @api {get} /treex/file/recycle 回收站
     * @apiVersion 1.0.0
     * @apiName recycle bin
     * @apiGroup Files
     */
    @GetMapping("file/recycle")
    fun recycleMapping(@RequestAttribute("name") name: String): R {
        return R.recycleBinResult(PathUtil.prefixBin(name))
    }

    /** @api {put} /treex/file/recovery 文件恢复(从回收站恢复文件)
     * @apiVersion 1.0.0
     * @apiGroup Files
     * @apiHeader {String} authorization token
     * @apiParam {String} path 恢复路径
     *
     */
    @PutMapping("file/recovery")
    fun recoveryMapping(@RequestParam("path") path: String, @RequestAttribute("name") name: String): R {

        return R.successResult()
    }

    /**
     * @api {delete} /treex/file/clear 清空回收站
     * @apiVersion 1.0.0
     * @apiName clear recycle bin
     * @apiGroup Files
     * @apiHeader {String} authorization token
     */
    @DeleteMapping("file/clear")
    fun clearRBMapping(@RequestAttribute("name") name: String): R {
        Files.walk(File(PathUtil.prefixBin(name)).toPath())
                .sorted(Comparator.reverseOrder())
                .forEach {
                    it.toFile().delete()
                }
        Files.createDirectory(File(PathUtil.prefixBin(name)).toPath())
        return R.successResult()
    }

    /**
     * @api {get} /treex/file/type 文件分类
     * @apiVersion 1.0.0
     * @apiName count all typed files
     * @apiGroup Files
     * @apiHeader {String} authorization token
     * @apiIgnore TODO
     */
    @GetMapping("file/typeCount")
    fun fileTypedMapping(@RequestAttribute("name") name: String): R {
        val type: FileType = FileType()
        Files.walk(File(PathUtil.prefix(name)).toPath()).forEach {
            val index = it.toFile().name.lastIndexOf(".")
            val nowFile = it.toFile()
            if (index != -1) {
            }
            when (nowFile.name.substring(index + 1)) {
                "jpg", "png", "jpeg" -> type.media.photo.add(nowFile)
                "mp4", "mkv", "mov" -> type.media.video.add(nowFile)
                "mp3", "aac" -> type.media.music.add(nowFile)
                "doc", "docx" -> type.document.doc.add(nowFile)
                "xls", "xlsx" -> type.document.excel.add(nowFile)
                "ppt", "pptx" -> type.document.powerPoint.add(nowFile)
            }
        }

        val r = R()
        r["types"] = type
        return r
    }

    /**
     * @api {get} /treex/file/search 文件搜索
     * @apiVersion 1.0.0
     * @apiName Search File In Files
     * @apiGroup Files
     * @apiHeader {String} authorization token
     * @apiParam {String} query
     * @apiParam {bool} share
     */
    @GetMapping("file/search")
    fun searchMapping(
            @RequestParam("query") query: String,
            @RequestParam("share") share: Boolean,
            @RequestAttribute("name") name: String
    ): R {
        val prefix =
                if (share) PathUtil.sharedPrefix()
                else PathUtil.prefix(name)
        val files = ArrayList<File>()
        Files.walk(File(prefix).toPath()).forEach {
            val tempFile = it.toFile()
            if (tempFile.path.contains(query)) {
                files.add(tempFile)
            }
        }
        return R.searchResult(files = files, prefix = prefix)
    }

}