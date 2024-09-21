package ru.robilko.core_ui.presentation

import androidx.compose.runtime.Immutable

interface Selectable {
    val value: String
    val name: String
}

@Immutable
data class SelectableData(
    override val value: String,
    override val name: String
) : Selectable

fun String.asSelectableData() = SelectableData(value = this, name = this)

fun List<String>.asSelectableList() = this.map { it.asSelectableData() }