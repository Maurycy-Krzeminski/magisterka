package org.maurycy.framework.math.proto

import javax.enterprise.context.ApplicationScoped
import org.infinispan.protostream.GeneratedSchema
import org.infinispan.protostream.annotations.AutoProtoSchemaBuilder

@ApplicationScoped
@AutoProtoSchemaBuilder(includeClasses = [], schemaPackageName = "tutorial")
interface ProtoSchema : GeneratedSchema