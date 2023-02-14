package org.maurycy.framework.mba

import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.awaitSuspending
import java.time.Duration
import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import kotlinx.coroutines.time.delay
import org.bson.types.ObjectId
import org.infinispan.client.hotrod.DefaultTemplate
import org.infinispan.client.hotrod.RemoteCache
import org.infinispan.client.hotrod.RemoteCacheManager
import org.jboss.resteasy.reactive.ResponseStatus
import org.maurycy.framework.mba.entities.DataDto
import org.maurycy.framework.mba.entities.DataDtoProto
import org.maurycy.framework.mba.entities.DataInput
import org.maurycy.framework.mba.repository.DataRepository

@Path("/data")
class DataResource(
    private val dataRepository: DataRepository,
    cacheManager: RemoteCacheManager
) {

    var cache: RemoteCache<String, DataDtoProto> =
        cacheManager.administration().getOrCreateCache("test", DefaultTemplate.DIST_SYNC)


    @GET
    fun getAll(): Uni<List<DataDto>> {
        return dataRepository.findAll().list()
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON) //TODO: make it idempotent
    @ResponseStatus(201)
    fun addData(aData: DataInput): Uni<DataDto> {
        return dataRepository.persist(DataDto(data = aData.data))
    }

    @DELETE
    @Path("{id}")
    @ResponseStatus(204)
    fun deleteData(@PathParam("id") aId: String): Uni<Void> {
        return dataRepository.deleteById(ObjectId(aId)).replaceWithVoid()
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun putData(@PathParam("id") aId: String, aData: DataInput): Uni<DataDto> {
        try {
            val id = ObjectId(aId)
            return dataRepository.findById(id).chain { it ->
                if (it == null) {
                    throw FailedToFindByIdException(id = id)
                }
                it.data = aData.data
                return@chain dataRepository.update(it)
            }
        } catch (aE: IllegalArgumentException) {
            throw FailedToBuildObjectIdException()
        }

    }

    @GET
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun getById(@PathParam("id") aId: String): DataDtoProto {
        return Uni.createFrom().item(cache.getOrPut(aId) {
            try {
                delay(Duration.ofMillis(2000))
                val id = ObjectId(aId)
                return dataRepository.findById(id).map {
                    if (it == null) {
                        throw FailedToFindByIdException(id)
                    }
                    cache[aId] = convertDataDtoToProto(it)
                    return@map convertDataDtoToProto(it)
                }.awaitSuspending()
            } catch (aE: IllegalArgumentException) {
                throw FailedToBuildObjectIdException()
            }
        }).awaitSuspending()
    }

    private fun convertDataDtoToProto(it: DataDto): DataDtoProto {
        return DataDtoProto(id = it.id?.toHexString(), data = it.data)
    }

}