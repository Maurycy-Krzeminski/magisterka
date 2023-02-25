package org.maurycy.framework.auth.model

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
class JWTToken constructor(@field:JsonProperty("id_token") val idToken: String)