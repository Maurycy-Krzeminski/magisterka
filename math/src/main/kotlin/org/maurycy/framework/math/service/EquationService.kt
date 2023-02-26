package org.maurycy.framework.math.service


import javax.enterprise.context.ApplicationScoped
import org.apache.commons.math3.linear.Array2DRowRealMatrix
import org.apache.commons.math3.linear.ArrayRealVector
import org.apache.commons.math3.linear.CholeskyDecomposition
import org.apache.commons.math3.linear.DecompositionSolver
import org.apache.commons.math3.linear.EigenDecomposition
import org.apache.commons.math3.linear.LUDecomposition
import org.apache.commons.math3.linear.QRDecomposition
import org.apache.commons.math3.linear.RRQRDecomposition
import org.apache.commons.math3.linear.SingularValueDecomposition
import org.maurycy.framework.math.enums.Decomposition
import org.maurycy.framework.math.model.EquationAnswer
import org.maurycy.framework.math.model.EquationInput

@ApplicationScoped
class EquationService {

    fun solve(aInput: EquationInput): EquationAnswer {

        val solver = decompositionSolver(aInput)
        return EquationAnswer(solver.solve(ArrayRealVector(aInput.constants)).toArray())
    }

    private fun decompositionSolver(aInput: EquationInput): DecompositionSolver =
        when (aInput.decomposition) {
            Decomposition.LUDecomposition -> {
                LUDecomposition(Array2DRowRealMatrix(aInput.coefficients)).solver
            }

            Decomposition.CholeskyDecomposition -> {
                CholeskyDecomposition(Array2DRowRealMatrix(aInput.coefficients)).solver
            }

            Decomposition.EigenDecomposition -> {
                EigenDecomposition(Array2DRowRealMatrix(aInput.coefficients)).solver

            }

            Decomposition.QRDecomposition -> {
                QRDecomposition(Array2DRowRealMatrix(aInput.coefficients)).solver

            }

            Decomposition.RRQRDecomposition -> {
                RRQRDecomposition(Array2DRowRealMatrix(aInput.coefficients)).solver
            }

            Decomposition.SingularValueDecomposition -> {
                SingularValueDecomposition(Array2DRowRealMatrix(aInput.coefficients)).solver
            }
        }
}