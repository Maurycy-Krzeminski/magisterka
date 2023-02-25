package org.maurycy.framework.auth

import io.quarkus.logging.Log
import io.smallrye.jwt.build.Jwt
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.awaitSuspending
import javax.enterprise.context.ApplicationScoped
import org.maurycy.framework.auth.repository.RoleRepository
import org.maurycy.framework.auth.repository.UserRepository
import org.maurycy.framework.auth.table.RoleTable
import org.maurycy.framework.auth.table.UserTable

@ApplicationScoped
class AuthService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository
) {
    private fun securePassword(password: String): String {
        //TODO: hash passwords
        return password
    }

    fun addRole(aName: String, aDescription: String): Uni<RoleTable> {
        return roleRepository.persist(
            RoleTable(name = aName, description = aDescription)
        )
    }

    fun register(userRegister: UserRegister): Uni<UserDto> {
        val password = securePassword(userRegister.password)
        val user = UserTable()
        user.userName = userRegister.userName
        user.password = password
        user.email = userRegister.email
        return roleRepository.findByName(userRegister.roles).chain { roleTableList ->
            Log.info("user register roles size ${userRegister.roles.size}")
            Log.info("size of roles: ${roleTableList.size}")
            user.roles = roleTableList
            return@chain userRepository.persist(user).map { userTable->
                val roles = userTable.roles.map(RoleTable::roleDto)
                return@map UserDto(userTable, roles)
            }
        }
    }

    suspend fun login(userLogin: UserLogin): String {
        val user = checkUser(userLogin)
        return Jwt.issuer("https://example.com/issuer")
            .upn(user.userName)
            .groups(setOf())
            .sign()
    }

    private suspend fun checkUser(userLogin: UserLogin): UserTable {
        return userRepository.findByUserName(userLogin.userName).awaitSuspending()
    }

    suspend fun getAllUsers(): List<UserDto> {
        return userRepository.findAll().list().awaitSuspending().map (UserTable::userDto)
    }


}