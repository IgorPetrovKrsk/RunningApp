package igor.petrov.core.domain.location

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class Location(
    val lat: Double,
    val lon: Double
){
    fun distanceTo(otherLocation:Location): Float{
        val latDistance = Math.toRadians(otherLocation.lat - lat)
        val lonDistance = Math.toRadians(otherLocation.lon - lon)
        val a = sin(latDistance / 2) * sin(latDistance / 2) +
                cos(Math.toRadians(lat)) * cos(Math.toRadians(otherLocation.lat)) *
                sin(lonDistance / 2) * sin(lonDistance / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return EARTH_RADIUS_METERS * c.toFloat()
    }

    companion object{
        private const val EARTH_RADIUS_METERS = 6_371_000
    }
}
