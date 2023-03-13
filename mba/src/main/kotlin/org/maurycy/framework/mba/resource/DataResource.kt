package org.maurycy.framework.mba.resource

import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.awaitSuspending
import javax.annotation.security.RolesAllowed
import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import org.jboss.resteasy.reactive.ResponseStatus
import org.maurycy.framework.mba.exception.FailedToFindByIdException
import org.maurycy.framework.mba.model.DataDto
import org.maurycy.framework.mba.model.DataInput
import org.maurycy.framework.mba.repository.DataRepository

@Path("/data")
class DataResource(
    private val dataRepository: DataRepository,
) {



    @GET
    @RolesAllowed("user", "admin")
    fun getAll(): Uni<List<DataDto>> {
        return dataRepository.findAll().list()
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON) //TODO: make it idempotent
    @ResponseStatus(201)
    @RolesAllowed("user", "admin")
    fun addData(aData: DataDto): Uni<DataDto> {
        return dataRepository.persist(aData)
    }

    @DELETE
    @Path("{id}")
    @ResponseStatus(204)
    @RolesAllowed("user", "admin")
    fun deleteData(@PathParam("id") aId: String): Uni<Void> {
        return dataRepository.deleteById(aId).replaceWithVoid()
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user", "admin")
    fun putData(@PathParam("id") aId: String, aData: DataInput): Uni<DataDto> {
        return dataRepository.findById(aId).chain { it ->
            if (it == null) {
                throw FailedToFindByIdException(id = aId)
            }
            it.data = aData.data
            return@chain dataRepository.update(it)
        }

    }

    @GET
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user", "admin")
    suspend fun getById(@PathParam("id") aId: String): DataDto {
            return dataRepository.findById(aId).map {
                if (it == null) {
                    throw FailedToFindByIdException(aId)
                }
                return@map it
            }.awaitSuspending()

    }


}