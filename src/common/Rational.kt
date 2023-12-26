package common

import java.math.BigInteger

data class Rational(val num: BigInteger, val den: BigInteger) : Comparable<Rational> {
    operator fun plus(other: Rational): Rational = Rational(num * other.den + other.num * den, den * other.den)
    operator fun minus(other: Rational): Rational = Rational(num * other.den - other.num * den, den * other.den)
    operator fun times(other: Rational): Rational = Rational(num * other.num, den * other.den)
    operator fun div(other: Rational): Rational = Rational(num * other.den, den * other.num)

    fun abs(): Rational = Rational(num.abs(), den.abs())

    companion object {
        fun from(d: Long): Rational = Rational(BigInteger.valueOf(d), BigInteger.ONE)
        fun from(d: Double): Rational = from(d.toLong())
        val zero = Rational(BigInteger.ZERO, BigInteger.ONE)
    }

    fun toLong(): Long {
        if (num % den == BigInteger.ZERO) return (num / den).toLong()
        throw RuntimeException("Cannot convert $this to Long")
    }
    override fun compareTo(other: Rational): Int = (num * other.den).compareTo(other.num * den)
}