package igor.petrov.run.location

import android.location.Location
import igor.petrov.core.domain.location.LocationWithAltitude

fun Location.toLocationWithAltitude(): LocationWithAltitude {
    return LocationWithAltitude(
        location = igor.petrov.core.domain.location.Location(
            lat = latitude,
            lon = longitude
        ),
        altitude = altitude
    )
}

