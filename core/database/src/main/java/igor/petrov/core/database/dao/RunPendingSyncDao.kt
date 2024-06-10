package igor.petrov.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import igor.petrov.core.database.entety.DeletedRunSyncEntity
import igor.petrov.core.database.entety.RunPendingSyncEntity

@Dao
interface RunPendingSyncDao {

    //Created runs but not uploaded to remote
    @Query("SELECT * FROM runpendingsyncentity WHERE userId=:userId")
    suspend fun getAllRunPendingSyncEntities(userId: String): List<RunPendingSyncEntity>

    @Query("SELECT * FROM runpendingsyncentity WHERE runId=:runId")
    suspend fun getAllRunPendingSyncEntity(runId:String): RunPendingSyncEntity?

    @Upsert
    suspend fun upsertRunPendingSyncEntity(runPendingSyncEntity: RunPendingSyncEntity)

    @Query ("DELETE FROM runpendingsyncentity WHERE runId=:runId")
    suspend fun deleteRunPendingSyncEntity(runId: String)

    //Deleted runs, but not deleted from remote
    @Query ("SELECT * FROM DeletedRunSyncEntity WHERE userId=:userId")
    suspend fun getAllDeletedRunSyncEntities(userId: String): List<DeletedRunSyncEntity>

    @Upsert
    suspend fun upsertDeletedRunSyncEntity (entity: DeletedRunSyncEntity)

    @Query ("DELETE FROM deletedrunsyncentity WHERE runId=:runId")
    suspend fun deleteDeletedRunSyncEntity (runId: String)
}