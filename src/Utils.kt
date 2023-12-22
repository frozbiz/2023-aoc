import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.absoluteValue

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("data/$name/input.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)


/// Multipurpose point class
data class Point(
    val x: Int,
    val y: Int
) {
    enum class Relationship {
        HORIZONTAL, VERTICAL, DIAGONAL, SKEW, EQUAL
    }

    operator fun plus(other: Point): Point {
        return Point(other.x + x, other.y + y)
    }

    companion object {
        fun fromString(str: String): Point{
            val (x, y) = str.split(',').map { it.trim().toInt() }
            return Point(x,y)
        }

        fun fromStringOrNull(str: String): Point? {
            return try { fromString(str) } catch (e: Exception) { null }
        }

        fun adjacentToLine(start: Point, end: Point): Set<Point> {
            // simple and stupid
            val line = start to end
            val allPoints = line.flatMap { it.pointsAdjacent() }.toSet()
            return allPoints - line
        }
    }

    fun relationshipTo(point: Point): Relationship {
        return if (point.x == x) {
            if (point.y == y) {
                Relationship.EQUAL
            } else {
                Relationship.VERTICAL
            }
        } else if (point.y == y) {
            Relationship.HORIZONTAL
        } else if ((point.y - y).absoluteValue == (point.x - x).absoluteValue) {
            Relationship.DIAGONAL
        } else {
            Relationship.SKEW
        }
    }

    infix fun to(point: Point): Set<Point> {
        val relationship = relationshipTo(point)
        return when (relationship) {
            Relationship.EQUAL, Relationship.VERTICAL -> {
                if (point.y >= y) {
                    (y..point.y).map { Point(x, it) }
                } else {
                    (y downTo point.y).map { Point(x, it) }
                }
            }
            Relationship.HORIZONTAL -> {
                if (point.x >= x) {
                    (x..point.x).map { Point(it, y) }
                } else {
                    (x downTo point.x).map { Point(it, y) }
                }
            }
            Relationship.DIAGONAL -> {
                val xRange =
                    if (point.x >= x) {
                        (x..point.x)
                    } else {
                        (x downTo point.x)
                    }
                val yRange =
                    if (point.y >= y) {
                        (y..point.y)
                    } else {
                        (y downTo point.y)
                    }
                (xRange zip yRange).map { (x,y) -> Point(x, y) }
            }
            Relationship.SKEW -> {
                throw IllegalArgumentException("Points are non-linear")
            }
        }.toSet()
    }

    fun pointsAdjacent(cardinalOnly: Boolean = false): Set<Point> {
        val cardinalPoints = setOf(
            Point(x-1, y),
            Point(x+1, y),
            Point(x, y-1),
            Point(x, y+1)
        )
        if (cardinalOnly) return cardinalPoints

        val diagonalPoints = setOf(
            Point(x-1, y-1),
            Point(x-1, y+1),
            Point(x+1, y-1),
            Point(x+1, y+1)
        )
        return cardinalPoints + diagonalPoints
    }

    fun manhattanDistanceTo(point: Point): Int {
        return (point.x - x).absoluteValue + (point.y - y).absoluteValue
    }
    override fun toString(): String {
        return "Point(${x}, ${y})"
    }
}

fun IntGrid(default: Int = 0) = Grid(default)

fun BooleanGrid(default: Boolean = false) = Grid(default)

class Grid<T>(
    val default: T
) {
    operator fun get(x: Int, y: Int): T {
        return grid[y]?.get(x) ?: default
    }

    operator fun set(x: Int, y: Int, value: T) {
        grid.getOrPut(y) { mutableMapOf() }[x] = value
    }

    operator fun get(point: Point): T {
        return get(point.x, point.y)
    }

    operator fun set(point: Point, value: T) {
        set(point.x, point.y, value)
    }

    val grid = mutableMapOf<Int, MutableMap<Int, T>>()

    override fun toString(): String {
        return grid.toString()
    }

    fun allPoints(): List<Pair<Pair<Int, Int>, T>> {
        return grid.flatMap { (y, dict) -> dict.map { (x, count) -> Pair(Pair(x,y), count) } }
    }
}
