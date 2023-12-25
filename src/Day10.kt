import java.lang.IllegalStateException

data class Pipe (
    val openings: Set<CardinalDirection>
) {
    companion object {
        fun fromChar(char: Char): Pipe? {
            return when (char) {
                '|' -> Pipe(setOf(CardinalDirection.NORTH, CardinalDirection.SOUTH))
                '-' -> Pipe(setOf(CardinalDirection.EAST, CardinalDirection.WEST))
                'J' -> Pipe(setOf(CardinalDirection.WEST, CardinalDirection.NORTH))
                'F' -> Pipe(setOf(CardinalDirection.EAST, CardinalDirection.SOUTH))
                '7' -> Pipe(setOf(CardinalDirection.WEST, CardinalDirection.SOUTH))
                'L' -> Pipe(setOf(CardinalDirection.NORTH, CardinalDirection.EAST))
                else -> null
            }
        }
    }
}

fun Grid<Pipe?>.connects(point: Point, direction: CardinalDirection): Boolean {
    return this[point.pointToThe(direction)]?.openings?.contains(direction.opposite) == true
}

fun main() {

    fun parse(input: List<String>): Pair<Grid<Pipe?>, Point> {
        val grid = Grid<Pipe?>(null)
        lateinit var start: Point
        for ((row, line) in input.withIndex()) {
            for ((col, char) in line.trim().withIndex()) {
                if (char == 'S') {
                    start = Point(col, row)
                } else {
                    Pipe.fromChar(char)?.let {
                        grid[Point(col, row)] = it
                    }
                }
            }
        }
        return grid to start
    }

    fun part1(input: List<String>): Int {
        val (grid, start) = parse(input)
        var nextDirection = CardinalDirection.entries.first { grid.connects(point = start, it) }
        var currentPoint = start.pointToThe(nextDirection)
        var steps = 1
        while (currentPoint != start) {
            val nextPipe = grid[currentPoint] ?: throw IllegalStateException()
            if (nextDirection.opposite !in nextPipe.openings) throw IllegalStateException()
            nextDirection = nextPipe.openings.first { it != nextDirection.opposite }
            ++steps
            currentPoint = currentPoint.pointToThe(nextDirection)
        }
        "Took $steps steps all the way around".println()
        return steps / 2
    }

    fun part2(input: List<String>): Int {
        val (grid, start) = parse(input)
        var nextDirection = CardinalDirection.entries.first { grid.connects(point = start, it) }
        var currentPoint = start.pointToThe(nextDirection)
        val pointSet = mutableSetOf(currentPoint)
        while (currentPoint != start) {
            val nextPipe = grid[currentPoint] ?: throw IllegalStateException()
            if (nextDirection.opposite !in nextPipe.openings) throw IllegalStateException()
            nextDirection = nextPipe.openings.first { it != nextDirection.opposite }
            currentPoint = currentPoint.pointToThe(nextDirection)
            pointSet.add(currentPoint)
        }
        grid[start] = Pipe(CardinalDirection.entries.filter { grid.connects(start, it) }.toSet())
        // Bounding box
        val top = pointSet.minOf(Point::y)
        val bottom = pointSet.maxOf(Point::y)
        val left = pointSet.minOf(Point::x)
        val right = pointSet.maxOf(Point::x)

        var count = 0

        for (row in top..bottom) {
            var edges = 0
            val ns = setOf(CardinalDirection.NORTH, CardinalDirection.SOUTH)
            for (col in left .. right) {
                val point = Point(col, row)
                if (point in pointSet) {
                    val pipe = grid[point] ?: throw IllegalStateException()
                    if (CardinalDirection.NORTH in pipe.openings) {
                        ++edges
                    }
                } else if ((edges % 2) == 1) {
                    ++count
                }
            }
        }

        return count
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = """
.....
.S-7.
.|.|.
.L-J.
.....
    """.trimIndent().split("\n")

    check(part1(testInput1) == 4)

    val testInput2 = """
-L|F7
7S-7|
L|7||
-L-J|
L|-JF
    """.trimIndent().split("\n")


    check(part1(testInput2) == 4)

    val testInput3 = """
7-F7-
.FJ|7
SJLL7
|F--J
LJ.LJ
    """.trimIndent().split("\n")


    check(part1(testInput3) == 8)

    val testInput4 = """
..........
.S------7.
.|F----7|.
.||OOOO||.
.||OOOO||.
.|L-7F-J|.
.|II||II|.
.L--JL--J.
..........
    """.trimIndent().split("\n")


    check(part2(testInput4) == 4)

    val testInput5 = """
FF7FSF7F7F7F7F7F---7
L|LJ||||||||||||F--J
FL-7LJLJ||||||LJL-77
F--JF--7||LJLJ7F7FJ-
L---JF-JLJ.||-FJLJJ7
|F|F-JF---7F7-L7L|7|
|FFJF7L7F-JF7|JL---7
7-L-JL7||F7|L7F-7F7|
L.L7LFJ|||||FJL7||LJ
L7JLJL-JLJLJL--JLJ.L
    """.trimIndent().split("\n")


    check(part2(testInput5) == 10)


    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
