package org.maurycy.framework.mba.proto

import javax.enterprise.context.ApplicationScoped
import org.infinispan.protostream.GeneratedSchema
import org.infinispan.protostream.annotations.AutoProtoSchemaBuilder
import org.maurycy.framework.mba.model.DataDtoProto

@ApplicationScoped
@AutoProtoSchemaBuilder(
    includeClasses = [DataDtoProto::class, MapAdapter::class, MapAdapter.Entry::class],
    schemaPackageName = "tutorial"
)
interface ProtoSchema : GeneratedSchema