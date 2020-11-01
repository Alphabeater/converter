package converter
import java.util.*

val s = Scanner(System.`in`)

fun main() {
    print("Enter a number of kilometers: ")
    val distance = s.nextInt()
    println("$distance kilometers is ${distance * 1000} meters")
}
