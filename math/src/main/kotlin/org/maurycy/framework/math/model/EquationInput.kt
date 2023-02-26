package org.maurycy.framework.math.model

import java.io.Serializable
import org.maurycy.framework.math.enums.Decomposition


data class EquationInput(
    val coefficients: Array<DoubleArray>,
    val constants: Array<Double>,
    val decomposition: Decomposition
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EquationInput

        if (!coefficients.contentDeepEquals(other.coefficients)) return false
        if (!constants.contentEquals(other.constants)) return false
        if (decomposition != other.decomposition) return false

        return true
    }

    override fun hashCode(): Int {
        var result = coefficients.contentDeepHashCode()
        result = 31 * result + constants.contentHashCode()
        result = 31 * result + decomposition.hashCode()
        return result
    }

    override fun toString(): String {
        return "EquationInput(coefficients=${coefficients.contentDeepToString()}, constants=${constants.contentToString()}, decomposition=$decomposition)"
    }
}


