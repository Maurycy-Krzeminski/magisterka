package org.maurycy.framework.math.exception.mapper

import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider
import org.apache.commons.math3.linear.NonSquareMatrixException
import org.maurycy.framework.math.model.ExceptionDto

@Provider
class MyExceptionMapper : ExceptionMapper<NonSquareMatrixException> {
    override fun toResponse(exception: NonSquareMatrixException): Response? {
        return Response.status(Response.Status.BAD_REQUEST).entity(ExceptionDto(exception.localizedMessage)).build()
    }
}