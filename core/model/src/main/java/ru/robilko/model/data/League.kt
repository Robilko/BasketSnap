package ru.robilko.model.data

data class League(
    val id: Int,
    val name: String,
    val type: String,
    val logoUrl: String,
    val country: Country,
    val seasons: List<Season>
)
