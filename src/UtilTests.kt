//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import org.junit.jupiter.api.Test
//
//@OptIn(ExperimentalCoroutinesApi::class)
object MultiRangeTests {
    fun testSubtractRange1() {
        val multiRange = MultiRange(
            mutableListOf(
                0..<5L,
                10..<15L,
                20..<25L
            )
        )

        check(multiRange.subtract(4..10L).rangeList == listOf(4..4L, 10..10L))
        check(multiRange.rangeList == listOf(0..<4L, 11..<15L, 20..<25L))
    }

    fun testSubtractRange2() {
        val multiRange = MultiRange(
            mutableListOf(
                0..<15L,
                20..<25L
            )
        )

        check(multiRange.subtract(4..10L).rangeList == listOf(4..10L))
        check(multiRange.rangeList == listOf(0..<4L, 11..<15L, 20..<25L))
    }

    fun testSubtractRange3() {
        val multiRange = MultiRange(
            mutableListOf(
                0..<5L,
                6..7L,
                10..<15L,
                20..<25L
            )
        )

        check(multiRange.subtract(4..10L).rangeList == listOf(4..4L, 6..7L, 10..10L))
        check(multiRange.rangeList == listOf(0..<4L, 11..<15L, 20..<25L))
    }

    fun testSubtractRange4() {
        val multiRange = MultiRange(
            mutableListOf(
                0..<5L,
                10..<15L,
                20..<25L
            )
        )

        check(multiRange.subtract(10..14L).rangeList == listOf(10..14L))
        check(multiRange.rangeList == listOf(0..<5L, 20..<25L))
    }
    fun testSubtractRange5() {
        val multiRange = MultiRange(
            mutableListOf(
                0..<5L,
                10..<15L,
                20..<25L
            )
        )

        check(multiRange.subtract(9..14L).rangeList == listOf(10..14L))
        check(multiRange.rangeList == listOf(0..<5L, 20..<25L))
    }
    fun testSubtractRange6() {
        val multiRange = MultiRange(
            mutableListOf(
                0..<5L,
                10..<15L,
                20..<25L
            )
        )

        check(multiRange.subtract(9..15L).rangeList == listOf(10..14L))
        check(multiRange.rangeList == listOf(0..<5L, 20..<25L))
    }
}

fun main() {
    MultiRangeTests.testSubtractRange1()
    MultiRangeTests.testSubtractRange2()
    MultiRangeTests.testSubtractRange3()
    MultiRangeTests.testSubtractRange4()
    MultiRangeTests.testSubtractRange5()
    MultiRangeTests.testSubtractRange6()
}
