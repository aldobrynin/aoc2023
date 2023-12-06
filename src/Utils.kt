import kotlin.time.measureTime

/**
 * The cleaner shorthand for printing output.
 */
fun <T> T.dump(message: String? = null): T {
    if (message != null) print(message)
    println(this)
    return this
}

fun <TResult : Comparable<TResult>> solveAndVerify(solver: () -> TResult, expected: TResult) {
    val result = solver()
    if (result != expected) {
        throw Exception("Expected result is '$expected', but got '$result'")
    }
}

fun measureAndPrint(action: () -> Unit) = measureTime(action).dump("Executed in ")

fun Iterable<Long>.product(): Long = this.fold(1L) { acc, cur -> acc * cur }
fun Iterable<Int>.product(): Int = this.fold(1) { acc, cur -> acc * cur }
fun <T> Iterable<T>.product(transform: (T) -> Long): Long = this.fold(1L) { acc, cur -> acc * transform(cur) }
fun <T> Iterable<T>.product(transform: (T) -> Int): Int = this.fold(1) { acc, cur -> acc * transform(cur) }
