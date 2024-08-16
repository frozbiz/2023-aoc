import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.net.InetAddress
import java.net.URI
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.Charset

fun main() {

    fun String.toNumber(): Int {
        return first { it.isDigit() }.digitToInt() * 10 + last { it.isDigit() }.digitToInt()
    }

    val numbers = listOf(
        "0",
        "1",
        "2",
        "3",
        "4",
        "5",
        "6",
        "7",
        "8",
        "9",
        "zero",
        "one",
        "two",
        "three",
        "four",
        "five",
        "six",
        "seven",
        "eight",
        "nine"
    )

    fun String.value(): Int {
        return when (this) {
            "0", "zero" -> 0
            "1", "one" -> 1
            "2", "two" -> 2
            "3", "three" -> 3
            "4", "four" -> 4
            "5", "five" -> 5
            "6", "six" -> 6
            "7", "seven" -> 7
            "8", "eight" -> 8
            "9", "nine" -> 9
            else -> throw IllegalArgumentException()
        }
    }

    fun String.toNumber2(): Int {
        val firstDigit = findAnyOf(numbers)?.second?.value() ?: throw IllegalStateException()
        val lastDigit = findLastAnyOf(numbers)?.second?.value() ?: throw IllegalStateException()
        return firstDigit * 10 + lastDigit
    }

    fun part1(input: List<String>): Int {
        var sum = 0
        for (value in input) {
            val num = value.toNumber()
//            num.println()
            sum += num
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        var sum = 0
        for (value in input) {
            val num = value.toNumber2()
//            num.println()
            sum += num
        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = """
        1abc2
        pqr3stu8vwx
        a1b2c3d4e5f
        treb7uchet
    """.trimIndent().split("\\s+".toRegex())

    check(part1(testInput) == 142)

    val testInputTwo = """
        two1nine
        eightwothree
        abcone2threexyz
        xtwone3four
        4nineeightseven2
        zoneight234
        7pqrstsixteen
    """.trimIndent().split("\\s+".toRegex())

    check(part2(testInputTwo) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()

    val uri = URI("local-library:/browse?fo%3do=bar")

    uri.println()
    uri.query.println()
    uri.rawQuery.println()

    data class URIQueryParam(
        val name: String,
        val value: String? = null
    ) {
        fun urlForm(): String {
            return StringBuilder().apply {
                append(URLEncoder.encode(name))
                if (value != null) {
                    append('=')
                    append(URLEncoder.encode(value))
                }
            }.toString()
        }
    }

    fun <T>Iterator<T>.nextOrNull(): T? {
        return if (hasNext()) next() else null
    }
    fun URIQueryParam(pair: Iterable<String>): URIQueryParam? {
        val iterator = pair.iterator()
        val name = iterator.nextOrNull() ?: return null
        val value = iterator.nextOrNull()
        return URIQueryParam(name, value)
    }

    val uri2 = URI("local-library:/browse?test1=1&test2=2&test3=&test4&&")
    var pairs = uri.rawQuery.split('&').map { it.split('=') }
    println(pairs)
    pairs = uri.rawQuery.split('&').map { it.split('=').map { URLDecoder.decode(it, Charset.defaultCharset()) } }
    println(pairs)
    var queries = uri.rawQuery.split('&').map { it.split('=').map { URLDecoder.decode(it, Charset.defaultCharset()) } }.mapNotNull(::URIQueryParam)
    println(queries)
    var query = queries.map(URIQueryParam::urlForm).joinToString(separator = "&")
    query.println()
    var uriConstructed = URI(uri.scheme, uri.authority, uri.path, query, uri.fragment)
    uriConstructed.println()
    pairs = uri2.rawQuery.split('&').map { it.split('=') }
    println(pairs)
    pairs = uri2.rawQuery.split('&').map { it.split('=').map { URLDecoder.decode(it, Charset.defaultCharset()) } }
    println(pairs)
    queries = uri2.rawQuery.split('&').map { it.split('=').map { URLDecoder.decode(it, Charset.defaultCharset()) } }.mapNotNull(::URIQueryParam)
    println(queries)
    query = queries.map(URIQueryParam::urlForm).joinToString(separator = "&")
    query.println()
    uriConstructed = URI(uri2.scheme, uri2.authority, uri2.path, query, uri2.fragment)
    uriConstructed.println()

//    var foo = URI(":test")
//    foo.println()
    var foo = URI("scheme", "host.com:1400", null, "query=string&foo=bar", null)
    foo.println()
    foo.host.println()

    listOf("foo=bar").joinToString(separator = "&", prefix = "?").println()
    emptyList<String>().joinToString(separator = "&", prefix = "?").println()
    listOf("").joinToString(separator = "&", prefix = "?").println()
    listOf("foo=bar", "", "a=b").joinToString(separator = "&", prefix = "?").println()
    (0xff and 0xff00.inv()).println()
    URI("").println()
//    InetAddress.getByAddress(ByteArray(5))

}
