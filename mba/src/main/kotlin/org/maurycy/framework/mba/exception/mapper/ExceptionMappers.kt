package org.maurycy.framework.mba.exception.mapper

import javax.ws.rs.core.Response
import org.jboss.resteasy.reactive.RestResponse

import org.jboss.resteasy.reactive.server.ServerExceptionMapper
import org.maurycy.framework.mba.exception.FailedToBuildObjectIdException
import org.maurycy.framework.mba.exception.FailedToFindByIdException
import org.maurycy.framework.mba.model.ExceptionDto


class ExceptionMappers {

    @ServerExceptionMapper
    fun mapExceptionFailedToBuildObjectIdException(x: FailedToBuildObjectIdException): RestResponse<ExceptionDto> {
        return RestResponse.status(Response.Status.BAD_REQUEST, ExceptionDto(x.message ?: "no message in exception"))
    }

    @ServerExceptionMapper
    fun mapExceptionFailedToFindByIdException(x: FailedToFindByIdException): RestResponse<Any> {
        return RestResponse.status(Response.Status.NO_CONTENT)
    }
}