package ru.robilko.remote.util

import kotlinx.serialization.json.JsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Test

class UtilsTest {
    @Test
    fun `json element string as string test`() {
        val value = "2022"
        val actual = JsonPrimitive(value).asString()
        val expected = "2022"
        assertEquals(expected, actual)
    }

    @Test
    fun `json element int as string test`() {
        val value = 2022
        val actual = JsonPrimitive(value).asString()
        val expected = "2022"
        assertEquals(expected, actual)
    }

    @Test
    fun `json element null as null test`() {
        val nullableString: String? = null
        val actual = JsonPrimitive(nullableString).asString()
        val expected = null
        assertEquals(expected, actual)
    }
}