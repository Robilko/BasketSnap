package ru.robilko.base.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import ru.robilko.base.util.Response

abstract class FlowUseCase<in P, R> {
    suspend operator fun invoke(parameters: P? = null): Flow<Response<R>> = execute(parameters)
        .onStart { emit(Response.Loading) }

    protected abstract suspend fun execute(parameters: P?): Flow<Response<R>>
}