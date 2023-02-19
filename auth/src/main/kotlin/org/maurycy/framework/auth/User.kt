package org.maurycy.framework.auth

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheEntity
import javax.persistence.Column
import javax.persistence.Entity

@Entity
open class User : PanacheEntity(){

    open lateinit var userName: String
    @Column(unique = true)
    open lateinit var email: String
    open lateinit var password: String
}

data class UserRegister(
    var userName: String?,
    var email: String?,
    var password: String?
)
data class UserLogin(
    var userName: String?,
    var password: String?
)
