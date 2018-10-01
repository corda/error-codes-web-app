package net.corda.tools.error.codes.server.repository.error.codes.properties

import net.corda.tools.error.codes.server.domain.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.net.URI

internal class PropertiesErrorDescriptionsRepositoryTest {

    @Test
    fun property_entries_are_mapped_to_descriptions() {

        val errorCode = ErrorCode("1uoda")
        val invocationContext = InvocationContext.newInstance()

        val description1 = ErrorDescription(ErrorDescriptionLocation.External(URI.create("https//a.com")), ErrorCoordinates(errorCode, ReleaseVersion(3, 2, 1), PlatformEdition.Enterprise))
        val description2 = ErrorDescription(ErrorDescriptionLocation.External(URI.create("https//b.com")), ErrorCoordinates(errorCode, ReleaseVersion(1, 3, 2), PlatformEdition.OpenSource))
        val descriptions: Flux<out ErrorDescription> = Flux.just(description1, description2)

        val repository = PropertiesErrorDescriptionsRepository { listOf(errorCode to descriptions).toProperties() }
        repository.start()

        val returned = repository[errorCode, invocationContext]

        assertThat(returned.collectList().block()).isEqualTo(listOf(description1, description2))
    }

    @Test
    fun empty_properties_results_in_no_descriptions() {

        val errorCode = ErrorCode("1uoda")
        val invocationContext = InvocationContext.newInstance()
        val descriptions: Flux<out ErrorDescription> = Flux.empty()

        val repository = PropertiesErrorDescriptionsRepository { listOf(errorCode to descriptions).toProperties() }
        repository.start()

        val returned = repository[errorCode, invocationContext]

        assertThat(returned.collectList().switchIfEmpty(Mono.empty()).block()).isEqualTo(descriptions.collectList().block())
    }
}