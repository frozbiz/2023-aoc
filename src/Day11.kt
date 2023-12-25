import java.lang.IllegalStateException

enum class GridCoordinateType {
    ROW, COL
}

fun main() {

    fun <T> Grid<T>.byTranslatingPoints(orderBy: GridCoordinateType, predicate:(Point) -> Point): Grid<T> {
        val newGrid = Grid(default)
        for ((point, value) in allPoints().sortedWith(
            compareBy(
                { if (orderBy == GridCoordinateType.ROW) it.first.y else it.first.x },
                { if (orderBy == GridCoordinateType.ROW) it.first.x else it.first.y }
            ))) {
            newGrid[predicate(point)] = value
        }
        return newGrid
    }

    fun parse(input: List<String>): Grid<Boolean> {
        val grid = BooleanGrid()
        var rowExpansion = 0
        for ((row, line) in input.withIndex()) {
            var emptyRow = true
            for ((col, char) in line.trim().withIndex()) {
                if (char == '#') {
                    emptyRow = false
                    grid[Point(col, row + rowExpansion)] = true
                }
            }
            if (emptyRow) {
                ++rowExpansion
            }
        }
        var colExpansion = 0
        var lastCol = -1
        return grid.byTranslatingPoints(orderBy = GridCoordinateType.COL) { point ->
            if (lastCol >= 0 && point.x > lastCol + 1 ) {
                colExpansion += point.x - (lastCol + 1)
            }
            lastCol = point.x
            Point(point.x + colExpansion, point.y)
        }
    }

    fun parse2(input: List<String>, coefficientOfExpansion: Int = 1000000): Grid<Boolean> {
        val grid = BooleanGrid()
        var rowExpansion = 0
        for ((row, line) in input.withIndex()) {
            var emptyRow = true
            for ((col, char) in line.trim().withIndex()) {
                if (char == '#') {
                    emptyRow = false
                    grid[Point(col, row + (rowExpansion * (coefficientOfExpansion - 1)))] = true
                }
            }
            if (emptyRow) {
                ++rowExpansion
            }
        }
        var colExpansion = 0
        var lastCol = -1
        return grid.byTranslatingPoints(orderBy = GridCoordinateType.COL) { point ->
            if (lastCol >= 0 && point.x > lastCol + 1 ) {
                colExpansion += (point.x - (lastCol + 1)) * (coefficientOfExpansion - 1)
            }
            lastCol = point.x
            Point(point.x + colExpansion, point.y)
        }
    }

    fun part1(input: List<String>): Int {
        var sum = 0
        val grid = parse2(input, 2)
        val points = grid.allPoints().filter { it.second }.map { it.first }
        for ((ix, first) in points.withIndex()) {
            for (second in points.drop(ix + 1)) {
                val distance = first.manhattanDistanceTo(second)
                sum += distance
            }
        }
        return sum
    }

    fun part2(input: List<String>, coefficientOfExpansion: Int = 1000000): Long {
        var sum = 0L
        val grid = parse2(input, coefficientOfExpansion)
        val points = grid.allPoints().filter { it.second }.map { it.first }
        for ((ix, first) in points.withIndex()) {
            for (second in points.drop(ix + 1)) {
                val distance = first.manhattanDistanceTo(second)
                sum += distance
            }
        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = """
...#......
.......#..
#.........
..........
......#...
.#........
.........#
..........
.......#..
#...#.....
    """.trimIndent().split("\n")

    check(part1(testInput1) == 374)

    check(part2(testInput1, 10) == 1030L)
    check(part2(testInput1, 100) == 8410L)
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
    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
