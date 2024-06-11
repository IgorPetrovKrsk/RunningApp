package igor.petrov.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import igor.petrov.core.database.dao.AnalyticsDao
import igor.petrov.core.database.dao.RunDao
import igor.petrov.core.database.dao.RunPendingSyncDao
import igor.petrov.core.database.entety.DeletedRunSyncEntity
import igor.petrov.core.database.entety.RunEntity
import igor.petrov.core.database.entety.RunPendingSyncEntity

@Database(
    entities = [
        RunEntity::class,
        RunPendingSyncEntity::class,
        DeletedRunSyncEntity::class
    ],
    version = 1
)
abstract class RunDatabase : RoomDatabase() {
    abstract val runDao: RunDao
    abstract val runPendingSyncDao: RunPendingSyncDao
    abstract val analyticsDao: AnalyticsDao
}