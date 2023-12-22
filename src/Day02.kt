import java.lang.Integer.max

fun main() {

    fun parse(input: String): Pair<Int, Triple<Int, Int, Int>> {
        var red = 0
        var blue = 0
        var green = 0
        val (game, trials) = input.split(":", limit = 2)
        val gameNoIx = game.lastIndexOf(" ")
        val gameNo = game.substring(gameNoIx+1).toInt()
        for (trial in trials.split(";")) {
            val colorPairs = trial.split(",")
            for (colorPair in colorPairs) {
                val (num, color) = colorPair.trim().split("\\s+".toRegex())
                when (color) {
                    "red" -> red = max(red, num.toInt())
                    "blue" -> blue = max(blue, num.toInt())
                    "green" -> green = max(green, num.toInt())
                }
            }
        }
        return gameNo to Triple(red,green,blue)
    }

    fun <A> Triple<A,A,A>.all(predicate: (A) -> Boolean): Boolean {
        return predicate(first) && predicate(second) && predicate(third)
    }

    infix fun Triple<Int, Int, Int>.zip(triple: Triple<Int, Int, Int>): Triple<Pair<Int, Int>, Pair<Int, Int>, Pair<Int, Int>> {
        return Triple(first to triple.first, second to triple.second, third to triple.third)
    }

    infix fun Triple<Int, Int, Int>.lessThan(triple: Triple<Int, Int, Int>): Boolean {
        return zip(triple).all { (a, b) -> a < b }
    }

    infix fun Triple<Int, Int, Int>.lessThanOrEqual(triple: Triple<Int, Int, Int>): Boolean {
        return zip(triple).all { (a, b) -> a <= b }
    }

    fun part1(input: List<String>): Int {
        var sum = 0
        for (value in input) {
            val (num, rgb) = parse(value)
            if (rgb lessThanOrEqual Triple(12, 13, 14)) {
//                num.println()
                sum += num
            }
        }
        return sum
    }

    fun Triple<Int,Int,Int>.power(): Int {
        return first * second * third
    }

    fun part2(input: List<String>): Int {
        var sum = 0
        for (value in input) {
            val (_, rgb) = parse(value)
            val power = rgb.power()
//            power.println()
            sum += power
        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = """
Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
    """.trimIndent().split("\n")

    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

//    val testInputTwo = """
//        two1nine
//        eightwothree
//        abcone2threexyz
//        xtwone3four
//        4nineeightseven2
//        zoneight234
//        7pqrstsixteen
//    """.trimIndent().split("\\s+".toRegex())
//
//    check(part2(testInputTwo) == 281)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
