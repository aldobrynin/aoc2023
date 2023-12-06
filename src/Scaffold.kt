import java.time.LocalDateTime
import java.util.*
import kotlin.io.path.*

private const val AdventTimeZone = "EST"

fun main() {
    val adventTimeZone = TimeZone.getTimeZone(AdventTimeZone).toZoneId()
    val nowDateTime = LocalDateTime.now(adventTimeZone).dump("Current $AdventTimeZone time is: ")
    val day = nowDateTime.dayOfMonth
    val paddedDay = "%02d".format(day)
    println("Scaffolding files for day $paddedDay of AoC2023")

    val directory = Path("src/day$paddedDay").absolute()
    if (directory.exists()) {
        println("Directory $directory already exists")
        return
    }
    println("Creating directory $directory")
    directory.createDirectory()

    Path(directory.pathString + "/Day$paddedDay.kt").createFile().writeText(renderTemplate(day))
    Path(directory.pathString + "/sample.txt").createFile()
    Path(directory.pathString + "/input.txt").createFile()
}

fun renderTemplate(day: Int): String {
    val paddedDay = "%02d".format(day)
    return """
        package day$paddedDay

        import common.Puzzle
        import solveAndVerify
        
        private typealias ParsedInput = List<String>
        
        private class Day$paddedDay : Puzzle<ParsedInput>(day = $day) {
            override fun parse(rawInput: List<String>): ParsedInput = rawInput

            override fun part1(input: ParsedInput): Long {
                return 0
            }

            override fun part2(input: ParsedInput): Long {
                return 0
            }
        }
        
        fun main() {
            val puzzle = Day$paddedDay()

            solveAndVerify({ puzzle.part1(puzzle.parse("sample.txt")) }, expected = 0)
            solveAndVerify({ puzzle.part2(puzzle.parse("sample.txt")) }, expected = 0)

            puzzle.run()
        }
    """.trimIndent()
}
