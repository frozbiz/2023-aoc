import java.lang.IllegalStateException
import java.util.regex.Pattern
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.measureTime

enum class Condition(val char: Char) {
    Working(','), Damaged('#'), Unknown('?');

    companion object {
        fun fromChar(char: Char): Condition? {
            return when (char) {
                '.' -> Working
                '#' -> Damaged
                '?' -> Unknown
                else -> null
            }
        }
    }
}

fun main() {
    data class ConditionRow(val conditions: List<Condition>, val brokenGroups: List<Int>)

    infix fun List<Condition>.with(brokenGroups: List<Int>): ConditionRow {
        return ConditionRow(this, brokenGroups)
    }

    fun parse(input: String): ConditionRow {
        val ret = mutableListOf<Condition>()
        val (conditions, brokenList) = input.trim().split(' ', limit = 2)
        for (char in conditions) {
            val condition = Condition.fromChar(char) ?: throw IllegalArgumentException()
            ret.add(condition)
        }
        return ret with brokenList.split(',').map(String::toInt)
    }

    fun List<Condition>.brokenGroups(): List<Int> {
        val result: MutableList<Int> = mutableListOf()
        var broken = 0
        for (condition in this) {
            when (condition) {
                Condition.Damaged -> ++broken
                Condition.Working -> {
                    if (broken > 0) {
                        result.add(broken)
                        broken = 0
                    }
                }
                else -> throw IllegalStateException()
            }
        }
        if (broken > 0) {
            result.add(broken)
        }
        return result
    }

    fun List<Condition>.fitsBrokenPattern(pattern: List<Int>): Boolean {
        return this.brokenGroups() == pattern
    }

    fun plausible(conditions: List<Condition>, test: List<Int>): Boolean {
        return conditions.count { it != Condition.Working } >= test.sum()
    }

    fun enumerateOptions(conditions: MutableList<Condition>, test: List<Int>): Sequence<List<Condition>> = sequence {
        val ixTest = conditions.lastIndexOf(Condition.Unknown)
        if (ixTest < 0) {
            yield(conditions)
        } else if (plausible(conditions, test)) {
            conditions[ixTest] = Condition.Working
            yieldAll(enumerateOptions(conditions, test))
            conditions[ixTest] = Condition.Damaged
            yieldAll(enumerateOptions(conditions, test))
            conditions[ixTest] = Condition.Unknown
        }
    }

    fun validConditions(conditionRow: ConditionRow): Sequence<List<Condition>> {
        return enumerateOptions(conditionRow.conditions.toMutableList(), conditionRow.brokenGroups).filter { it.fitsBrokenPattern(conditionRow.brokenGroups) }
    }

    fun part1(input: List<String>): Int {
        var sum = 0
        var timeSum = 0.seconds
        for (line in input) {
            val row = parse(line)
            timeSum += measureTime {
                sum += validConditions(row).count()
            }
        }
        "Took $timeSum".println()
        return sum
    }

    operator fun<T> List<T>.times(multiple: Int): List<T> {
        val result = mutableListOf<T>()
        repeat (multiple) {
            result += this
        }
        return result
    }

    fun part2(input: List<String>): Int {
        var sum = 0
        for (line in input) {
            val row = parse(line)
            val expandedRow = row.conditions * 5 with row.brokenGroups * 5
            sum += validConditions(expandedRow).count()
        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = """
???.### 1,1,3
.??..??...?##. 1,1,3
?#?#?#?#?#?#?#? 1,3,1,6
????.#...#... 4,1,1
????.######..#####. 1,6,5
?###???????? 3,2,1
    """.trimIndent().split("\n")

    check(part1(testInput1) == 21)
//    check(part2(testInput1) == 525152)
//    check(part2(testInput1, 100) == 8410L)
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
    val input = readInput("Day12")
    part1(input).println()
//    part2(input).println()
}
