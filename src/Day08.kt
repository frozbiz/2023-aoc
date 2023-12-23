import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

enum class Direction(val char: Char) {
    LEFT('L'), RIGHT('R');

    companion object {
        fun fromChar(char: Char): Direction {
            return entries.first { it.char == char }
        }

        fun fromCharOrNull(char: Char): Direction? {
            return entries.firstOrNull { it.char == char }
        }
    }
}

data class TreeNode (
    val name: String,
) {
    var left: String = ""
    var right: String = ""

    constructor(name: String, left: String, right: String): this(name) {
        this.left = left
        this.right = right
    }

    override fun toString(): String {
        return "TreeNode(name: $name, left: $left, right: $right)"
    }
}

fun main() {

    fun parseInstructions(input: String): List<Direction> = input.map(Direction::fromChar)

    val nodeRegex = "(\\w+) = \\((\\w+), (\\w+)\\)".toRegex()

    fun parseNodeDescription(input: String): TreeNode {
        val match = nodeRegex.find(input) ?: throw IllegalArgumentException()
        return TreeNode(match.groupValues[1], match.groupValues[2], match.groupValues[3])
    }

    fun part1(input: List<String>): Int {
        val instructions = parseInstructions(input[0])
        val nodeDictionary = mutableMapOf<String, TreeNode>()
        for (line in input.drop(2)) {
            val node = parseNodeDescription(line)
//            "Adding node: $node".println()
            nodeDictionary[node.name] = node
        }

        var currentNode = "AAA"
        var instructionIx = 0
        var moves = 0
        while (currentNode != "ZZZ") {
//            "Path: $currentNode, going: ${instructions[instructionIx]}".println()
            currentNode = when(instructions[instructionIx]) {
                Direction.RIGHT -> nodeDictionary[currentNode]?.right ?: throw IllegalStateException()
                Direction.LEFT -> nodeDictionary[currentNode]?.left ?: throw IllegalStateException()
            }
            ++moves
            ++instructionIx
            instructionIx %= instructions.size
        }
        return moves
    }

    fun part2(input: List<String>): Long {
        val instructions = parseInstructions(input[0])
        val nodeDictionary = mutableMapOf<String, TreeNode>()
        for (line in input.drop(2)) {
            val node = parseNodeDescription(line)
            nodeDictionary[node.name] = node
        }

        val startingNodes = nodeDictionary.keys.filter { it.endsWith("A") }
//        var instructionIx = 0
//        var moves = 0
        "currentNodes: $startingNodes".println()
        var product = instructions.size.toLong()
        for (startingNode in startingNodes) {
            var currentNode = startingNode
            var instructionIx = 0
            var moves = 0
            while (!currentNode.endsWith("Z") || (moves % instructions.size != 0) ) {
//            "Path: $currentNode, going: ${instructions[instructionIx]}".println()
                currentNode = when(instructions[instructionIx]) {
                    Direction.RIGHT -> nodeDictionary[currentNode]?.right ?: throw IllegalStateException()
                    Direction.LEFT -> nodeDictionary[currentNode]?.left ?: throw IllegalStateException()
                }
                ++moves
                ++instructionIx
                instructionIx %= instructions.size
            }

            product *= moves / instructions.size
        }
//        while (!currentNodes.all { it.endsWith("Z") }) {
//            "currentNodes: $currentNodes".println()
//            currentNodes = currentNodes.map { currentNode ->
//                when (instructions[instructionIx]) {
//                    Direction.RIGHT -> nodeDictionary[currentNode]?.right
//                        ?: throw IllegalStateException()
//
//                    Direction.LEFT -> nodeDictionary[currentNode]?.left
//                        ?: throw IllegalStateException()
//                }
//            }
//            ++moves
//            ++instructionIx
//            instructionIx %= instructions.size
//        }
        return product
    }

    // test if implementation meets criteria from the description, like:
    val testInput = """
RL

AAA = (BBB, CCC)
BBB = (DDD, EEE)
CCC = (ZZZ, GGG)
DDD = (DDD, DDD)
EEE = (EEE, EEE)
GGG = (GGG, GGG)
ZZZ = (ZZZ, ZZZ)
    """.trimIndent().split("\n")

    val testInput2 = """
LLR

AAA = (BBB, BBB)
BBB = (AAA, ZZZ)
ZZZ = (ZZZ, ZZZ)
    """.trimIndent().split("\n")

    check(part1(testInput) == 2)
    check(part1(testInput2) == 6)

    val testInput3 = """
LR

11A = (11B, XXX)
11B = (XXX, 11Z)
11Z = (11B, XXX)
22A = (22B, XXX)
22B = (22C, 22C)
22C = (22Z, 22Z)
22Z = (22B, 22B)
XXX = (XXX, XXX)
    """.trimIndent().split("\n")

    check(part2(testInput3) == 6L)


    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
