package org.maurycy.framework.dsa

import io.quarkus.logging.Log
import io.smallrye.common.annotation.Blocking
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("store")
class StoreResource(
    private val storeService: StoreService,
) {
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    fun test(formData: FormData): String {
        return storeService.storeFiles(formData = formData)
    }

    @GET
    @Path("{name}")
    @Consumes
    @Produces(MediaType.TEXT_PLAIN)
    @Blocking
    fun get(@PathParam("name") name: String): Response {
        Log.info("searching file")
        val e = storeService.findFile(fileName = name)
        Log.info(e)
        return Response.ok(
            e.readAllBytes()
        )
            .header("Content-Disposition", "attachment;filename=$name")
            .header("Content-Type", e.headers().get("Content-Type"))
            .build()

    }

    @GET
    fun getAll() = storeService.listObjects()

    @GET
    @Path("search/{name}")
    fun getName(@PathParam("name") name: String) = storeService.searchByName(name);

}