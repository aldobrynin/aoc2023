import kotlin.io.path.*

fun main() {
    val day = 7
    val paddedDay = "%02d".format(day)
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
        
        private class Day$paddedDay : Puzzle<List<String>>(day = $day) {
            override fun parse(rawInput: List<String>): List<String> = rawInput

            override fun part1(input: List<String>): Long {
                return 0
            }

            override fun part2(input: List<String>): Long {
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
