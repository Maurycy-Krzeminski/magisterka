package org.maurycy.framework.auth.repository

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepository
import io.smallrye.mutiny.Uni
import javax.enterprise.context.ApplicationScoped
import org.maurycy.framework.auth.table.RoleTable

@ApplicationScoped
class RoleRepository : PanacheRepository<RoleTable> {
    fun findByName(name: String): Uni<RoleTable> {
        return find("name", name).singleResult()
    }

    fun findByName(listName: List<String>): Uni<List<RoleTable>> {
        return list("name in ?1", listName)
    }
}