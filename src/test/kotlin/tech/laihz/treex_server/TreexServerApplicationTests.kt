package tech.laihz.treex_server

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.Future
import kotlin.random.Random

@SpringBootTest
class TreexServerApplicationTests {
    val logger = LoggerFactory.getLogger(TreexServerApplicationTests::class.java)!!
    val okHttpClient = OkHttpClient()
    val testName = "name-${Random.nextInt()}"
    @Test
    @Order(2)
    fun `sign up assertion`() {
        val response = okHttpClient.newCall(
                Request.Builder()
                        .url("http://127.0.0.1:8080/api/signup?name=${testName}&password=test")
                        .build()
        ).execute()
        logger.info(response.body?.string())
        val request2 = Request.Builder().url("http://127.0.0.1:8080/api/login?name=${testName}&password=test").build()
        val response2 = okHttpClient.newCall(request2).execute()
        logger.info(response2.body?.string())

    }

    @Test
    @Order(1)
    fun `login assertion`() {


    }

}
