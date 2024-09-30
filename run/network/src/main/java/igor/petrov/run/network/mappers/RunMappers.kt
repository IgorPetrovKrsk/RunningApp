package igor.petrov.run.network.mappers

import igor.petrov.core.domain.location.Location
import igor.petrov.core.domain.run.Run
import igor.petrov.run.network.RunDto
import java.time.Instant
import java.time.ZoneId
import kotlin.time.Duration.Companion.milliseconds

fun RunDto.toRun(): Run{
    return Run (
        id = id,
        duration = durationMillis.milliseconds,
        dateTimeUtc = Instant.parse(dateTimeUtc)
            .atZone(ZoneId.of("UTC")),
        distanceMeters = distanceMeters,
        location = Location(lat,long),
        maxSpeedKmh = maxSpeedKmh,
        totalElevationMeters = totalElevationMeters,
        mapPictureUrl = mapPictureUrl,
        avgHeartRate = avgHeartRate,
        maxHeartRate = maxHeartRate
    )
}

fun Run.toRunDto(): RunDto{
    return RunDto(
        id = id!!,
        dateTimeUtc = dateTimeUtc.toInstant().toString(),
        durationMillis = duration.inWholeMilliseconds,
        distanceMeters = distanceMeters,
        lat = location.lat,
        long = location.lon,
        avgSpeedKmh = avgSpeedKmh,
        maxSpeedKmh = maxSpeedKmh,
        totalElevationMeters = totalElevationMeters,
        mapPictureUrl = mapPictureUrl,
        avgHeartRate = avgHeartRate,
        maxHeartRate = maxHeartRate
    )
}

fun Run.toCreateRunRequest(): CreateRunRequest{
    return CreateRunRequest(
        id = id!!,
        durationMillis = duration.inWholeMilliseconds,
        distanceMeters = distanceMeters,
        lat = location.lat,
        long = location.lon,
        avgSpeedKmh = avgSpeedKmh,
        maxSpeedKmh = maxSpeedKmh,
        totalElevationMeters = totalElevationMeters,
        epochMillis = dateTimeUtc.toEpochSecond() * 1000L,
        avgHeartRate = avgHeartRate,
        maxHeartRate = avgHeartRate
    )
}