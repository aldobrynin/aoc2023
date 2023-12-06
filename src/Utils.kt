import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.time.measureTime

/**
 * Reads lines from the given input txt file.
 */
fun readInput(day: Int, name: String) = Path("src/day${"%02d".format(day)}/$name.txt").readLines()

/**
 * The cleaner shorthand for printing output.
 */
fun <T> T.dump(message: String? = null): T {
    if (message != null) print(message)
    println(this)
    return this
}

fun <TInput, TResult : Comparable<TResult>> solveAndVerify(input: TInput, solver: (TInput) -> TResult, expected: TResult) {
    val result = solver(input)
    if (result != expected) {
        throw Exception("Expected result is '$expected', but got '$result'")
    }
}

fun measureAndPrint(action: () -> Unit) = measureTime(action).dump("Executed in ")

fun Iterable<Long>.product(): Long = this.fold(1L) { acc, cur -> acc * cur}
fun Iterable<Int>.product(): Int = this.fold(1) { acc, cur -> acc * cur}
fun <T> Iterable<T>.product(transform: (T) -> Long): Long = this.fold(1L) { acc, cur -> acc * transform(cur)}
fun <T> Iterable<T>.product(transform: (T) -> Int): Int = this.fold(1) { acc, cur -> acc * transform(cur)}
