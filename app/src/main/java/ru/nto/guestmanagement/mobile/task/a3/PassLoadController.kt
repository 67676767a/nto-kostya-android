package ru.nto.guestmanagement.mobile.task.a3

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.nto.guestmanagement.mobile.data.PassRepository
import ru.nto.guestmanagement.mobile.model.PassScreenState

class PassLoadController(
    private val repository: PassRepository,
    private val scope: CoroutineScope
) {
    private val _state = MutableStateFlow<PassScreenState>(PassScreenState.InitialLoading)
    val state: StateFlow<PassScreenState> = _state.asStateFlow()

    private var hasStarted = false
    private var currentJob: Job? = null

    private val isBusy: Boolean
        get() = currentJob?.isActive == true

    fun start() {
        if (hasStarted || isBusy) return
        if (_state.value !is PassScreenState.InitialLoading) return

        hasStarted = true
        executeInitialLoad()
    }

    fun retry() {
        if (isBusy) return
        if (_state.value !is PassScreenState.InitialError) return

        _state.value = PassScreenState.InitialLoading
        executeInitialLoad()
    }

    fun refresh() {
        if (isBusy) return
        val current = _state.value as? PassScreenState.Content ?: return

        _state.value = current.copy(isRefreshing = true)
        currentJob = scope.launch {
            repository.load()
                .onSuccess { newData ->
                    _state.value = PassScreenState.Content(
                        data = newData,
                        isRefreshing = false,
                        isOffline = false
                    )
                }
                .onFailure {
                    _state.value = current.copy(
                        isRefreshing = false,
                        isOffline = true
                    )
                }
        }
    }

    private fun executeInitialLoad() {
        currentJob = scope.launch {
            repository.load()
                .onSuccess { data ->
                    _state.value = PassScreenState.Content(
                        data = data,
                        isRefreshing = false,
                        isOffline = false
                    )
                }
                .onFailure { error ->
                    val message = error.message?.takeIf { it.isNotBlank() }
                        ?: "Не удалось загрузить пропуск"
                    _state.value = PassScreenState.InitialError(message)
                }
        }
    }
}
