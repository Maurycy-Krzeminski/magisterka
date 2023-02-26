package org.maurycy.framework.math.model

data class EquationAnswer(val solution: DoubleArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EquationAnswer

        if (!solution.contentEquals(other.solution)) return false

        return true
    }

    override fun hashCode(): Int {
        return solution.contentHashCode()
    }
}