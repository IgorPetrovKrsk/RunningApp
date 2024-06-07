package igor.petrov.run.domain

import igor.petrov.core.domain.location.LocationTimestamp
import kotlin.math.roundToInt
import kotlin.time.DurationUnit

object LocationDataCalculator {
    fun getTotalDistanceMeters(locations: List<List<LocationTimestamp>>): Int {
        return locations
            .sumOf { timestampsPerLine ->
                timestampsPerLine.zipWithNext { location1, location2 ->
                    location1.location.location.distanceTo(location2.location.location)
                }.sum().roundToInt()
            }
    }

    fun getMaxSpeedKmh(locations: List<List<LocationTimestamp>>): Double {
        return locations.maxOf { locations ->
            locations.zipWithNext { location1, location2 ->
                val distance = location1.location.location.distanceTo(location2.location.location)
                val hoursDifference = (location2.durationTimestamp - location2.durationTimestamp).toDouble(DurationUnit.HOURS)
                if (hoursDifference == 0.0) {
                    0.0
                } else {
                    (distance / 1000.0) / hoursDifference
                }
            }.maxOrNull() ?: 0.0
        }
    }

    fun getTotalElevationMeters(locations: List<List<LocationTimestamp>>): Int {
        return locations.sumOf {locations ->
            locations.zipWithNext { location1, location2 ->
                val altitude1 = location1.location.altitude
                val altitude2 = location2.location.altitude
                (altitude2 - altitude1).coerceAtLeast(0.0)
            }.sum().roundToInt()
        }

    }


}