package org.maurycy.framework.mba.repository

import io.quarkus.mongodb.panache.kotlin.reactive.ReactivePanacheMongoRepository
import io.smallrye.mutiny.Uni
import javax.enterprise.context.ApplicationScoped
import org.bson.types.ObjectId
import org.maurycy.framework.mba.DataDto

@ApplicationScoped
class DataRepository : ReactivePanacheMongoRepository<DataDto> {
    fun findById(aId: String): Uni<DataDto?> {
        return findById(ObjectId(aId))
    }
}