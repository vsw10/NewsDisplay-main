package com.husqvarna.newsapp.extras

enum class StatusTypes(private val message: String) {

    SUCCESS("SUCCESS"),
    ERROR("ERROR");

    fun getMessage(): String {
        return message
    }
}