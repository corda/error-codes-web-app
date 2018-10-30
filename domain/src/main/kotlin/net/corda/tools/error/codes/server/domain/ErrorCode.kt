package net.corda.tools.error.codes.server.domain

import net.corda.tools.error.codes.server.commons.domain.ValidationResult

data class ErrorCode(val value: String) {

    init {
        val errors = errorsForArgs(value)
        require(errors.isEmpty()) { errors.joinToString() }
    }

    companion object {

        private const val EMPTY_VALUE_ERROR = "Error code cannot be empty."
        private const val VALUE_WITH_ILLEGAL_CHARACTERS_ERROR = "Error code can only contain [a-z][0-9]."

        val allowedCharacters: Collection<Char> = ('a'..'z') + ('0'..'9')

        @JvmStatic
        fun errorsForArgs(value: String): Set<String> {

            if (value.isEmpty()) {
                return setOf(EMPTY_VALUE_ERROR)
            }
            if (value.any { !allowedCharacters.contains(it) }) {
                return setOf(VALUE_WITH_ILLEGAL_CHARACTERS_ERROR)
            }
            return emptySet()
        }
    }

    object Valid {

        @JvmStatic
        fun create(value: String): ValidationResult<ErrorCode> {

            val errors = errorsForArgs(value)
            return if (errors.isEmpty()) {
                ValidationResult.valid(ErrorCode(value))
            } else {
                ValidationResult.invalid(errors)
            }
        }
    }
}