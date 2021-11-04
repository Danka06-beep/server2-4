package com.kuzmin.Model

import io.ktor.auth.*

data class UserModel (
    val id : Long,
    val name : String,
    val password : String
        ): Principal {
}