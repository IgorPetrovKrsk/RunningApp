package igor.petrov.run.data.di

import igor.petrov.core.domain.run.SyncRunScheduler
import igor.petrov.run.data.CreateRunWorker
import igor.petrov.run.data.DeleteRunWorker
import igor.petrov.run.data.FetchRunsWorker
import igor.petrov.run.data.SyncRunWorkerScheduler
import igor.petrov.run.data.connectivity.PhoneToWatchConnector
import igor.petrov.run.domain.WatchConnector
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val runDataModule = module {
    workerOf(::CreateRunWorker)
    workerOf(::FetchRunsWorker)
    workerOf(::DeleteRunWorker)

    singleOf(::SyncRunWorkerScheduler).bind<SyncRunScheduler>()
    singleOf(::PhoneToWatchConnector).bind<WatchConnector>()
}