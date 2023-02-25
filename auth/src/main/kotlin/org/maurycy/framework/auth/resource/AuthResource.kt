package org.maurycy.framework.auth.resource

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional
import io.smallrye.mutiny.Uni
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.MediaType
import org.jboss.resteasy.reactive.RestResponse
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder
import org.maurycy.framework.auth.model.JWTToken
import org.maurycy.framework.auth.model.UserDto
import org.maurycy.framework.auth.model.UserLogin
import org.maurycy.framework.auth.model.UserRegister
import org.maurycy.framework.auth.service.AuthService


@Path("/api/auth")
class AuthResource(
    private val authService: AuthService
) {
    @POST
    @ReactiveTransactional
    fun register(userRegister: UserRegister): Uni<UserDto> {
        return authService.register(userRegister)
    }

    @POST
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun login(userLogin: UserLogin, @Context headers: HttpHeaders): RestResponse<JWTToken> {
        val jwt = authService.login(userLogin)
        return ResponseBuilder.ok(JWTToken(jwt))
            .header(
                "Authorization",
                "Bearer $jwt"
            )
            .build()
    }

    @GET
    suspend fun getAllUsers(): List<UserDto> {
        return authService.getAllUsers()
    }


}
