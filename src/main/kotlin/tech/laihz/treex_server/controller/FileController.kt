package tech.laihz.treex_server.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.laihz.treex_server.utils.R
import java.io.File
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping(value = ["/api/treex"])

class FileController {
    /**
     * @api {get} /treex/file 获取文件列表
     * @apiGroup Files
     * @apiHeader {String} Authorization token
     * @apiParam {String} path
     *
     */
    @GetMapping("file")
    fun fileMapping(request: HttpServletRequest): R {
        val name = request.getAttribute("name")
        val path = request.getParameter("path")
        val prefix = ".${File.separator}" +
                "FILESYSTEM${File.separator}" +
                "FILES${File.separator}" +
                "${name}${File.separator}"
        //TODO Filter all and tow dot
        return R.fileResultDefault(
                code = 200,
                prefix = prefix,
                path = path
        )
    }
}