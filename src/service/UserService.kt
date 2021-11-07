package com.kuzmin.service

import com.kuzmin.Exception.PasswordChangeException
import com.kuzmin.Exception.UseraddException
import com.kuzmin.Model.UserModel
import com.kuzmin.Repository.UserRepository
import com.kuzmin.dto.AuthenticationRequestDto
import com.kuzmin.dto.AuthenticationResponseDto
import com.kuzmin.dto.PasswordChangeRequestDto
import com.kuzmin.dto.PostResponseDto.Companion.fromModel
import com.kuzmin.dto.UserResponseDto
import io.ktor.features.*
import org.springframework.security.crypto.password.PasswordEncoder

class UserService (
    private val repo: UserRepository,
    private val tokenService: JWTTokenService,
    private val passwordEncoder: PasswordEncoder
) {
    suspend fun getModelById(id: Long): UserModel? {
        return repo.getById(id)
    }

    suspend fun getById(id: Long): UserResponseDto {
        val model = repo.getById(id) ?: throw NotFoundException()
        return UserResponseDto.fromModel(model)
    }

    suspend fun changePassword(id: Long, input: PasswordChangeRequestDto) {

        val model = repo.getById(id) ?: throw NotFoundException()
        if (!passwordEncoder.matches(input.old, model.password)) {
            throw PasswordChangeException("Wrong password!")
        }
        val copy = model.copy(password = passwordEncoder.encode(input.new))
        repo.save(copy)
    }

    suspend fun authenticate(input: AuthenticationRequestDto): AuthenticationResponseDto {
        val model = repo.getByUsername(input.name) ?: throw NotFoundException()
        if (!passwordEncoder.matches(input.password, model.password)) {
            throw PasswordChangeException("Wrong password")
        }
        val token = tokenService.generate(model.id)

        return AuthenticationResponseDto(token)
    }

    suspend fun addUser(username: String, password: String): AuthenticationResponseDto {
        val model = UserModel(
            id = 0, //repo.getSizeListUser().toLong(),
            name = username,
            password = passwordEncoder.encode(password)
        )
        val checkingIsUser = repo.addUser(model)
        if (checkingIsUser) {
            val token = tokenService.generate(model.id)
            return AuthenticationResponseDto(token)
        }
        return throw UseraddException("Такой логин уже зарегистрирован")
    }
}

