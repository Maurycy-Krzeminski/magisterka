package org.maurycy.framework.auth.resource

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional
import io.smallrye.mutiny.Uni
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import org.maurycy.framework.auth.model.RoleDto
import org.maurycy.framework.auth.repository.RoleRepository
import org.maurycy.framework.auth.table.RoleTable

@Path("roles")
class RolesResource(
    private val roleRepository: RoleRepository
) {
    @GET
    @ReactiveTransactional
    fun getAllRoles(): Uni<List<RoleDto>> {
        return roleRepository.findAll().list().map { listRoleTable: List<RoleTable> ->
            listRoleTable.map(RoleTable::roleDto)
        }
    }

    @POST
    @ReactiveTransactional
    fun addRole(roleDto: RoleDto): Uni<RoleTable> {
        val role = roleDto.roleTable()
        return roleRepository.persist(role)
    }
}