import java.lang.IllegalStateException

fun main() {

    fun parse(input: List<String>): List<Grid<Boolean>> {
        val result = mutableListOf<Grid<Boolean>>()
        var grid = BooleanGrid()
        var row = 0
        for (line in input) {
            if (line.isBlank()) {
                if (row > 0) {
                    row = 0
                    result.add(grid)
                    grid = BooleanGrid()
                }
                continue
            }
            for ((col, char) in line.withIndex()) {
                grid[col, row] = char == '#'
            }
            ++row
        }

        if (row > 0) {
            result.add(grid)
        }

        return result
    }

    fun <T> Grid<T>.testVerticalSymmetry(cleave: Int, errors: Int): Boolean {
        val start = minX
        val end = maxX
        var bottom = cleave-1
        var top = cleave
        var smudges = 0
        while (top <= end && bottom >= start) {
            for (y in minY..maxY) {
                if (get(top, y) != get(bottom, y)) {
                    ++smudges
                    if (smudges > errors) {
                        return false
                    }
                }
            }
            ++top
            --bottom
        }
        return smudges == errors
    }

    fun <T> Grid<T>.findVerticalSymmetry(errors: Int = 0): Int? {
        val xStart = 1
        val xEnd = maxX
        for (ix in xStart..xEnd) {
            if (testVerticalSymmetry(ix, errors)) {
                return ix
            }
        }
        return null
    }

    fun <T> Grid<T>.testHorizontalSymmetry(cleave: Int, errors: Int): Boolean {
        val start = minY
        val end = maxY
        var bottom = cleave-1
        var top = cleave
        var smudges = 0
        while (top <= end && bottom >= start) {
            for (x in minX..maxX) {
                if (get(x, top) != get(x, bottom)) {
                    ++smudges
                    if (smudges > errors) {
                        return false
                    }
                }
            }
            ++top
            --bottom
        }
        return smudges == errors
    }

    fun <T> Grid<T>.findHorizontalSymmetry(errors: Int = 0): Int? {
        val xStart = 1
        val xEnd = maxY
        for (ix in xStart..xEnd) {
            if (testHorizontalSymmetry(ix, errors)) {
                return ix
            }
        }
        return null
    }

    fun part1(input: List<String>): Int {
        var sum = 0
        val grids = parse(input)
        for (grid in grids) {
            sum += grid.findVerticalSymmetry() ?: grid.findHorizontalSymmetry()?.let { it * 100 } ?: throw IllegalArgumentException()
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        var sum = 0
        val grids = parse(input)
        for (grid in grids) {
            sum += grid.findVerticalSymmetry(1) ?: grid.findHorizontalSymmetry(1)?.let { it * 100 } ?: throw IllegalArgumentException()
        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = """
#.##..##.
..#.##.#.
##......#
##......#
..#.##.#.
..##..##.
#.#.##.#.

#...##..#
#....#..#
..##..###
#####.##.
#####.##.
..##..###
#....#..#
    """.trimIndent().split("\n")

    check(part1(testInput1) == 405)
    check(part2(testInput1) == 400)

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
    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}
