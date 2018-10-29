package net.corda.tools.error.codes.server.endpoints.location

import net.corda.tools.error.codes.server.domain.ErrorCoordinates

internal class ErrorLocationContractTest : ErrorLocationContractTestTemplate() {

    // This should stay hard-coded, rather than read from the actual configuration, to avoid breaking the contract without breaking the test.
    override fun path(coordinates: ErrorCoordinates): String = "/editions/${coordinates.platformEdition.description}/releases/${coordinates.releaseVersion.description()}/errors/${coordinates.code.value}"
}

internal class ShortErrorLocationContractTest : ErrorLocationContractTestTemplate() {

    // This should stay hard-coded, rather than read from the actual configuration, to avoid breaking the contract without breaking the test.
    override fun path(coordinates: ErrorCoordinates): String = "/${coordinates.platformEdition.description}/${coordinates.releaseVersion.description()}/${coordinates.code.value}"
}