package igor.petrov.run.presentation.run_overview.mapper

import igor.petrov.core.domain.run.Run
import igor.petrov.core.presentation.ui.formatted
import igor.petrov.core.presentation.ui.toFormattedKm
import igor.petrov.core.presentation.ui.toFormattedKmh
import igor.petrov.core.presentation.ui.toFormattedMeters
import igor.petrov.core.presentation.ui.toFormattedPace
import igor.petrov.run.presentation.run_overview.model.RunUi
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Run.toRunUi(): RunUi{
    val dateTimeInLocalTime = dateTimeUtc
        .withZoneSameInstant(ZoneId.systemDefault())
    val formattedDateTime = DateTimeFormatter
        .ofPattern("MMM dd, yyyy - hh:mma")
        .format(dateTimeInLocalTime)

    val distanceKm = distanceMeters / 1000.0
    return RunUi(
        id = id!!,
        duration = duration.formatted(),
        dateTime = formattedDateTime,
        distance = distanceKm.toFormattedKm(),
        avgSpeed = avgSpeedKmh.toFormattedKmh(),
        maxSpeed = maxSpeedKmh.toFormattedKmh(),
        pace = duration.toFormattedPace(distanceKm),
        totalElevation = totalElevationMeters.toFormattedMeters(),
        mapPictureUrl = mapPictureUrl

    )
}