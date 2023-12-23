
enum class CardNoJoker(val char: Char) {
    Ace('A'),
    King('K'),
    Queen('Q'),
    Jack('J'),
    Ten('T'),
    Nine('9'),
    Eight('8'),
    Seven('7'),
    Six('6'),
    Five('5'),
    Four('4'),
    Three('3'),
    Two('2');

    companion object {
        fun fromChar(char: Char): CardNoJoker {
            return entries.first { it.char == char }
        }

        fun fromCharOrNull(char: Char): CardNoJoker? {
            return entries.firstOrNull { it.char == char }
        }
    }
}

enum class CardJoker(val char: Char) {
    Ace('A'),
    King('K'),
    Queen('Q'),
    Ten('T'),
    Nine('9'),
    Eight('8'),
    Seven('7'),
    Six('6'),
    Five('5'),
    Four('4'),
    Three('3'),
    Two('2'),
    Joker('J');

    companion object {
        fun fromChar(char: Char): CardJoker {
            return entries.first { it.char == char }
        }

        fun fromCharOrNull(char: Char): CardJoker? {
            return entries.firstOrNull { it.char == char }
        }
    }
}

enum class Rank {
    FiveOAK,
    FourOAK,
    FullHouse,
    ThreeOAK,
    TwoPair,
    Pair,
    HighCard;

    companion object {
        fun fromHandNoJokers(hand: String): Rank {
            val map = mutableMapOf<CardNoJoker, Int>()
            for (char in hand) {
                map.increment(CardNoJoker.fromChar(char))
            }

            return rankForMap(map)
        }

        private fun <T> rankForMap(map: Map<T, Int>): Rank {
            return if (map.values.first() == 5) {
                FiveOAK
            } else if (4 in map.values) {
                FourOAK
            } else if (3 in map.values) {
                if (2 in map.values) {
                    FullHouse
                } else {
                    ThreeOAK
                }
            } else if (2 in map.values) {
                if (map.values.filter { it == 2 }.size == 2) {
                    TwoPair
                } else {
                    Pair
                }
            } else {
                HighCard
            }
        }

        fun fromHandWithJokers(hand: String): Rank {
            val map = mutableMapOf<CardJoker, Int>()
            for (char in hand) {
                map.increment(CardJoker.fromChar(char))
            }

            // Beef up the number for hand rank purposes
            map.remove(CardJoker.Joker)?.let { jokerCount ->
                var maxCount = 0
                var maxCard = CardJoker.Joker
                for ((card, count) in map) {
                    if (count > maxCount) {
                        maxCount = count
                        maxCard = card
                    }
                }
                map[maxCard] = maxCount + jokerCount
            }

            return rankForMap(map)
        }
    }
}

data class Hand(
    val cards: String,
    val bid: Int
) {
    companion object {
        operator fun invoke(pair: Pair<String, Int>): Hand {
            return Hand(pair.first, pair.second)
        }
    }
}

fun main() {

    fun parse(input: String): Hand {
        val (cards, bid) = input.split("\\s+".toRegex(), limit = 2)
        return Hand(cards to bid.toInt())
    }

    fun score(hands: List<Hand>): Int {
        val totalHands = hands.size
        var sum = 0
        for ((ix, hand) in hands.withIndex()) {
            val handVal = hand.bid * (totalHands - ix)
            sum += handVal
        }
        return sum
    }

    fun part1(input: List<String>): Int {
        var hands = input.map(::parse)
        hands = hands.sortedWith(compareBy(
            { Rank.fromHandNoJokers(it.cards) },
            { CardNoJoker.fromChar(it.cards[0]) },
            { CardNoJoker.fromChar(it.cards[1]) },
            { CardNoJoker.fromChar(it.cards[2]) },
            { CardNoJoker.fromChar(it.cards[3]) },
            { CardNoJoker.fromChar(it.cards[4]) },
        ))
//        hands.println()
        return score(hands)
    }

    fun part2(input: List<String>): Int {
        var hands = input.map(::parse)
        hands = hands.sortedWith(compareBy(
            { Rank.fromHandWithJokers(it.cards) },
            { CardJoker.fromChar(it.cards[0]) },
            { CardJoker.fromChar(it.cards[1]) },
            { CardJoker.fromChar(it.cards[2]) },
            { CardJoker.fromChar(it.cards[3]) },
            { CardJoker.fromChar(it.cards[4]) },
        ))
        hands.println()
        return score(hands)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = """
32T3K 765
T55J5 684
KK677 28
KTJJT 220
QQQJA 483
    """.trimIndent().split("\n")

    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)


    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
