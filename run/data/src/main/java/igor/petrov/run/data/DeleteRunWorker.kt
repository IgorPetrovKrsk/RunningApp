package igor.petrov.run.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import igor.petrov.core.database.dao.RunPendingSyncDao
import igor.petrov.core.domain.run.RemoteRunDataSource
import igor.petrov.core.domain.util.Result

class DeleteRunWorker(
    context: Context,
    private val parameters: WorkerParameters,
    private val remoteRunDataSource: RemoteRunDataSource,
    private val pendingSyncDao: RunPendingSyncDao
): CoroutineWorker(context,parameters) {
    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5){
            return Result.failure()
        }
        val runId = parameters.inputData.getString(RUN_ID)?: return Result.failure()
        return when (val result = remoteRunDataSource.deleteRun(runId)){
            is igor.petrov.core.domain.util.Result.Error -> {
                result.error.toWorkerResult()
            }
            is igor.petrov.core.domain.util.Result.Success -> {
                pendingSyncDao.deleteDeletedRunSyncEntity(runId)
                Result.success()
            }
        }
    }

    companion object{
        const val RUN_ID = "RUN_ID"
    }
}
