package net.corda.tools.error.codes.server.web.endpoints

import com.uchuhimo.konf.Config
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import net.corda.tools.error.codes.server.application.RevisionId
import net.corda.tools.error.codes.server.application.annotations.Application
import net.corda.tools.error.codes.server.web.endpoints.template.ConfigurableEndpoint
import net.corda.tools.error.codes.server.web.endpoints.template.EndpointConfigProvider
import javax.inject.Inject
import javax.inject.Named

@Named
internal class HealthCheckEndpoint @Inject constructor(configuration: HealthCheckEndpoint.Configuration) : ConfigurableEndpoint(configuration, setOf(HttpMethod.GET)) {

    private val answer = JsonObject().put("message", "Healthy as a trout!").put("revision_id", configuration.revisionId.value)

    override fun install(router: Router) {

        // Here we could use a function to find out whether the application is healthy or not. If not, provide a message to explain why not.
        // The function would get injected with all the managed components implementing an `HealthCheckable` interface.
        serve(router.get(path)) { response().setStatusCode(HttpResponseStatus.OK.code()).end(answer) }
    }

    interface Configuration : ConfigurableEndpoint.Configuration {

        val revisionId: RevisionId
    }

    @Named
    internal class HealthCheckConfigProvider @Inject constructor(applyConfigStandards: (Config) -> Config, @Application revisionId: () -> RevisionId) : HealthCheckEndpoint.Configuration, EndpointConfigProvider(CONFIGURATION_SECTION_PATH, applyConfigStandards) {

        override val revisionId = revisionId.invoke()

        private companion object {

            private const val CONFIGURATION_SECTION_PATH = "configuration.web.server.endpoints.healthcheck"
        }
    }
}
