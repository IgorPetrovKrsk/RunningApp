package igor.petrov.test

import igor.petrov.core.domain.location.LocationWithAltitude
import igor.petrov.run.domain.LocationObserver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class LocationObserverFake: LocationObserver {

    private val _locations = MutableSharedFlow<LocationWithAltitude>()

    override fun observerLocation(interval: Long): Flow<LocationWithAltitude> {
        return _locations.asSharedFlow()
    }

    suspend fun trackLocation (location: LocationWithAltitude) {
        _locations.emit(location)
    }
}