package com.jpmc.ctsweatherapp.util

data class Resource(
    val status: Status,
    val data: Any?,
    val message: String?
) {
    companion object {

        fun success(data: Any?): Resource {
            return Resource(Status.SUCCESS, data, null)
        }

        fun error(msg: String, data: Any?): Resource {
            return Resource(Status.ERROR, data, msg)
        }

        fun loading(data: Any?): Resource{
            return Resource(Status.LOADING, data, null)
        }
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}
