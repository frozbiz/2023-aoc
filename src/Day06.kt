

fun main() {

    fun parse(input: String): Pair<String, List<Int>> {
        val (label, valueString) = input.split(":", limit = 2)
        val values = valueString.trim().split("\\s+".toRegex()).map(String::toInt)
        return label to values
    }

    fun parse2(input: String): Pair<String, Long> {
        val (label, valueString) = input.split(":", limit = 2)
        val value = valueString.split("\\s+".toRegex()).joinToString("").toLong()
        return label to value
    }

    fun Int.distances(): List<Long> = (1..<this).map { it.toLong() * (this - it) }

    fun Long.distances(): List<Long> = (1..<this).map { it.toLong() * (this - it) }

    fun part1(input: List<String>): Int {
        val (times, distances) = input.map { parse(it).second }
        var product = 1
        for ((time, distance) in times zip distances) {
            val wins = time.distances().filter { it > distance }
//            wins.println()
            product *= wins.size
        }
        return product
    }

    fun part2(input: List<String>): Int {
        val (time, distance) = input.map { parse2(it).second }
        val wins = time.distances().filter { it > distance }
//            wins.println()
        return wins.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = """
Time:      7  15   30
Distance:  9  40  200
    """.trimIndent().split("\n")

    check(part1(testInput) == 288)
    check(part2(testInput) == 71503)


    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
