package converter
import java.util.*

val s = Scanner(System.`in`)

val distance = listOf("meters", "meter", "m", "kilometer", "kilometers", "km",
    "centimeters", "centimeter", "cm", "millimeters", "millimeter", "mm",
    "miles", "mile", "mi", "yards", "yard", "yd", "feet", "foot", "ft", "inches", "inch", "in")
val distanceValue = listOf(1.0, 1000.0, 0.01, 0.001, 1609.35, 0.9144, 0.3048, 0.0254)
val weight = listOf("grams", "gram", "g", "kilograms", "kilogram", "kg",
    "milligrams", "milligram", "mg", "pounds", "pound", "lb", "ounces", "ounce", "oz")
val weightValue = listOf(1.0, 1000.0, 0.001, 453.592, 28.3495)

data class Data(var inputValue: Double,
                var inputUnit: String,
                var outputValue: Double,
                var outputUnit: String)

fun convertUnit(inputValue: Double, inputUnit: String, outputUnit: String, inverse: Boolean = false): Data {
    var iUnit = inputUnit
    var oUnit = outputUnit
    var outputValue = 0.0
    when (inputUnit) {
        in distance -> {
            val indexIn = distance.indexOf(inputUnit) / 3
            val indexOut = distance.indexOf(outputUnit) / 3
            outputValue = inputValue * if (!inverse) distanceValue[indexIn] else (1.0 / distanceValue[indexOut])
            iUnit = if (inputValue.isUnit()) distance[indexIn * 3 + 1] else distance[indexIn * 3]
            oUnit = if (outputValue.isUnit()) distance[indexOut * 3 + 1] else distance[indexOut * 3]
        }
        in weight -> {
            val indexIn = weight.indexOf(inputUnit) / 3
            val indexOut = weight.indexOf(outputUnit) / 3
            outputValue = inputValue * if (!inverse) weightValue[indexIn] else 1 / weightValue[indexOut]
            iUnit = if (inputValue.isUnit()) weight[indexIn * 3 + 1] else weight[indexIn * 3]
            oUnit = if (outputValue.isUnit()) weight[indexOut * 3 + 1] else weight[indexOut * 3]
        }
    }
    return Data(inputValue, iUnit, outputValue, oUnit)
}

fun Double.isUnit() = this % 1 == 0.0 && this.toInt() == 1
fun String.isMeter() = distance.subList(0, 3).contains(this)
fun String.isGrams() = weight.subList(0, 3).contains(this)

fun main() {
    loop@do {
        print("Enter what you want to convert (or exit): ")
        try {
            val inputValue = s.next()
            if (inputValue == "exit") break@loop
            val inputUnit = s.next().toLowerCase()
            s.next()
            val outputUnit = s.next().toLowerCase()

            if (outputUnit.isMeter() || outputUnit.isGrams()) {
                convertUnit(inputValue.toDouble(), inputUnit, outputUnit).let {
                    println("${it.inputValue} ${it.inputUnit} is ${it.outputValue} ${it.outputUnit}")
                }
            } else {
                convertUnit(inputValue.toDouble(), inputUnit, "meters").let {
                    convertUnit(it.outputValue, it.outputUnit, outputUnit, true). let{ it2 ->
                        println("${it.inputValue} ${it.inputUnit} is ${it2.outputValue} ${it2.outputUnit}")
                    }
                }
            }
        } catch (e: Exception) {
            println(e.message)
            break@loop
        }
    } while (true)
}
