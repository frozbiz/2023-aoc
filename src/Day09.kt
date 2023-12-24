import kotlin.math.pow
fun main() {

    fun parse(input: String): List<Int> = input.trim().split("\\s+".toRegex()).map(String::toInt)

    fun Iterable<Int>.differenceArray(): List<Int> {
        var previous = first()
        val ret = mutableListOf<Int>()
        for (value in this.drop(1)) {
            ret.add(value - previous)
            previous = value
        }
        return ret
    }

    fun buildPredictionStack(sequence: List<Int>): List<List<Int>> {
        val predictionStack = mutableListOf(
            sequence
        )

        while (!predictionStack.last().all { it == 0 }) {
            predictionStack.add(predictionStack.last().differenceArray())
        }

        return predictionStack
    }

    fun predict(sequence: List<Int>): Int {
        val predictionStack = buildPredictionStack(sequence).toMutableList()

        var last = predictionStack.removeLast().last()
        while (predictionStack.isNotEmpty()) {
            last += predictionStack.removeLast().last()
        }
        return last
    }

    fun part1(input: List<String>): Int {
        var sum = 0
        for (value in input) {
            val sequence = parse(value)
            sum += predict(sequence)
        }
        return sum
    }

    fun predictBackwards(sequence: List<Int>): Int {
        val predictionStack = buildPredictionStack(sequence).toMutableList()

        var first = predictionStack.removeLast().first()
        while (predictionStack.isNotEmpty()) {
            first = predictionStack.removeLast().first() - first
        }
        return first
    }

    fun part2(input: List<String>): Int {
        var sum = 0
        for (value in input) {
            val sequence = parse(value)
            sum += predictBackwards(sequence)
        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = """
0 3 6 9 12 15
1 3 6 10 15 21
10 13 16 21 30 45
    """.trimIndent().split("\n")

    check(part1(testInput) == 114)
    check(part2(testInput) == 2)


    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
