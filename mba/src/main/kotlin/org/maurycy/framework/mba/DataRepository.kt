package org.maurycy.framework.mba

import io.quarkus.mongodb.panache.kotlin.reactive.ReactivePanacheMongoRepository
import io.smallrye.mutiny.Uni
import javax.enterprise.context.ApplicationScoped
import org.bson.types.ObjectId

@ApplicationScoped
class DataRepository: ReactivePanacheMongoRepository<DataDto> {
    fun findById(aId: String): Uni<DataDto?> {
        return findById(ObjectId(aId))
    }
}