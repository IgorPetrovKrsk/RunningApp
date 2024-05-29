package igor.petrov.auth.domain

interface PatternValidator {
    fun matches(value: String): Boolean
}