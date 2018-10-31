package net.corda.tools.error.codes.server.application

data class RevisionId(val value: String) {

    init {
        require(value.isNotBlank())
    }
}