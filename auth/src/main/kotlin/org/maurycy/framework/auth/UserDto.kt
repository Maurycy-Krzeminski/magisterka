package org.maurycy.framework.auth

import org.maurycy.framework.auth.table.UserTable

data class UserDto(
    var userName: String,
    var email: String,
    var roles: List<RoleDto>
) {
    constructor(userTable: UserTable, roles: List<RoleDto>) : this(
        userName = userTable.userName,
        email = userTable.email,
        roles = roles
    )
}