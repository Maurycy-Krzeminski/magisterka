package org.maurycy.framework.dsa.exception.mapper

import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider
import org.maurycy.framework.dsa.exception.TooManyFilesSentException

@Provider
class TooManyFilesSentExceptionMapper : ExceptionMapper<TooManyFilesSentException> {
    override fun toResponse(exception: TooManyFilesSentException): Response {
        TODO("Not yet implemented")
    }
}