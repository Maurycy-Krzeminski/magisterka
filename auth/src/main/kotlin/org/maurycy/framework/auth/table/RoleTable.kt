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
import org.maurycy.framework.auth.RoleDto

@Entity
@Table(name = "user_role")
data class RoleTable(
    @Column(unique = true)
    var name: String = "",
    var description: String = "",
    @ManyToMany(cascade = [CascadeType.MERGE], fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_role_user",
        joinColumns = [JoinColumn(name = "user_role_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")],
    )
    var users: List<UserTable> = emptyList()

) : PanacheEntity() {
    fun roleDto(): RoleDto {
        return RoleDto(name = this.name, description = this.description)
    }
}