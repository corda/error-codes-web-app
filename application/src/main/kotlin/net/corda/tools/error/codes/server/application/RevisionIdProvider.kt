package net.corda.tools.error.codes.server.application

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.ConfigSpec
import net.corda.tools.error.codes.server.application.annotations.Application
import net.corda.tools.error.codes.server.domain.loggerFor
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Named

@Application
@Named
class RevisionIdProvider @Inject constructor(applyConfigStandards: (Config) -> Config) : () -> RevisionId {

    private companion object {

        private const val CONFIGURATION_SECTION_PATH = "configuration.metadata.revision"
        private val logger = loggerFor<RevisionIdProvider>()

        private object Spec : ConfigSpec(CONFIGURATION_SECTION_PATH) {

            val id by optional("unknown")
        }
    }

    private val config = applyConfigStandards.invoke(Config { addSpec(Spec) })
    private val revision = RevisionId(config[Spec.id])

    override fun invoke() = revision

    @PostConstruct
    internal fun init() {

        logger.info("Revision ID is: ${revision.value}")
    }
}