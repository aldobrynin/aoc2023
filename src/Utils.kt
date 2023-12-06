import kotlin.time.measureTime

/**
 * The cleaner shorthand for printing output.
 */
fun <T> T.dump(message: String? = null, transform: ((T) -> Any)? = null): T {
    if (message != null) print(message)
    val objectToPrint = if (transform == null) this else transform(this)
    println(objectToPrint)
    return this
}

fun String.toIntArray(delimiters: String = " ") = this.split(delimiters).filterNot { it.isBlank() }.map { it.toInt() }
fun String.toLongArray(delimiters: String = " ") = this.split(delimiters).filterNot { it.isBlank() }.map { it.toLong() }

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
