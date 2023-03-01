package org.maurycy.framework.mba.exception.mapper

import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider
import org.maurycy.framework.mba.exception.FailedToFindByIdException

@Provider
class FailedToFindByIdExceptionMapper : ExceptionMapper<FailedToFindByIdException> {
    override fun toResponse(exception: FailedToFindByIdException?): Response {
        return Response.status(Response.Status.NO_CONTENT).build()
    }
}