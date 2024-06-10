package igor.petrov.run.presentation.run_overview

import igor.petrov.run.presentation.run_overview.model.RunUi

data class RunOverviewState(
    val runs: List<RunUi> = emptyList()
)
