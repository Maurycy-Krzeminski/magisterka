package org.maurycy.framework.auth.table

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheEntity
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.Table
import org.maurycy.framework.auth.model.UserDto

@Entity
@Table(name = "users")
data class UserTable(
    @Column(unique = true)
    var userName: String = "",
    @Column(unique = true)
    var email: String = "",
    var hash: String = "",

    @ManyToMany(cascade = [CascadeType.MERGE], fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_role_user",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "user_role_id")]
    )
    var roles: List<RoleTable> = emptyList()
) : PanacheEntity() {
    fun userDto(): UserDto {
        return UserDto(
            userName = this.userName,
            email = this.email,
            roles = this.roles.map(RoleTable::roleDto)
        )
    }
}

