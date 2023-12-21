import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

fun main() {

    fun String.toNumber(): Int{
        return first { it.isDigit() }.digitToInt() * 10 + last { it.isDigit() }.digitToInt()
    }

    val numbers = listOf(
        "0",
        "1",
        "2",
        "3",
        "4",
        "5",
        "6",
        "7",
        "8",
        "9",
        "zero",
        "one",
        "two",
        "three",
        "four",
        "five",
        "six",
        "seven",
        "eight",
        "nine"
    )
    fun String.value(): Int {
        return when (this) {
            "0", "zero" -> 0
            "1", "one" -> 1
            "2", "two" -> 2
            "3", "three" -> 3
            "4", "four" -> 4
            "5", "five" -> 5
            "6", "six" -> 6
            "7", "seven" -> 7
            "8", "eight" -> 8
            "9", "nine" -> 9
            else -> throw IllegalArgumentException()
        }
    }
    fun String.toNumber2(): Int{
        val firstDigit = findAnyOf(numbers)?.second?.value() ?: throw IllegalStateException()
        val lastDigit = findLastAnyOf(numbers)?.second?.value() ?: throw IllegalStateException()
        return firstDigit * 10 + lastDigit
    }

    fun part1(input: List<String>): Int {
        var sum = 0
        for (value in input) {
            val num = value.toNumber()
//            num.println()
            sum += num
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        var sum = 0
        for (value in input) {
            val num = value.toNumber2()
//            num.println()
            sum += num
        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = """
        1abc2
        pqr3stu8vwx
        a1b2c3d4e5f
        treb7uchet
    """.trimIndent().split("\\s+".toRegex())

    check(part1(testInput) == 142)

    val testInputTwo = """
        two1nine
        eightwothree
        abcone2threexyz
        xtwone3four
        4nineeightseven2
        zoneight234
        7pqrstsixteen
    """.trimIndent().split("\\s+".toRegex())

    check(part2(testInputTwo) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
