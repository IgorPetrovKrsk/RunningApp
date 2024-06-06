package igor.petrov.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import igor.petrov.core.database.dao.RunDao
import igor.petrov.core.database.entety.RunEntity

@Database(
    entities = [RunEntity::class],
    version = 1
)
abstract class RunDatabase: RoomDatabase() {

    abstract val runDao: RunDao

}