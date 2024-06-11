package igor.petrov.run.data

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.await
import igor.petrov.core.database.dao.RunPendingSyncDao
import igor.petrov.core.database.entety.DeletedRunSyncEntity
import igor.petrov.core.database.entety.RunPendingSyncEntity
import igor.petrov.core.database.mappers.toRunEntity
import igor.petrov.core.domain.run.Run
import igor.petrov.core.domain.run.RunId
import igor.petrov.core.domain.run.SyncRunScheduler
import igor.petrov.core.domain.util.SessionStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.toJavaDuration

class SyncRunWorkerScheduler(
    private val context: Context,
    private val pendingSyncDao: RunPendingSyncDao,
    private val sessionStorage: SessionStorage,
    private val applicationScope: CoroutineScope
): SyncRunScheduler {

    private val workManager = WorkManager.getInstance(context)
    override suspend fun scheduleSync(type: SyncRunScheduler.SyncType) {
        when (type) {
            is SyncRunScheduler.SyncType.FetchRuns -> scheduleFetchRunsWorker(type.interval)
            is SyncRunScheduler.SyncType.CreateRun -> scheduleCreateRunWorker(
                run = type.run,
                mapPictureBytes = type.mapPictureBytes
            )
            is SyncRunScheduler.SyncType.DeleteRun -> scheduleDeleteRunWorker(type.runId)
        }
    }

    private suspend fun scheduleDeleteRunWorker(runId: RunId){
        val userId = sessionStorage.get()?.userId?: return
        val deletedRun = DeletedRunSyncEntity(
            runId = runId,
            userId = userId
        )
        pendingSyncDao.upsertDeletedRunSyncEntity(deletedRun)
        val workRequest = OneTimeWorkRequestBuilder<DeleteRunWorker>()
            .addTag(DELETE_WORK)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 2000L,
                timeUnit = TimeUnit.MILLISECONDS
            )
            .setInputData(
                Data.Builder()
                    .putString(DeleteRunWorker.RUN_ID, deletedRun.runId)
                    .build()
            )
            .build()
        applicationScope.launch {
            workManager.enqueue(workRequest).await()
        }.join()
    }

    private suspend fun scheduleCreateRunWorker(run: Run, mapPictureBytes: ByteArray){
        val userId = sessionStorage.get()?.userId?: return
        val pendingRun = RunPendingSyncEntity(
            run = run.toRunEntity(),
            mapPictureBytes = mapPictureBytes,
            userId = userId
        )
        pendingSyncDao.upsertRunPendingSyncEntity(pendingRun)
        val workRequest = OneTimeWorkRequestBuilder<CreateRunWorker>()
            .addTag(CREATE_WORK)
            .setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 2000L,
                timeUnit = TimeUnit.MILLISECONDS
            )
            .setInputData(
                Data.Builder()
                    .putString(CreateRunWorker.RUN_ID, pendingRun.runId)
                    .build()
            )
            .build()
        applicationScope.launch {
            workManager.enqueue(workRequest).await()
        }.join()
    }

    private suspend fun scheduleFetchRunsWorker(interval: Duration){
        val isSyncScheduled = withContext(Dispatchers.IO){
            workManager
                .getWorkInfosByTag(SYNC_WORK)
                .get()
                .isNotEmpty()
        }
        if (isSyncScheduled){
            return
        }

        val workRequest = PeriodicWorkRequestBuilder<FetchRunsWorker>(
            repeatInterval = interval.toJavaDuration()
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 2000L,
                timeUnit = TimeUnit.MILLISECONDS
            )
            .setInitialDelay(
                duration = 30,
                timeUnit = TimeUnit.MINUTES
            )
            .addTag(SYNC_WORK)
            .build()

        workManager.enqueue(workRequest).await()
    }

    override suspend fun cancelAllSyncs() {
        WorkManager.getInstance(context)
            .cancelAllWork()
            .await()
    }

    companion object{
        private const val SYNC_WORK = "sync_work"
        private const val CREATE_WORK = "create_work"
        private const val DELETE_WORK = "delete_work"
    }
}