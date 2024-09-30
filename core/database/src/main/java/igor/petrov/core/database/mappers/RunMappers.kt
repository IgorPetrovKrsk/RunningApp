package igor.petrov.core.database.mappers

import igor.petrov.core.database.entety.RunEntity
import igor.petrov.core.domain.location.Location
import igor.petrov.core.domain.run.Run
import org.bson.types.ObjectId
import java.time.Instant
import java.time.ZoneId
import kotlin.time.Duration.Companion.milliseconds

fun RunEntity.toRun(): Run{
    return Run(
        id = id,
        duration = durationMillis.milliseconds,
        dateTimeUtc = Instant.parse(dateTimeUtc)
            .atZone(ZoneId.of("UTC")).withFixedOffsetZone(),
        distanceMeters = distanceMeters,
        location = Location(
            lat = latitude,
            lon = longitude
        ),
        maxSpeedKmh = maxSpeedKmh,
        totalElevationMeters = totalElevationMeters,
        mapPictureUrl = mapPictureUrl,
        avgHeartRate = avgHeartRate,
        maxHeartRate = maxHeartRate
    )
}

fun Run.toRunEntity(): RunEntity{
    return RunEntity(
        id = id?: ObjectId().toHexString(),
        durationMillis = duration.inWholeMilliseconds,
        maxSpeedKmh = maxSpeedKmh,
        dateTimeUtc = dateTimeUtc.toInstant().toString(),
        latitude = location.lat,
        longitude = location.lon,
        distanceMeters = distanceMeters,
        avgSpeedKmh = avgSpeedKmh,
        totalElevationMeters = totalElevationMeters,
        mapPictureUrl = mapPictureUrl,
        avgHeartRate = avgHeartRate,
        maxHeartRate = maxHeartRate
    )
}