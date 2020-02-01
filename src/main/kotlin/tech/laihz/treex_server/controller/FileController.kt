package tech.laihz.treex_server.controller

import org.springframework.web.bind.annotation.*
import tech.laihz.treex_server.utils.FileRenameResult
import tech.laihz.treex_server.utils.FileResult
import tech.laihz.treex_server.utils.PathUtil
import tech.laihz.treex_server.utils.R
import java.io.File
import javax.servlet.http.HttpServletRequest

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
        if(path.contains("..")){
            return R.fileResultDefault(code = 200,result = FileResult.WRONG_OPERATION)
        }
        return R.fileResultDefault(code=200,prefix = prefix,path = path,result = FileResult.SUCCESS)
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
            @RequestParam("new") new:String
    ): R {
        val tempFile = File(PathUtil.prefix(name) + file)
        if(!tempFile.exists()){
            return R.fileRename(result = FileRenameResult.NOT_FOUND)
        }
        val parentFile = File(PathUtil.prefix(name) + file).parentFile
        tempFile.renameTo(File(parentFile.path+File.separator+new))
        return R.fileRename(result = FileRenameResult.SUCCESS)
    }
}