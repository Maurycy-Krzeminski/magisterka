package org.maurycy.framework.auth.repository

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepository
import io.smallrye.mutiny.Uni
import javax.enterprise.context.ApplicationScoped
import org.maurycy.framework.auth.table.UserTable

@ApplicationScoped
class UserRepository : PanacheRepository<UserTable> {
    fun findByUserName(userName: String): Uni<UserTable> {
        return find("username", userName).singleResult()
    }
}