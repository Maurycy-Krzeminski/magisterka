package org.maurycy.framework.mba.entities

import io.quarkus.mongodb.panache.common.MongoEntity
import io.quarkus.mongodb.panache.kotlin.PanacheMongoEntityBase
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId


@MongoEntity(collection = "StoredData")
class DataDto : PanacheMongoEntityBase {
    @BsonId
    var id: ObjectId? = null
    var data: Map<String, String>? = null

    constructor(aData: Map<String, String>?) {
        data = aData
    }

    constructor(id: ObjectId?, data: Map<String, String>?) {
        this.id = id
        this.data = data
    }

    constructor()
    constructor(aId: String?, aData: DataInput) {
        DataDto(ObjectId(aId), aData.data)
    }
}