package com.kuzmin.route

import com.kuzmin.Model.PostModel
import com.kuzmin.Repository.PostRepository
import com.kuzmin.dto.PostRequestDto
import com.kuzmin.dto.PostResponseDto
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.generic.instance
import org.kodein.di.ktor.kodein
fun Routing.v1() {
    route("/api/v1") {
        val repo by kodein().instance<PostRepository>()
        get {
            val response = repo.getAll().map { PostResponseDto.fromModel(it) }
            call.respond(response)
        }
        get("/{id}") {
            val id =
                call.parameters["id"]?.toLongOrNull()
                    ?: throw ParameterConversionException("id", "Long")
            val model = repo.getById(id) ?: throw NotFoundException()
            val response = PostResponseDto.fromModel(model)
            call.respond(response)
        }
        post("/like") {
            val request = call.receive<PostRequestDto>()
            val response = repo.likeById(request.id) ?: throw NotFoundException()
            call.respond(response)
        }
        post("/dislike") {
            val request = call.receive<PostRequestDto>()
            val response = repo.dislikeById(request.id ) ?: throw NotFoundException()
            call.respond(response)
        }
        get("/posts") {
            val response = repo.getAll()
            call.respond(response)
        }
        get("/new"){
            val request = call.receive<PostResponseDto>()
            print(request.toString())
            val response = repo.new(request.txt.toString(), request.author) ?: throw NotFoundException()
            call.respond(response)
        }
    }
    }