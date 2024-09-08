package ru.robilko.remote.data.model

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class SeasonDtoTest {
    @Test
    fun `json with season int as season dto test`() {
        val json = "{\"season\":2021,\"start\":\"2021-02-11\",\"end\":\"2021-06-03\"}"
        val actual = Json.decodeFromString<SeasonDto>(json)
        val expected = SeasonDto(
            season = JsonPrimitive(2021),
            start = "2021-02-11",
            end = "2021-06-03"
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `json with season string as season dto test`() {
        val json = "{\"season\":\"2021\",\"start\":\"2021-02-11\",\"end\":\"2021-06-03\"}"
        val actual = Json.decodeFromString<SeasonDto>(json)
        val expected = SeasonDto(
            season = JsonPrimitive("2021"),
            start = "2021-02-11",
            end = "2021-06-03"
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `json with season int not equals season dto with string season test`() {
        val json = "{\"season\":2021,\"start\":\"2021-02-11\",\"end\":\"2021-06-03\"}"
        val actual = Json.decodeFromString<SeasonDto>(json)
        val expected = SeasonDto(
            season = JsonPrimitive("2021"),
            start = "2021-02-11",
            end = "2021-06-03"
        )
        assertNotEquals(expected, actual)
    }
}