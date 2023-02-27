package org.maurycy.framework.dsa.exception.mapper

import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider
import org.maurycy.framework.dsa.exception.NoFileSentException

@Provider
class NoFileSentExceptionMapper : ExceptionMapper<NoFileSentException> {
    override fun toResponse(exception: NoFileSentException): Response {
        TODO("Not yet implemented")
    }
}