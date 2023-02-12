package org.maurycy.framework.dsa

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.jboss.resteasy.reactive.RestForm
import org.jboss.resteasy.reactive.multipart.FileUpload


@Schema(type = SchemaType.STRING, format = "binary")
interface UploadItemSchema

// Class that will be used to define the request body, and with that
// it will allow uploading of "N" files
class UploadFormSchema {
    var files: List<UploadItemSchema>? = null
}

// We instruct OpenAPI to use the schema provided by the 'UploadFormSchema'
// class implementation and thus define a valid OpenAPI schema for the Swagger
// UI
@Schema(implementation = UploadFormSchema::class)
class FormData {
    @RestForm("files")
    var files: List<FileUpload>? = null
}