package com.kuzmin.route

import com.kuzmin.Model.PostModel
import com.kuzmin.Repository.PostRepository
import com.kuzmin.dto.AuthenticationRequestDto
import com.kuzmin.dto.NewPostDto
import com.kuzmin.dto.PostRequestDto
import com.kuzmin.dto.PostResponseDto
import com.kuzmin.service.UserService
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.generic.instance
import org.kodein.di.ktor.kodein

class RoutingV1() {
    private lateinit var userService : UserService
    fun setup(configuration: Routing) {
        with(configuration) {
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
                    val response = repo.dislikeById(request.id) ?: throw NotFoundException()
                    call.respond(response)
                }
                get("/posts") {
                    val response = repo.getAll()
                    call.respond(response)
                }
                post("/new") {
                    val request = call.receive<NewPostDto>()
                    print(request.toString())
                    val response = repo.new(request.txt.toString(), request.author) ?: throw NotFoundException()
                    call.respond(response)
                }
                post("/authentication") {
                    val input = call.receive<AuthenticationRequestDto>()
                    val response = userService.addUser(input.name, input.password)
                    call.respond(response)
                }
                post("/registration") {
                    val input = call.receive<AuthenticationRequestDto>()
                    val response = userService.addUser(input.name, input.password)
                    call.respond(response)
                }
                get("/") {
                    call.respondText("Server working", ContentType.Text.Plain)
                }
            }
        }
    }
}