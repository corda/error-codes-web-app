package net.corda.tools.error.codes.server

import net.corda.tools.error.codes.server.commons.domain.Port
import net.corda.tools.error.codes.server.domain.ErrorCode
import net.corda.tools.error.codes.server.domain.ErrorCoordinates
import net.corda.tools.error.codes.server.domain.ErrorDescription
import net.corda.tools.error.codes.server.domain.InvocationContext
import net.corda.tools.error.codes.server.domain.annotations.Adapter
import net.corda.tools.error.codes.server.test.annotations.TestBean
import net.corda.tools.error.codes.server.web.WebServer
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.core.publisher.Flux
import java.net.InetSocketAddress
import java.net.ServerSocket
import javax.inject.Inject

// TODO add another test class that starts the entire thing as a test, to check that everything is wired up correctly.
@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [ErrorCodesWebAppContractTest.Configuration::class])
internal class ErrorCodesWebAppContractTest : ErrorCodesWebAppContractSpecification {

    @Inject
    private lateinit var webServer: WebServer

    private companion object {

        private var errorCoordinates: ErrorCoordinates? = null
        private var descriptions: Flux<out ErrorDescription>? = null
    }

    override fun startWebServerWithStubbedRepository(errorCoordinatesForServer: ErrorCoordinates, descriptionsReturned: Flux<out ErrorDescription>): Int {

        errorCoordinates = errorCoordinatesForServer
        descriptions = descriptionsReturned

        return webServer.options.port.value
    }

    @TestBean
    @ComponentScan(basePackageClasses = [ErrorCodesWebAppStarter::class], excludeFilters = [ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = [ErrorCodesWebAppStarter::class, WebServer.Options::class]), ComponentScan.Filter(type = FilterType.ANNOTATION, classes = [Adapter::class, TestBean::class])])
    @SpringBootApplication
    internal open class Configuration {

        @TestBean
        @Bean
        open fun webServerOptions(): WebServer.Options {

            return ServerSocket().use {

                it.reuseAddress = true
                it.bind(InetSocketAddress(0))
                object : WebServer.Options {

                    override val port = Port(it.localPort)
                }
            }
        }

        @Adapter
        @TestBean
        @Bean
        open fun repository(): (ErrorCode, InvocationContext) -> Flux<out ErrorDescription> {

            return object : (ErrorCode, InvocationContext) -> Flux<out ErrorDescription> {

                override fun invoke(errorCode: ErrorCode, invocationContext: InvocationContext): Flux<out ErrorDescription> {

                    return descriptions!!
                }
            }
        }
    }
}