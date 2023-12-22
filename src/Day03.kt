
fun main() {

    data class NumberLine(
        val row: Int,
        val colStart: Int,
        val colEnd: Int,
        val value: Int
    ) {
        val line: Set<Point> = Point(colStart, row) to Point(colEnd, row)
    }

    fun buildGrid1(input: List<String>): Grid<Boolean> {
        val grid = BooleanGrid()
        for ((row, line) in input.withIndex()) {
            for ((col, char) in line.withIndex()) {
                if (char.isDigit() || char == '.') continue
                grid[col, row] = true
            }
        }
        return grid
    }

    fun String.getInts(): List<Triple<Int, Int, Int>> {
        var inInt = false
        val result = mutableListOf<Triple<Int, Int, Int>>()
        var start = 0
        for ((ix, char) in withIndex()) {
            if (inInt) {
                if (char.isDigit()) continue
                val value = substring(start, ix).toInt()
                result.add(Triple(start, ix-1, value))
                inInt = false
            } else if (char.isDigit()) {
                inInt = true
                start = ix
            }
        }
        if (inInt) {
            val value = substring(start).toInt()
            result.add(Triple(start, length-1, value))
        }
        return result
    }

    fun buildGrid2(input: List<String>): Grid<NumberLine?> {
        val grid = Grid<NumberLine?>(null)
        for ((row, line) in input.withIndex()) {
            for ((start, end, value) in line.getInts()) {
                val number = NumberLine(row, start, end, value)
                number.line.forEach {
                    grid[it] = number
                }
            }
        }
        return grid
    }

    fun part1(input: List<String>): Int {
        val grid = buildGrid1(input)
        var sum = 0
        for ((row, line) in input.withIndex()) {
            for ((start, end, value) in line.getInts()) {
                val pointsAdjacent = Point.adjacentToLine(Point(start, row), Point(end, row))
                if (pointsAdjacent.any { grid[it] }) {
                    sum += value
                } else {
//                    println("$value not adjacent to a symbol")
//                    pointsAdjacent.println()
                }
            }
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        val grid = buildGrid2(input)
        var sum = 0
        for ((row, line) in input.withIndex()) {
            for (ix in line.mapIndexedNotNull { index, c -> if (c == '*') index else null }) {
                val numbers = Point(ix,row).pointsAdjacent().mapNotNull { grid[it] }.toSet()
                if (numbers.size == 2) {
//                    println("Got $numbers")
                    sum += numbers.first().value * numbers.last().value
                } else if (numbers.size > 2) {
                    println("Got $numbers")
                }
            }
        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = """
467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...${'$'}.*....
.664.598..
    """.trimIndent().split("\n")

    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
