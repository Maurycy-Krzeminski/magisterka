package org.maurycy.framework.mba.entities

import io.quarkus.mongodb.panache.common.MongoEntity
import io.quarkus.mongodb.panache.kotlin.PanacheMongoEntityBase
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId


@MongoEntity(collection = "StoredData")
data class DataDto(
    @BsonId
    var id: ObjectId? = null,
    var data: Map<String, String>? = null
) : PanacheMongoEntityBase()