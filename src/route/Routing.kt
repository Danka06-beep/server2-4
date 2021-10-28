package com.kuzmin.route

import com.kuzmin.Model.PostModel
import com.kuzmin.Repository.PostRepository
import com.kuzmin.dto.PostRequestDto
import com.kuzmin.dto.PostResponseDto
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*


fun Routing.v1() {
    route("/api/v1/posts") {
        val repo by kodein().instance<PostRepository>()

        get {
            val response = repo.getAll().map { PostResponseDto.fromModel(it) }
            call.respond(response)
        }
        get("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
            val model = repo.getById(id) ?: throw NotFoundException()
            val response = PostResponseDto.fromModel(model)
            call.respond(response)
        }
        post {
            val input = call.receive<PostRequestDto>()
            val model = PostModel(id = input.id, author = input.author)
            val response = PostResponseDto.fromModel(repo.save(model))
            call.respond(response)
        }
        delete("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
            repo.removeById(id)
            call.respond(HttpStatusCode.NoContent)
        }
    }
    }