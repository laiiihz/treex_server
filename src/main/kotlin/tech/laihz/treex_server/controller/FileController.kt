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
    @GetMapping("file")
    fun fileMapping(request: HttpServletRequest): R {
        val name = request.getAttribute("name")
        return R.fileResultDefault(
                code = 200,
                file = File(
                        ".${File.separator}" +
                                "FILESYSTEM${File.separator}" +
                                "FILES${File.separator}" +
                                name
                )
        )
    }
}