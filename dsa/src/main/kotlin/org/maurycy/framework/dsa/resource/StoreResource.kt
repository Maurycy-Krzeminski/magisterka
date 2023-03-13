package org.maurycy.framework.dsa.resource

import io.smallrye.common.annotation.Blocking
import javax.annotation.security.RolesAllowed
import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.UriInfo
import org.jboss.resteasy.reactive.RestResponse
import org.maurycy.framework.dsa.model.FormData
import org.maurycy.framework.dsa.service.StoreService

@Path("store")
class StoreResource(
    private val storeService: StoreService,
) {
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user", "admin")
    suspend fun uploadFile(aFormData: FormData, @Context uriInfo: UriInfo): RestResponse<String> {
        val answer = storeService.storeFiles(aFormData = aFormData)
        return RestResponse.ResponseBuilder
            .created<String>(uriInfo.absolutePathBuilder.path(answer).build()).build()
    }

    @GET
    @Path("{name}")
    @Produces(MediaType.TEXT_PLAIN)
    @Blocking
    @RolesAllowed("user", "admin")
    fun downloadFile(@PathParam("name") aName: String): Response {
        val e = storeService.findFile(aFileName = aName)
        return Response.ok(
            e.readAllBytes()
        )
            .header("Content-Disposition", "attachment;filename=$aName")
            .header("Content-Type", e.headers().get("Content-Type"))
            .build()

    }

    @GET
    @RolesAllowed("user", "admin")
    fun getAll() = storeService.listObjects()

    @GET
    @Path("search/{input}")
    @RolesAllowed("user", "admin")
    fun searchForFile(@PathParam("input") aParamToSearchBy: String) = storeService.searchFull(aParamToSearchBy)

}