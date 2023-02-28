package org.maurycy.framework.math.exception.mapper

import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider
import org.apache.commons.math3.exception.DimensionMismatchException
import org.maurycy.framework.math.model.ExceptionDto

@Provider
class DimensionMismatchExceptionMapper : ExceptionMapper<DimensionMismatchException> {
    override fun toResponse(exception: DimensionMismatchException): Response {
        return Response.status(Response.Status.BAD_REQUEST).entity(ExceptionDto(exception.localizedMessage)).build()
    }
}