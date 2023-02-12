package org.maurycy.framework.dsa

import io.minio.errors.ErrorResponseException
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class MinioErrorExceptionMapper : ExceptionMapper<ErrorResponseException> {
    override fun toResponse(exception: ErrorResponseException): Response {
        return Response.status(Response.Status.BAD_REQUEST).entity(ExceptionMessage(exception.localizedMessage)).build()
    }
}


data class ExceptionMessage(val exceptionMessage: String)
