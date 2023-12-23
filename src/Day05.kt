data class SeedMap(
    val offset: Long,
    val range: LongRange
) {
    operator fun contains(value: Long): Boolean = value in range
}

typealias SeedMaps = Iterable<SeedMap>

fun String.toSeedMap(): SeedMap {
//    this.println()
    val (dest, src, len) = split("\\s+".toRegex()).mapNotNull(String::toLong)
    return SeedMap(
        offset = dest - src,
        range = LongRange(start = src, length = len)
    )
}

fun main() {

    fun parse(input: List<String>): Pair<List<Long>, List<SeedMaps>> {
        val (_, list) = input[0].split(":", limit = 2)
        val seeds = list.split("\\s+".toRegex()).mapNotNull(String::toLongOrNull)
        val mapList = mutableListOf<SeedMaps>()
        lateinit var maps: MutableList<SeedMap>
        for (line in input.drop(1)) {
            if (line.isBlank()) continue
            if (line.endsWith("map:")) {
                maps = mutableListOf()
                mapList.add(maps)
            } else {
                maps.add(line.toSeedMap())
            }
        }
        return seeds to mapList
    }

    fun Long.transformedBy(seedMaps: SeedMaps): Long {
        for (seedMap in seedMaps) {
            if (this in seedMap) {
                return this + seedMap.offset
            }
        }
        return this
    }

    fun part1(input: List<String>): Long {
        var (seeds, seedMapList) = parse(input)
//        seeds.println()
        for (seedMaps in seedMapList) {
            seeds = seeds.map { it.transformedBy(seedMaps) }
//            seeds.println()
        }
        return seeds.min()
    }

    fun MultiRange.applyOffset(offset: Long) {
        for (ix in 0..<rangeList.size) {
            val range = rangeList[ix]
            rangeList[ix] = (range.first + offset)..(range.last + offset)
        }
    }

    fun MultiRange.transformedBy(seedMaps: SeedMaps): MultiRange {
        val input = this.copy()
        val output = MultiRange()

        for (seedMap in seedMaps) {
            val newRange = input.subtract(seedMap.range)
            newRange.applyOffset(seedMap.offset)
            output += newRange
        }
        output += input
        return output
    }

    fun part2(input: List<String>): Long {
        val (seedRanges, seedMapList) = parse(input)
        var seeds = MultiRange()
        for (ix in seedRanges.indices step 2) {
            seeds.add(LongRange(start = seedRanges[ix], length = seedRanges[ix+1]))
        }
        for (seedMaps in seedMapList) {
            seeds = seeds.transformedBy(seedMaps)
//            seeds.println()
        }
        return seeds.first()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = """
seeds: 79 14 55 13

seed-to-soil map:
50 98 2
52 50 48

soil-to-fertilizer map:
0 15 37
37 52 2
39 0 15

fertilizer-to-water map:
49 53 8
0 11 42
42 0 7
57 7 4

water-to-light map:
88 18 7
18 25 70

light-to-temperature map:
45 77 23
81 45 19
68 64 13

temperature-to-humidity map:
0 69 1
1 0 69

humidity-to-location map:
60 56 37
56 93 4
    """.trimIndent().split("\n")

    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)


    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
