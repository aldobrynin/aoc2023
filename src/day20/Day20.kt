package day20

import common.Puzzle
import lcm
import product
import solveAndVerify

private typealias ParsedInput = Map<String, Module>
private enum class Pulse { HIGH, LOW }
private data class Signal(val source: String, val target: String, val pulse: Pulse)

private abstract class Module(val name: String, val outputs: List<String>) {
    val sources = mutableListOf<String>()
    fun addSource(source: String) = sources.add(source)
    abstract fun handle(signal: Signal): List<Signal>
    open fun reset() {}

    companion object {
        fun parse(rawInput: String): Module {
            val (sender, receiver) = rawInput.split(" -> ")
            val name = sender.trimStart('%', '&')
            val outputs = receiver.split(", ")
            return when (sender[0]) {
                '%' -> FlipFlop(name, outputs)
                '&' -> Conjunction(name, outputs)
                else -> Broadcaster(name, outputs)
            }
        }
    }

    class FlipFlop(name: String, outputs: List<String>) : Module(name, outputs) {
        private var state = Pulse.LOW
        override fun handle(signal: Signal): List<Signal> {
            if (signal.pulse == Pulse.HIGH) return emptyList()
            state = if (state == Pulse.HIGH) Pulse.LOW else Pulse.HIGH
            return outputs.map { Signal(name, it, state) }
        }

        override fun reset() {
            state = Pulse.LOW
        }
    }

    class Conjunction(name: String, outputs: List<String>) : Module(name, outputs) {
        private val memory: MutableMap<String, Pulse> = mutableMapOf()
        override fun handle(signal: Signal): List<Signal> {
            memory[signal.source] = signal.pulse
            val response =
                if (memory.size == sources.size && memory.all { it.value == Pulse.HIGH }) Pulse.LOW else Pulse.HIGH
            return outputs.map { Signal(name, it, response) }
        }

        override fun reset() = memory.clear()
    }

    class Broadcaster(name: String, outputs: List<String>) : Module(name, outputs) {
        override fun handle(signal: Signal): List<Signal> = outputs.map { Signal(name, it, signal.pulse) }
    }
}

private class Day20 : Puzzle<ParsedInput>(day = 20) {
    override fun parse(rawInput: List<String>): ParsedInput = rawInput.map { Module.parse(it) }
        .associateBy { it.name }
        .also {
            it.flatMap { (name, module) -> module.outputs.map { output -> name to output } }
                .filter { (_, output) -> output in it }
                .forEach { (source, output) -> it.getValue(output).addSource(source) }
        }

    override fun part1(input: ParsedInput): Long {
        val counts = mutableMapOf(Pulse.LOW to 0L, Pulse.HIGH to 0L)

        repeat(1000) {
            for ((_, _, pulse) in pressButton(input))
                counts.computeIfPresent(pulse) { _, value -> value + 1 }
        }

        return counts.values.product()
    }

    override fun part2(input: ParsedInput): Long {
        input.values.forEach { it.reset() }
        val importantModules = input.values.single { "rx" in it.outputs }.sources
        val periods = mutableMapOf<String, Long>()
        var iteration = 0L
        while (iteration++ < 100_000 && periods.size < importantModules.size) {
            for (signal in pressButton(input).filter { it.pulse == Pulse.HIGH && it.source in importantModules }) {
                periods.putIfAbsent(signal.source, iteration)
            }
        }

        return periods.values.lcm()
    }

    fun pressButton(modules: ParsedInput): List<Signal> {
        val signal = Signal("button", "broadcaster", Pulse.LOW)
        var counter = 0
        val queue = mutableListOf(signal)
        while (counter < queue.size) {
            val current = queue[counter++]
            queue.addAll(modules[current.target]?.handle(current) ?: emptyList())
        }
        return queue
    }
}

fun main() {
    val puzzle = Day20()

    solveAndVerify({ puzzle.part1(puzzle.parse("sample.txt")) }, expected = 32000000)
    solveAndVerify({ puzzle.part1(puzzle.parse("sample2.txt")) }, expected = 11687500)

    puzzle.run()
}