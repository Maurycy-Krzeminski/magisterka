package org.maurycy.framework.mba.exception.mapper

import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider
import org.maurycy.framework.mba.exception.FailedToBuildObjectIdException
import org.maurycy.framework.mba.model.ExceptionDto

@Provider
class FailedToBuildObjectIdExceptionMapper: ExceptionMapper<FailedToBuildObjectIdException> {
    override fun toResponse(exception: FailedToBuildObjectIdException?): Response {
        return Response
            .status(Response.Status.BAD_REQUEST)
            .entity(ExceptionDto(exception?.message ?: "no message in exception"))
            .build()

    }
}