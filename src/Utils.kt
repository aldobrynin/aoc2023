import common.V
import kotlin.time.measureTime

/**
 * The cleaner shorthand for printing output.
 */
fun <T> T.dump(message: String? = null): T {
    if (message != null) print(message)
    println(this)
    return this
}

fun String.toIntArray(delimiters: String = " ") = this.split(delimiters).filterNot { it.isBlank() }.map { it.trim().toInt() }
fun String.toLongArray(delimiters: String = " ") = this.split(delimiters).filterNot { it.isBlank() }.map { it.trim().toLong() }

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

fun <T> List<List<T>>.rows(): Iterable<List<T>> = this
fun <T> List<List<T>>.columns(): Iterable<List<T>> = this.first().indices.map { colIndex -> this.indices.map { rowIndex -> this[rowIndex][colIndex] } }

fun <T> Collection<Collection<T>>.coordinates(): Iterable<V> = this.flatMapIndexed { y, row -> List(row.size) { x -> V(x, y) } }

fun lcm(a: Long, b: Long): Long = a * b / gcd(a, b)

fun Iterable<Long>.lcm(): Long = this.fold(1L, ::lcm)

fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)

fun <T> Array<T>.swap(i: Int, j: Int) {
    val tmp = this[i]
    this[i] = this[j]
    this[j] = tmp
}