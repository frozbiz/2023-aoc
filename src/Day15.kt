
fun String.HASH(): Int {
    var hash = 0
    for (char in this) {
        hash += char.code
        hash *= 17
        hash %= 256
    }
    return hash
}

class HASHMAP {
    data class Entry(
        val key: String,
        var value: Int
    )

    val hashBoxes = Array(256) {
        mutableListOf<Entry>()
    }

    fun remove(key: String) {
        hashBoxes[key.HASH()].removeIf { it.key == key }
    }

    fun processInstruction(instruction: String) {
        val ix = instruction.indexOfAny("-=".toCharArray())
        val key = instruction.substring(0, ix)
        when (instruction[ix]) {
            '-' -> remove(key)
            '=' -> set(key, instruction.substring(ix+1).toInt())
        }
    }

    operator fun set(key: String, value: Int) {
        val list = hashBoxes[key.HASH()]
        val ix = list.indexOfFirst { it.key == key }
        if (ix < 0) {
            list.add(Entry(key, value))
        } else {
            list[ix].value = value
        }
    }
}

fun main() {

    fun parse(input: List<String>): List<String> {
        return input.joinToString(separator = "", transform = String::trim).split(',')
    }

    fun part1(input: List<String>): Int {
        val instructions = parse(input)
        return instructions.sumOf { it.HASH() }
    }

    fun HASHMAP.calculateValue(): Int {
        var sum = 0
        for ((hash, list) in hashBoxes.withIndex()) {
            for ((ix, entry) in list.withIndex()) {
                sum += (hash + 1) * (ix + 1) * entry.value
            }
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        val instructions = parse(input)
        val myMap = HASHMAP()
        for (instruction in instructions) {
            myMap.processInstruction(instruction)
        }
        return myMap.calculateValue()
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = """
rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-, 
pc=6,ot=7
    """.trimIndent().split("\n")

    "HASH".HASH().println()
    check(part1(testInput1) == 1320)
    check(part2(testInput1) == 145)
//
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
    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}
