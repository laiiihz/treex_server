package tech.laihz.treex_server.filter

import com.alibaba.fastjson.JSON
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import tech.laihz.treex_server.utils.R
import tech.laihz.treex_server.utils.TokenUtil
import javax.servlet.*
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebFilter(filterName = "AuthFilter" , urlPatterns =["/api/treex/*"])
class AuthFilter : Filter {
    @Autowired
    lateinit var stringRedisTemplate: StringRedisTemplate
    private val logger = LoggerFactory.getLogger(AuthFilter::class.java)
    override fun init(filterConfig: FilterConfig?) {
        super.init(filterConfig)
        logger.info("init auth filter")
    }

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val req:HttpServletRequest = request as HttpServletRequest
        val res:HttpServletResponse = response as HttpServletResponse
        response.characterEncoding = "UTF-8"
        val token:String? = req.getHeader("authorization")
        if("OPTIONS" == req.method){
            res.status = HttpServletResponse.SC_OK
            chain?.doFilter(req,res)
        }else{
            if(token == null){
                val r = R.noPermission()
                res.addHeader("Content-Type",MediaType.APPLICATION_JSON.toString())
                res.writer.write(JSON.toJSONString(r))
                return
            }
            val nameInJedis:String? = stringRedisTemplate.opsForValue().get(token)
            if(nameInJedis==null){
                val r = R.noPermission()
                res.addHeader("Content-Type",MediaType.APPLICATION_JSON.toString())
                res.writer.write(JSON.toJSONString(r))
                return
            }else{
                req.setAttribute("name",nameInJedis)
                chain?.doFilter(request,response)
            }
        }
    }

    override fun destroy() {
        super.destroy()
    }

}