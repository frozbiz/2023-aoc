import kotlin.time.measureTime

enum class Mirror(char: Char) {
    Vertical('|'),
    Horizontal('-'),
    LeftLeaning('\\'),
    RightLeaning('/'),
    Empty('.');

    fun nextDirections(direction: CardinalDirection): Iterable<CardinalDirection> {
        return when (this) {
            Vertical -> {
                if (direction in horizontalDirections) {
                    verticalDirections
                } else {
                    setOf(direction)
                }
            }
            Horizontal -> {
                if (direction in verticalDirections) {
                    horizontalDirections
                } else {
                    setOf(direction)
                }
            }
            LeftLeaning -> {
                when (direction) {
                    CardinalDirection.NORTH -> setOf(CardinalDirection.WEST)
                    CardinalDirection.WEST -> setOf(CardinalDirection.NORTH)
                    CardinalDirection.SOUTH -> setOf(CardinalDirection.EAST)
                    CardinalDirection.EAST -> setOf(CardinalDirection.SOUTH)
                }
            }
            RightLeaning -> {
                when (direction) {
                    CardinalDirection.NORTH -> setOf(CardinalDirection.EAST)
                    CardinalDirection.EAST -> setOf(CardinalDirection.NORTH)
                    CardinalDirection.SOUTH -> setOf(CardinalDirection.WEST)
                    CardinalDirection.WEST -> setOf(CardinalDirection.SOUTH)
                }
            }
            Empty -> setOf(direction)
        }
    }
    companion object {
        val verticalDirections = setOf(CardinalDirection.NORTH, CardinalDirection.SOUTH)
        val horizontalDirections = setOf(CardinalDirection.EAST, CardinalDirection.WEST)
        fun fromChar(char: Char): Mirror? {
            return when(char) {
                '|' -> Vertical
                '-' -> Horizontal
                '\\' -> LeftLeaning
                '/' -> RightLeaning
                '.' -> Empty
                else -> null
            }
        }
    }
}

fun main() {

    fun parse(input: List<String>): Grid<Mirror> {
        val grid = Grid(Mirror.Empty)
        for ((row, line) in input.withIndex()) {
            for ((col, char) in line.trim().withIndex()) {
                grid[col, row] = Mirror.fromChar(char) ?: throw IllegalArgumentException()
            }
        }
        return grid
    }

    data class Ray(
        val point: Point,
        val direction: CardinalDirection
    )

    fun Grid<Set<CardinalDirection>>.add(ray: Ray) {
        val current = get(ray.point)
        set(ray.point, current + ray.direction)
    }

    operator fun Grid<Set<CardinalDirection>>.contains(ray: Ray): Boolean {
        return get(ray.point).contains(ray.direction)
    }

//    fun <T>MutableSet<T>.pop(): T? {
//        return firstOrNull()?.also {
//            remove(it)
//        }
//    }
//
    fun <T>MutableSet<T>.pop(): T {
        return first().also {
            remove(it)
        }
    }

    fun CardinalDirection.char(): Char {
        return when(this) {
            CardinalDirection.NORTH -> '^'
            CardinalDirection.EAST -> '>'
            CardinalDirection.SOUTH -> 'v'
            CardinalDirection.WEST -> '<'
        }
    }

    fun Collection<CardinalDirection>.char(): Char {
        return when(size) {
            0 -> '.'
            1 -> first().char()
            in 2..9 -> size.digitToChar()
            else -> '*'
        }
    }

    fun Grid<Set<CardinalDirection>>.print() {
        val xStart = minX
        val xEnd = maxX
        for (row in minY..maxY) {
            (xStart..xEnd).forEach { print(get(it, row).char()) }
            kotlin.io.println()
        }
    }

    fun Grid<Mirror>.nextRays(ray: Ray): Iterable<Ray> {
        val mirror = get(ray.point)
        val xBounds = minX..maxX
        val yBounds = minY..maxY
        return mirror.nextDirections(ray.direction).map { Ray(ray.point + it, it) }.filter { it.point.x in xBounds && it.point.y in yBounds }
    }

    fun part1(input: List<String>): Int {
        val mirrorGrid = parse(input)
        val rayGrid = Grid<Set<CardinalDirection>>(emptySet())
        val rays = mutableSetOf(Ray(Point(0,0), CardinalDirection.EAST))
        rayGrid.add(rays.first())
        while (rays.isNotEmpty()) {
            for (ray in mirrorGrid.nextRays(rays.pop())) {
                if (ray !in rayGrid) {
                    rays.add(ray)
                    rayGrid.add(ray)
                }
            }
        }
//        rayGrid.print()
        return rayGrid.allPoints().count { it.second.isNotEmpty() }
    }


    fun part2(input: List<String>): Int {
        val mirrorGrid = parse(input)
        val top = mirrorGrid.minY
        val bottom = mirrorGrid.maxY
        val left = mirrorGrid.minX
        val right = mirrorGrid.maxX

        val topRays = (Point(left, top) to Point(right, top)).map { Ray(it, CardinalDirection.SOUTH) }
        val bottomRays = (Point(left, bottom) to Point(right, bottom)).map { Ray(it, CardinalDirection.NORTH) }
        val leftRays = (Point(left, top) to Point(left, bottom)).map { Ray(it, CardinalDirection.EAST) }
        val rightRays = (Point(right, top) to Point(right, bottom)).map { Ray(it, CardinalDirection.WEST) }



        val rayGrid = Grid<Set<CardinalDirection>>(emptySet())
        val rays = mutableSetOf(Ray(Point(0,0), CardinalDirection.EAST))
        rayGrid.add(rays.first())
        while (rays.isNotEmpty()) {
            for (ray in mirrorGrid.nextRays(rays.pop())) {
                if (ray !in rayGrid) {
                    rays.add(ray)
                    rayGrid.add(ray)
                }
            }
        }
//        rayGrid.print()
        return rayGrid.allPoints().count { it.second.isNotEmpty() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = """
.|...\....
|.-.\.....
.....|-...
........|.
..........
.........\
..../.\\..
.-.-/..|..
.|....-|.\
..//.|....
    """.trimIndent().split("\n")

    check(part1(testInput1) == 46)
//    check(part2(testInput1) == 145)
//
    val input = readInput("Day16")
    measureTime {
        part1(input).println()
    }.println()
//    part2(input).println()
}
