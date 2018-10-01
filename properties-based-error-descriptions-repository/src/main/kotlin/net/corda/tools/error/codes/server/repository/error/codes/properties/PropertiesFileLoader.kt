package net.corda.tools.error.codes.server.repository.error.codes.properties

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.ConfigSpec
import net.corda.tools.error.codes.server.domain.annotations.Adapter
import net.corda.tools.error.codes.server.domain.loggerFor
import org.springframework.core.io.ResourceLoader
import java.io.InputStream
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@Adapter
@Named
internal class PropertiesFileLoader @Inject constructor(private val configuration: Configuration) {

    internal fun load(): Properties {

        val properties = Properties()
        configuration.openProperties().use(properties::load)
        return properties
    }

    interface Configuration {

        fun openProperties(): InputStream
    }
}

// This allows injecting functions instead of types.
@Adapter
@Named
internal class LoadedProperties @Inject constructor(private val loader: PropertiesFileLoader) : () -> Properties {

    override fun invoke() = loader.load()
}

@Adapter
@Named
internal class PropertiesFileLoaderConfiguration @Inject constructor(applyConfigStandards: (Config) -> Config, private val resourceLoader: ResourceLoader) : PropertiesFileLoader.Configuration {

    private companion object {

        private val logger = loggerFor<PropertiesFileLoaderConfiguration>()

        private const val CONFIGURATION_SECTION_PATH = "configuration.adapters.repositories.properties.file_based"

        private object Spec : ConfigSpec(CONFIGURATION_SECTION_PATH) {

            val properties_file_path by optional<String?>(null)
            val properties_file_resource_name by required<String>()
        }
    }

    private val config = applyConfigStandards.invoke(Config { addSpec(Spec) })

    override fun openProperties(): InputStream {

        return (resourceFromFile() ?: bundledResource()).let {

            resourceLoader.getResource(it).inputStream
        }
    }

    private fun resourceFromFile(): String? = config[Spec.properties_file_path]?.also { logger.info("Loading error code locations from path \"$it\"") }

    private fun bundledResource(): String = config[Spec.properties_file_resource_name].also { logger.info("Loading error code locations from resource \"$it\"") }
}