import kotlin.math.pow
fun main() {

    fun parse(input: String): Triple<Int, Set<Int>, Set<Int>> {
        val (cardNoSection, card) = input.split(":", limit = 2)
        val cardNoIx = cardNoSection.lastIndexOf(" ")
        val cardNo = cardNoSection.substring(cardNoIx+1).toInt()
        val (winnerString, numbersWeHaveString) = card.split('|')
        val winners = winnerString.trim().split("\\s+".toRegex()).map(String::toInt).toSet()
        val numbersWeHave = numbersWeHaveString.trim().split("\\s+".toRegex()).map(String::toInt).toSet()
        return Triple(cardNo, winners, numbersWeHave)
    }

    fun part1(input: List<String>): Int {
        var sum = 0
        for (value in input) {
            val (_, winners, numbersWeHave) = parse(value)
            val winningNumbers = winners intersect numbersWeHave
            if (winningNumbers.isNotEmpty()) {
                val num = 1 shl (winningNumbers.size - 1)
//                num.println()
                sum += num.toInt()
            }
        }
        return sum
    }

    fun <K> MutableMap<K, Int>.increment(index: K, step: Int = 1): Int {
        val next = getOrDefault(index, 0) + step
        set(index, next)
        return next
    }

    fun part2(input: List<String>): Int {
        val cardArray = mutableMapOf<Int, Int>()
        for (value in input) {
            val (cardNo, winners, numbersWeHave) = parse(value)
            val cards = cardArray.increment(cardNo)
            val winningNumbers = winners intersect numbersWeHave
            for (ix in (cardNo + 1)..(cardNo + winningNumbers.size)) {
//                "Bumping $ix by $cards".println()
                cardArray.increment(ix, cards)
            }
        }
        return cardArray.values.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = """
Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
    """.trimIndent().split("\n")

    check(part1(testInput) == 13)
    check(part2(testInput) == 30)


    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
