package tech.laihz.treex_server.configuration

import io.undertow.Undertow
import io.undertow.server.HttpServerExchange
import io.undertow.servlet.api.*
import org.springframework.boot.web.embedded.undertow.UndertowBuilderCustomizer
import org.springframework.boot.web.embedded.undertow.UndertowDeploymentInfoCustomizer
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory
import org.springframework.boot.web.servlet.server.ServletWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class HttpRedirectConfiguration {
    @Bean
    fun undertowFactory(): ServletWebServerFactory {
        val undertowFactory = UndertowServletWebServerFactory()
        undertowFactory.addBuilderCustomizers(UndertowBuilderCustomizer { builder: Undertow.Builder ->
            builder.addHttpListener(80, "0.0.0.0")
        })
        undertowFactory.addDeploymentInfoCustomizers(UndertowDeploymentInfoCustomizer { deploymentInfo: DeploymentInfo ->
            deploymentInfo.addSecurityConstraint(SecurityConstraint()
                    .addWebResourceCollection(WebResourceCollection().addUrlPattern("/*"))
                    .setTransportGuaranteeType(TransportGuaranteeType.CONFIDENTIAL)
                    .setEmptyRoleSemantic(SecurityInfo.EmptyRoleSemantic.PERMIT)).confidentialPortManager =
                    ConfidentialPortManager { _: HttpServerExchange? -> 443 }
        })
        return undertowFactory
    }

}