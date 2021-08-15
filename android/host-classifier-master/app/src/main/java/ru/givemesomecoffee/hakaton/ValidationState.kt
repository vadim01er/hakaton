package ru.givemesomecoffee.hakaton


data class ValidationState constructor(
    val status: Status,
    val msg: String? = null,
    val host: String? = null
) {
    companion object {
        val READY = ValidationState(Status.READY)
        fun host(host: String?) = ValidationState(Status.VALIDATED, host = host)
        fun error(msg: String?) = ValidationState(Status.FAILED, msg = msg)
    }

    enum class Status {
        READY,
        VALIDATED,
        FAILED
    }
}
