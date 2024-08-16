import java.util.PriorityQueue
import kotlin.time.measureTime

fun main() {

    fun parse(input: List<String>): Grid<Int> {
        val grid = IntGrid()
        for ((row, line) in input.withIndex()) {
            for ((col, char) in line.trim().withIndex()) {
                grid[col, row] = char.digitToInt()
            }
        }
        return grid
    }

    data class RayCount(
        val ray: Ray,
        val count: Int = 0,
        val heatLoss: Int = 0
    )

    fun Grid<Int>.nextValidChoices(rayCount: RayCount): List<RayCount> {
        return if (rayCount.count < 3) {
            val currentDirection = rayCount.ray.direction
            CardinalDirection.entries.filter { it != currentDirection.opposite }.mapNotNull {
                val newPoint = rayCount.ray.point + it
                if (!contains(newPoint)) {
                    null
                } else {
                    val newCount = if (it == currentDirection) rayCount.count + 1 else 1
                    RayCount(Ray(newPoint, it), newCount, rayCount.heatLoss + get(newPoint))
                }
            }
        } else {
            val currentDirection = rayCount.ray.direction
            CardinalDirection.entries.filter { it !in setOf(currentDirection.opposite, currentDirection) }.mapNotNull {
                val newPoint = rayCount.ray.point + it
                if (!contains(newPoint)) {
                    null
                } else {
                    RayCount(Ray(newPoint, it), 1, rayCount.heatLoss + get(newPoint))
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val grid = parse(input)
        val priorityQueue = PriorityQueue(compareBy<RayCount> {
            it.heatLoss
        })

        val visitedMap = mutableMapOf<Ray, Int>()
        listOf(RayCount(Ray(Point(0,0), CardinalDirection.SOUTH)), RayCount(Ray(Point(0,0), CardinalDirection.EAST))).forEach {
            priorityQueue.add(it)
            visitedMap[it.ray] = it.count
        }

        var nextRayToProcess = priorityQueue.remove()

        val terminus = Point(grid.maxX, grid.maxY)
        do {
            val nextOptions = grid.nextValidChoices(nextRayToProcess)
            val filteredOptions = nextOptions.filter {
                val previousCount = visitedMap[it.ray]
                previousCount == null || previousCount > it.count
            }
            filteredOptions.forEach { visitedMap[it.ray] = it.count }
            priorityQueue.addAll(filteredOptions)
            nextRayToProcess = priorityQueue.remove()
        } while (nextRayToProcess.ray.point != terminus)

        return nextRayToProcess.heatLoss
    }


    fun part2(input: List<String>): Int {
        val grid = parse(input)
        val priorityQueue = PriorityQueue(compareBy<RayCount> {
            it.heatLoss
        })

        val visitedMap = mutableMapOf<Ray, Int>()
        listOf(RayCount(Ray(Point(0,0), CardinalDirection.SOUTH)), RayCount(Ray(Point(0,0), CardinalDirection.EAST))).forEach {
            priorityQueue.add(it)
            visitedMap[it.ray] = it.count
        }

        var nextRayToProcess = priorityQueue.remove()

        val terminus = Point(grid.maxX, grid.maxY)
        do {
            priorityQueue.addAll(grid.nextValidChoices(nextRayToProcess))
            nextRayToProcess = priorityQueue.remove()
        } while (nextRayToProcess.ray.point != terminus)

        return nextRayToProcess.heatLoss
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = """
2413432311323
3215453535623
3255245654254
3446585845452
4546657867536
1438598798454
4457876987766
3637877979653
4654967986887
4564679986453
1224686865563
2546548887735
4322674655533
    """.trimIndent().split("\n")

    check(part1(testInput1) == 102)
//    check(part2(testInput1) == 51)
//
    val input = readInput("Day17")
    part1(input).println()
//    part2(input).println()
}
