package ru.robilko.core_ui.presentation

sealed class DataState {
    data object Loading : DataState()
    data object Success : DataState()
    data class Error(val message: String, val onRetryAction: () -> Unit) : DataState()
}