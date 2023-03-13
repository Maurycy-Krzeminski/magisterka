package org.maurycy.framework.math.resource

import javax.annotation.security.RolesAllowed
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import org.maurycy.framework.math.exception.EquationInputNullException
import org.maurycy.framework.math.model.EquationAnswer
import org.maurycy.framework.math.model.EquationInput
import org.maurycy.framework.math.service.EquationService

@Path("/equation")
class EquationResource(
    private val equationService: EquationService
){

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user", "admin")
    suspend fun solve(aInput: EquationInput?): EquationAnswer {
        return aInput?.let { equationService.solve(aInput = it) } ?: throw EquationInputNullException()
    }


}