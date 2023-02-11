package org.maurycy.framework.mba

import io.smallrye.mutiny.Uni
import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import org.bson.types.ObjectId
import org.jboss.resteasy.reactive.ResponseStatus

@Path("/data")
class DataResource(
    private val dataRepository: DataRepository
) {

    @GET
    fun getAll(): Uni<List<DataDto>> {
        return dataRepository.findAll().list()
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON) //TODO: make it idempotent
    @ResponseStatus(201)
    fun addData(aData: DataInput): Uni<DataDto> {
        return dataRepository.persist(DataDto(aData.data))
    }

    @DELETE
    @Path("{id}")
    fun deleteData(@PathParam("id") aId: String): Uni<Boolean> {
        return dataRepository.deleteById(ObjectId(aId))
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun putData(@PathParam("id") aId: String, aData: DataInput): Uni<DataDto> {
        return dataRepository.findById(ObjectId(aId)).map {
            if (it == null) {
                throw NullPointerException()
            }
            return@map it
        }
    }

    @GET
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun getById(@PathParam("id") aId: String): Uni<DataDto> {
        try {
            val id = ObjectId(aId)
            return dataRepository.findById(id).map {
                if (it == null) {
                    throw FailedToFindByIdException(id)
                }
                return@map it
            }
        } catch (aE: IllegalArgumentException) {
            throw FailedToBuildObjectIdException()
        }


    }


}