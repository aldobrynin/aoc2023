import kotlin.io.path.Path
import kotlin.io.path.readLines

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