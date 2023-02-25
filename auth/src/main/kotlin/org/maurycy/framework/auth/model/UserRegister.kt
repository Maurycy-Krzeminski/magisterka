package org.maurycy.framework.auth.model

data class UserRegister(
    var userName: String,
    var email: String,
    var password: String,
    var roles: List<String>
)