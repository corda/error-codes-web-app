package net.corda.tools.error.codes.server

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.ConfigSpec
import java.nio.file.Paths
import javax.inject.Named

@Named
internal class ConfigLocatingDecorator : (Config) -> Config {

    private companion object {

        private const val CONFIGURATION_FILE = "configuration.file"
        private const val CONFIGURATION_FILE_PATH = "path"
        private const val CONFIGURATION_FILE_RESOURCE_NAME = "configuration.yml"

        private object Spec : ConfigSpec(CONFIGURATION_FILE) {

            val path by optional<String?>(name = CONFIGURATION_FILE_PATH, default = null)
        }

        private val rootConfig = Config { addSpec(Spec) }.from().env().from().systemProperties()
    }

    override fun invoke(config: Config): Config = fromExternalFile().invoke(fromResource(config)).from.env().from.systemProperties()

    private fun fromExternalFile(): (Config) -> Config {

        return rootConfig[Spec.path]?.let { { config: Config -> config.from.yaml.file(Paths.get(it).toAbsolutePath().toFile()) } } ?: { config -> config }
    }

    private fun fromResource(config: Config): Config = config.from.yaml.resource(CONFIGURATION_FILE_RESOURCE_NAME)
}