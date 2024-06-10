package igor.petrov.core.database.entety

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DeletedRunSyncEntity(
    @PrimaryKey(autoGenerate = false)
    val runId: String,
    val userId: String
)

