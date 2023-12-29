import java.lang.IllegalStateException
import kotlin.time.measureTime

enum class Rock(val char: Char) {
    Round('O'), Square('#'), Empty('.');

    companion object {
        fun fromChar(char: Char): Rock {
            return when (char) {
                '#' -> Square
                'O' -> Round
                else -> Empty
            }
        }
    }
}

fun main() {

    fun parse(input: List<String>): Grid<Rock> {
        val grid = Grid(Rock.Empty)
        for ((row, line) in input.withIndex()) {
            for ((col, char) in line.trim().withIndex()) {
                grid[col, row] = Rock.fromChar(char)
            }
        }
        return grid
    }

    fun Grid<Rock>.print() {
        val xStart = minX
        val xEnd = maxX
        for (row in minY..maxY) {
            (xStart..xEnd).forEach { print(get(it, row).char) }
            kotlin.io.println()
        }
    }

    fun Grid<Rock>.slide(x: Int, y: Int, direction: CardinalDirection) {
        var newX = x
        var newY = y
        when (direction) {
            CardinalDirection.NORTH -> {
                newY = ((0..<y).findLast { get(x, it) != Rock.Empty } ?: -1) + 1
            }

            CardinalDirection.SOUTH -> {
                val yEnd = maxY
                newY = (((y + 1)..yEnd).find { get(x, it) != Rock.Empty } ?: (yEnd + 1)) - 1
            }

            CardinalDirection.WEST -> {
                newX = ((0..<x).findLast { get(it, y) != Rock.Empty } ?: -1) + 1
            }

            CardinalDirection.EAST -> {
                val xEnd = maxX
                newX = (((x + 1)..xEnd).find { get(it, y) != Rock.Empty } ?: (xEnd + 1)) - 1
            }
        }

        if (newX != x || newY != y) {
            this[newX, newY] = this[x, y]
            this[x, y] = Rock.Empty
        }
    }

    fun Grid<Rock>.tilt(direction: CardinalDirection) {
        when (direction) {
            CardinalDirection.NORTH -> {
                val top = 1
                val bottom = maxY
                val xEnd = maxX
                for (row in top..bottom) {
                    for (col in 0..xEnd) {
                        if (get(col, row) == Rock.Round) {
                            slide(col, row, direction)
                        }
                    }
                }
            }

            CardinalDirection.SOUTH -> {
                val top = 0
                val bottom = maxY - 1
                val xEnd = maxX
                for (row in bottom downTo top) {
                    for (col in 0..xEnd) {
                        if (get(col, row) == Rock.Round) {
                            slide(col, row, direction)
                        }
                    }
                }
            }

            CardinalDirection.WEST -> {
                val left = 1
                val right = maxX
                val yEnd = maxY
                for (col in left..right) {
                    for (row in 0..yEnd) {
                        if (get(col, row) == Rock.Round) {
                            slide(col, row, direction)
                        }
                    }
                }
            }

            CardinalDirection.EAST -> {
                val left = 0
                val right = maxX - 1
                val yEnd = maxY
                for (col in right downTo left) {
                    for (row in 0..yEnd) {
                        if (get(col, row) == Rock.Round) {
                            slide(col, row, direction)
                        }
                    }
                }
            }
        }
    }

    fun Grid<Rock>.sumWeights(): Int {
        var sum = 0
        val weightLimit = maxY + 1
        for (point in allPoints()) {
            if (point.second == Rock.Round) {
                sum += weightLimit - point.first.y
            }
        }
        return sum
    }

    fun part1(input: List<String>): Int {
        val grid = parse(input)
        val direction = CardinalDirection.NORTH
        grid.tilt(direction)
        return grid.sumWeights()
    }

    fun Grid<Rock>.spinCycle() {
        tilt(CardinalDirection.NORTH)
        tilt(CardinalDirection.WEST)
        tilt(CardinalDirection.SOUTH)
        tilt(CardinalDirection.EAST)
    }

    fun part2(input: List<String>, targetCycles: Int = 1000000000): Int {
        val grid = parse(input)
        var cycles = 0
        val grids = mutableMapOf<Grid<Rock>, Int>()
        val reverseGrids = mutableListOf<Grid<Rock>>()
        while (grid !in grids) {
            val gridCopy = grid.copy()
            reverseGrids.add(gridCopy)
            grids[gridCopy] = cycles
            grid.spinCycle()
            ++cycles
        }
        val cycleStart = grids[grid] ?: throw IllegalStateException()
        val delta = cycles - cycleStart
        val remaining = targetCycles - cycles
        val skip = (remaining / delta) * delta
        val lastGrid = reverseGrids[cycleStart + (remaining - skip)]
//        reverseGrids.forEach { it.sumWeights().println() }
        return lastGrid.sumWeights()
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = """
O....#....
O.OO#....#
.....##...
OO.#O....O
.O.....O#.
O.#..O.#.#
..O..#O..O
.......O..
#....###..
#OO..#....
    """.trimIndent().split("\n")

    check(part1(testInput1) == 136)

    part2(testInput1).println()

//    val testInput2 = """
//-L|F7
//7S-7|
//L|7||
//-L-J|
//L|-JF
//    """.trimIndent().split("\n")
//
//
//    check(part1(testInput2) == 4)
//
//    val testInput3 = """
//7-F7-
//.FJ|7
//SJLL7
//|F--J
//LJ.LJ
//    """.trimIndent().split("\n")
//
//
//    check(part1(testInput3) == 8)
//
//    val testInput4 = """
//..........
//.S------7.
//.|F----7|.
//.||OOOO||.
//.||OOOO||.
//.|L-7F-J|.
//.|II||II|.
//.L--JL--J.
//..........
//    """.trimIndent().split("\n")
//
//
//    check(part2(testInput4) == 4)
//
//    val testInput5 = """
//FF7FSF7F7F7F7F7F---7
//L|LJ||||||||||||F--J
//FL-7LJLJ||||||LJL-77
//F--JF--7||LJLJ7F7FJ-
//L---JF-JLJ.||-FJLJJ7
//|F|F-JF---7F7-L7L|7|
//|FFJF7L7F-JF7|JL---7
//7-L-JL7||F7|L7F-7F7|
//L.L7LFJ|||||FJL7||LJ
//L7JLJL-JLJLJL--JLJ.L
//    """.trimIndent().split("\n")
//
//
//    check(part2(testInput5) == 10)
//
//
    val input = readInput("Day14")
    part1(input).println()
    part2(input).println()
}
