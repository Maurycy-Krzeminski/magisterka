package org.maurycy.framework.mba.model

import org.infinispan.protostream.annotations.ProtoField

data class DataDtoProto(
    @get:ProtoField(1)
    var id: String? = null,
    @get:ProtoField(2, javaType = HashMap::class)
    var data: Map<String, String>? = null
)