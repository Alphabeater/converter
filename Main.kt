package converter
import java.util.*

val s = Scanner(System.`in`)

fun convertToMeters(number: Double, unit: String): Pair<Double, String> {
    var returnedPair = when (unit) {
            "m", "meter", "meters" -> Pair(number, "meters")
            "km", "kilometer", "kilometers" -> Pair(number * 1000, "kilometers")
            "cm", "centimeter", "centimeters" -> Pair(number * 0.01, "centimeters")
            "mm", "millimeter", "millimeters" -> Pair(number * 0.001, "millimeters")
            "mi", "mile", "miles" -> Pair(number * 1609.35, "miles")
            "yd", "yard", "yards" -> Pair(number * 0.9144, "yards")
            "ft", "foot", "feet" -> Pair(number * 0.3048, "feet")
            "in", "inch", "inches" -> Pair(number * 0.0254, "inches")
            else -> Pair(number * 0, "invalid")
        }
    return if (fixUnit(number)) {
        Pair(returnedPair.first, returnedPair.second.dropLast(1))
    }
    else returnedPair
}

fun fixUnit(number: Double) = number % 1 == 0.0 && number.toInt() == 1

fun main() {
    print("Enter a number and a measure of length: ")
    val number = s.nextDouble()
    val unit = s.next().toLowerCase()
    val (convertedNumber, returnedUnit) = convertToMeters(number, unit)
    val convertedUnit = if (fixUnit(convertedNumber)) "meter" else "meters"

    println("$number $returnedUnit is $convertedNumber $convertedUnit")
}
