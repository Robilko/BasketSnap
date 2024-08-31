package ru.robilko.remote.util

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive

fun JsonElement.asString(): String? {
    return when {
        jsonPrimitive.isString -> jsonPrimitive.content
         else -> jsonPrimitive.intOrNull?.toString()
    }
}