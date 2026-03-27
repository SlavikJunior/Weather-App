package com.github.slavikjunior.weatherapp.data.utils

import kotlinx.serialization.SerialName
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class IdFromPath

fun Any.toQueryMap(): Map<String, String> {
    val map = mutableMapOf<String, String>()

    this::class.memberProperties.forEach { property ->

        if (property.findAnnotation<IdFromPath>() != null) return@forEach

        val value = property.getter.call(this) ?: return@forEach
        val paramName = property.findAnnotation<SerialName>()?.value ?: property.name

        when (value) {
            is Enum<*> -> {
                val serialName = value::class.java
                    .getField(value.name)
                    .getAnnotation(SerialName::class.java)
                map[paramName] = serialName?.value ?: value.name
            }

            is List<*> ->
                map[paramName] = value.joinToString(",")

            is Boolean, is Number, is String ->
                map[paramName] = value.toString()

            else ->
                throw IllegalArgumentException(
                    "Don't know how to serialize field '$paramName' from ${this::class.simpleName}"
                )
        }
    }

    return map
}
