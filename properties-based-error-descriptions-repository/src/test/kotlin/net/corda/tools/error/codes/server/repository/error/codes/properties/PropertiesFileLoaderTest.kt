package net.corda.tools.error.codes.server.repository.error.codes.properties

import net.corda.tools.error.codes.server.domain.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import java.io.File
import java.io.InputStream
import java.net.URI
import java.nio.file.Files
import java.util.*

internal class PropertiesFileLoaderTest {

    private val temporaryFile: File = Files.createTempFile("error_code", ".properties").toFile().also(File::deleteOnExit)

    @Test
    fun empty_file_results_in_no_descriptions() {

        val errorCode = ErrorCode("1uoda")
        val descriptions: Flux<out ErrorDescription> = Flux.empty()

        val properties = listOf(errorCode to descriptions).toProperties().also { it.writeToFile(temporaryFile) }
        val loader = loaderFrom(temporaryFile)

        val loadedProperties = loader.load()

        assertThat(loadedProperties).isEqualTo(properties)
    }

    @Test
    fun file_with_one_line_results_in_one_description() {

        val errorCode = ErrorCode("1uoda")
        val description1 = ErrorDescription(ErrorDescriptionLocation.External(URI.create("https//a.com")), ErrorCoordinates(errorCode, ReleaseVersion(3, 2, 1), PlatformEdition.Enterprise))
        val description2 = ErrorDescription(ErrorDescriptionLocation.External(URI.create("https//b.com")), ErrorCoordinates(errorCode, ReleaseVersion(1, 3, 2), PlatformEdition.OpenSource))
        val descriptions: Flux<out ErrorDescription> = Flux.just(description1, description2)

        val properties = listOf(errorCode to descriptions).toProperties().also { it.writeToFile(temporaryFile) }
        val loader = loaderFrom(temporaryFile)

        val loadedProperties = loader.load()

        assertThat(loadedProperties).isEqualTo(properties)
    }

    private fun loaderFrom(file: File): PropertiesFileLoader {

        return PropertiesFileLoader(object : PropertiesFileLoader.Configuration {

            override fun openProperties(): InputStream = file.inputStream()
        })
    }

    private fun Properties.writeToFile(file: File) {

        file.outputStream().use {
            store(it, null)
        }
    }
}