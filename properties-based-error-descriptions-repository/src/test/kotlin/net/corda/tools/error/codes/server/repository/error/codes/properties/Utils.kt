package net.corda.tools.error.codes.server.repository.error.codes.properties

import net.corda.tools.error.codes.server.domain.ErrorCode
import net.corda.tools.error.codes.server.domain.ErrorDescription
import net.corda.tools.error.codes.server.domain.ErrorDescriptionLocation
import net.corda.tools.error.codes.server.domain.PlatformEdition
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

internal fun Iterable<Pair<ErrorCode, Flux<out ErrorDescription>>>.toProperties(): Properties {

    return map { pair -> pair.toEntry() }.fold(Properties()) { props, entry ->
        props[entry.first] = entry.second
        props
    }
}

internal fun Pair<ErrorCode, Flux<out ErrorDescription>>.toEntry(): Pair<String, String> {

    return first.value to serialise(second.collectList().switchIfEmpty(Mono.empty()).block()!!)
}

internal fun serialise(descriptions: List<ErrorDescription>): String {

    return descriptions.joinToString("|", transform = ::serialise)
}

internal fun serialise(description: ErrorDescription): String {

    val serialised = StringBuilder()
    val location = description.location
    when (location) {
        is ErrorDescriptionLocation.External -> serialised.append(location.uri.toASCIIString())
    }
    serialised.append(",")
    when (description.coordinates.platformEdition) {
        is PlatformEdition.OpenSource -> serialised.append("OS")
        else -> serialised.append("ENT")
    }
    serialised.append(",")
    description.coordinates.releaseVersion.apply {
        serialised.append("$major.$minor.$patch")
    }
    return serialised.toString()
}