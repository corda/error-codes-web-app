package net.corda.tools.error.codes.server

import com.uchuhimo.konf.Config
import javax.inject.Named

@Named
internal class ConfigLocatingDecorator : (Config) -> Config {

    private companion object {

        private const val CONFIGURATION_FILE_NAME = "configuration.yml"
    }

    override fun invoke(config: Config): Config = config.from.yaml.resource(CONFIGURATION_FILE_NAME).from.env().from.systemProperties()
}