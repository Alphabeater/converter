package converter

enum class Units(val names: List<String>, val value: Double, val type: Char) {
    METER(listOf("meters", "meter", "m"), 1.0, 'd'),
    KILOMETER(listOf("kilometers", "kilometer", "km"), 1000.0, 'd'),
    CENTIMETER(listOf("centimeters", "centimeter", "cm"), 0.01, 'd'),
    MILLIMETER(listOf("millimeters", "millimeter", "mm"), 0.001, 'd'),
    MILE(listOf("miles", "mile", "mi"), 1609.35, 'd'),
    YARD(listOf("yards", "yard", "yd"), 0.9144, 'd'),
    FOOT(listOf("feet", "foot", "ft"), 0.3048, 'd'),
    INCH(listOf("inches", "inch", "in"), 0.0254, 'd'),

    GRAM(listOf("grams", "gram", "g"), 1.0, 'w'),
    KILOGRAM(listOf("kilograms", "kilogram", "kg"), 1000.0, 'w'),
    MILLIGRAM(listOf("milligrams", "milligram", "mg"), 0.001, 'w'),
    POUND(listOf("pounds", "pound", "lb"), 453.592, 'w'),
    OUNCE(listOf("ounces", "ounce", "oz"), 28.3495, 'w'),

    CELCIUS(listOf("degrees celsius", "degree celsius", "celsius", "dc", "c"), 0.0, 't'),
    FAHRENHEIT(listOf("degrees fahrenheit", "degree fahrenheit", "fahrenheit", "df", "f"), 0.0, 't'),
    KELVIN(listOf("kelvins", "kelvin", "k"), 0.0, 't'),

    ERROR(listOf("???"), 0.0, 'e');

    companion object {
        fun returnEnum(input: String): Units {
            for (element in values()) {
                if (input in element.names) return element
            }
            return ERROR
        }
        fun convert(inputValue: Double, inputEnum: Units, outputEnum: Units, inverse: Boolean = false): Double {
            var outputValue = 0.0
            if (inputEnum.type == 'd' || inputEnum.type == 'w') {
                outputValue = if (outputEnum == METER || outputEnum == GRAM) {
                    inputValue * if (!inverse) inputEnum.value else 1.0 / inputEnum.value
                } else {
                    if (inputEnum.type == 'd') convert(inputValue * inputEnum.value, outputEnum, METER, true)
                    else convert(inputValue * inputEnum.value, outputEnum, GRAM, true)
                }
            } else {
                outputValue = when (inputEnum) {
                    CELCIUS -> when (outputEnum) {
                        KELVIN -> inputValue + 273.15
                        FAHRENHEIT -> inputValue * 9 / 5 + 32
                        else -> inputValue
                    }
                    FAHRENHEIT -> when (outputEnum) {
                        KELVIN -> (inputValue + 459.67) * 5 / 9
                        CELCIUS -> (inputValue - 32) * 5 / 9
                        else -> inputValue
                    }
                    KELVIN -> when (outputEnum) {
                        CELCIUS -> inputValue - 273.15
                        FAHRENHEIT -> inputValue * 9 / 5 - 459.67
                        else -> inputValue
                    }
                    else -> -1.0
                }
            }
            return outputValue
        }
    }
}

fun Double.isUnit() = this % 1 == 0.0 && this.toInt() == 1

fun main() {
    loop@do {
        print("Enter what you want to convert (or exit): ")
        try {
            val line = readLine()!!.split(" ")

            if (line[0] == "exit") break@loop

            val inputValue = line[0].toDouble()

            val inputEnum = Units.returnEnum((line[1] +
                if (line[1].contains("degree", true)) {
                    ' ' + line[2]
                } else "").toLowerCase())

            val outputEnum = Units.returnEnum((
                if (line[line.lastIndex - 1].contains("degree", true)) {
                    line[line.lastIndex - 1] + ' '
                } else { "" } + line.last()).toLowerCase())

            if (inputEnum == Units.ERROR || outputEnum == Units.ERROR || inputEnum.type != outputEnum.type) {
                println("Conversion from ${inputEnum.names[0]} to ${outputEnum.names[0]} is impossible\n")
                continue@loop
            } else if (inputValue < 0) {
                if (inputEnum.type == 'd') {
                    println("Length shouldn't be negative\n")
                    continue@loop
                }
                else if (inputEnum.type == 'w') {
                    println("Weight shouldn't be negative\n")
                    continue@loop
                }
            }

            val outputValue = Units.convert(inputValue, inputEnum, outputEnum)
            println("$inputValue ${if (inputValue.isUnit()) inputEnum.names[1] else inputEnum.names[0]} is " +
                    "$outputValue ${if (outputValue.isUnit()) outputEnum.names[1] else outputEnum.names[0]}\n")

        } catch (e: Exception) {
            println("Parse error\n")
        }
    } while (true)
}
