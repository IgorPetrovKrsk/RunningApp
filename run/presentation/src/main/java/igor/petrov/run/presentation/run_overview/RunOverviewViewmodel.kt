package igor.petrov.run.presentation.run_overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import igor.petrov.core.domain.run.RunRepository
import igor.petrov.run.presentation.run_overview.mapper.toRunUi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class RunOverviewViewmodel(
    private val runRepository: RunRepository
): ViewModel()
{
    var state by mutableStateOf(RunOverviewState())
        private set

    init {
        runRepository.getRuns().onEach {runs ->
            val runsUi = runs.map {it.toRunUi()}
            state = state.copy(runs = runsUi)
        }.launchIn(viewModelScope)

        viewModelScope.launch {
            runRepository.syncPendingRuns()
            runRepository.fetchRuns()
        }
    }
    fun onAction(action: RunOverviewAction){
        when (action){
            RunOverviewAction.OnLogOutClick -> Unit
            RunOverviewAction.OnStartRunClick -> Unit
            is RunOverviewAction.DeleteRun -> {
                viewModelScope.launch {
                    runRepository.deleteRun(action.runUi.id)
                }
            }
            else -> Unit
        }
    }

}