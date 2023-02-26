package org.maurycy.framework.dsa.exception.mapper

import io.minio.errors.ErrorResponseException
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider
import org.maurycy.framework.dsa.model.ExceptionDto

@Provider
class MinioErrorExceptionMapper : ExceptionMapper<ErrorResponseException> {
    override fun toResponse(exception: ErrorResponseException): Response {
        return Response.status(Response.Status.BAD_REQUEST).entity(ExceptionDto(exception.localizedMessage)).build()
    }
}


