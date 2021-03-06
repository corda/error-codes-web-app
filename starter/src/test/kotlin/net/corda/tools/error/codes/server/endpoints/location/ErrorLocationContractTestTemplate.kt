package net.corda.tools.error.codes.server.endpoints.location

import net.corda.tools.error.codes.server.ErrorCodesWebAppStarter
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
import javax.inject.Inject

@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [ErrorLocationContractTestTemplate.Configuration::class])
internal abstract class ErrorLocationContractTestTemplate : ErrorLocationContractSpecification {

    @Inject
    private lateinit var webServer: WebServer

    private companion object {

        private var errorCoordinates: ErrorCoordinates? = null
        private var descriptions: Flux<out ErrorDescription>? = null
    }

    override fun startWebServerWithStubbedRepository(errorCoordinatesForServer: ErrorCoordinates, descriptionsReturned: Flux<out ErrorDescription>): Port {

        errorCoordinates = errorCoordinatesForServer
        descriptions = descriptionsReturned

        return webServer.actualPort!!
    }

    @TestBean
    @ComponentScan(basePackageClasses = [ErrorCodesWebAppStarter::class], excludeFilters = [ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = [ErrorCodesWebAppStarter::class, WebServer.Options::class]), ComponentScan.Filter(type = FilterType.ANNOTATION, classes = [Adapter::class, TestBean::class])])
    @SpringBootApplication
    internal open class Configuration {

        @TestBean
        @Bean
        open fun webServerOptions(): WebServer.Options {

            return object : WebServer.Options {

                override val port = Port(0)
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