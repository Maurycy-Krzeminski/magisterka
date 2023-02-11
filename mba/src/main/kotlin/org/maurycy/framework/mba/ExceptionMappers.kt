package org.maurycy.framework.mba

import javax.ws.rs.core.Response
import org.jboss.resteasy.reactive.RestResponse

import org.jboss.resteasy.reactive.server.ServerExceptionMapper


internal class ExceptionMappers {

    @ServerExceptionMapper
    fun mapExceptionFailedToBuildObjectIdException(x: FailedToBuildObjectIdException): RestResponse<ExceptionDto> {
        return RestResponse.status(Response.Status.BAD_REQUEST, ExceptionDto(x.message ?: "no message in exception"))
    }

    @ServerExceptionMapper
    fun mapExceptionFailedToFindByIdException(x: FailedToFindByIdException): RestResponse<Any> {
        return RestResponse.status(Response.Status.NO_CONTENT)
    }
}