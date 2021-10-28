package com.kuzmin.Repository

import com.kuzmin.Model.PostModel
import sun.awt.Mutex
import java.util.concurrent.CopyOnWriteArrayList

class PostRepositoryInMemoryConcurrentImpl : PostRepository {
    private var nextId = 1L
    private val items = CopyOnWriteArrayList<PostModel>()
    private val mutex = Mutex()

    override suspend fun getAll(): List<PostModel> {
        return items.reversed()
    }
    override suspend fun getById(id: Long): PostModel? {
        return items.find { it.id == id }
    }
    override suspend fun save(item: PostModel): PostModel {
        return when (val index = items.indexOfFirst { it.id == item.id }) {
            -1 -> {
                val copy = item.copy(id = nextId++)
                items.add(copy)
                copy
            }
            else -> {
                items[index] = item
                item
            }
        }
    }
    override suspend fun removeById(id: Long) {
        items.removeIf { it.id == id }
    }

    override suspend fun likeById(id: Long): PostModel? {
       return when (val index = items.indexOfFirst { it.id == id }){
            -1 -> null
           else -> {
               val item = items[index]
               val copy = item.copy(likeTxt = item.likeTxt + 1)
               items[index] = copy
               copy
           }
           }
       }

    override suspend fun dislikeById(id: Long): PostModel? {
        return when (val index = items.indexOfFirst { it.id == id }){
            -1 -> null
            else -> {
                val item = items[index]
                val copy = item.copy(likeTxt = item.likeTxt - 1)
                items[index] = copy
                copy
            }
        }
    }
}

