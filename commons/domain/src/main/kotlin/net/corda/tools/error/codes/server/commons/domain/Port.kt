package net.corda.tools.error.codes.server.commons.domain

data class Port(val value: Int) {

    init {
        require(value >= 0)
    }
}