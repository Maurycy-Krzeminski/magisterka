package org.maurycy.framework.auth

import org.maurycy.framework.auth.table.RoleTable

data class RoleDto(
    var name: String,
    var description: String
) {
    fun roleTable(): RoleTable {
        return RoleTable(name = this.name, description = this.description)
    }
}