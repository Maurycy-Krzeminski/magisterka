package org.maurycy.framework.auth

import io.smallrye.jwt.build.Jwt
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.awaitSuspending
import javax.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.jwt.Claims

@ApplicationScoped
class AuthService(
    private val userRepository: UserRepository
) {
    private fun securePassword(password: String): String {
        //TODO: hash passwords
        return  password
    }

    fun register(userRegister: UserRegister): Uni<User> {
        val password = securePassword(userRegister.password!!)
        val user = User()
        user.userName = userRegister.userName!!
        user.password = password
        user.email = userRegister.email!!
        return userRepository.persist(user)
    }

    fun login(userLogin: UserLogin): String {
        return Jwt.issuer("https://example.com/issuer")
            .upn(userLogin.userName)
            .groups(HashSet(listOf("User", "Admin")))
            .claim(Claims.birthdate.name, "2001-07-13")
            .sign()
    }

    suspend fun getAllUsers(): List<User> {
        return userRepository.findAll().list().awaitSuspending()
    }


}