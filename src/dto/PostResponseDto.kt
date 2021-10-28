package com.kuzmin.dto

import com.kuzmin.Model.PostModel
import com.kuzmin.Model.PostType

data class PostResponseDto(
    val id: Long,
    val author: String,
    val data: Long = 0,
    val txt: String,
    var like: Boolean,
    val comment: Boolean,
    val share: Boolean,
    var likeTxt : Int,
    val commentTxt : Int,
    val shareTxt : Int,
    val adress : String,
    val coordinates : Pair<Double,Double>,
    val type: PostType = PostType.Reposts,
    val url: String? = null,
    val dateRepost: Long? = null,
    val autorRepost: String? = null,
    var hidePost: Boolean = false,
    var viewPost: Long = 0
) {
    companion object {
        fun fromModel(model: PostModel) = PostResponseDto(
           id = model.id,
            author = model.author,
            data = model.data,
            txt = model.txt,
            like = model.like,
            comment = model.comment,
            share = model.share,
            likeTxt = model.likeTxt,
            commentTxt = model.commentTxt,
            shareTxt = model.shareTxt,
            adress = model.adress,
            coordinates = model.coordinates,
            type = model.type,
            url = model.url,
            dateRepost = model.dateRepost,
            autorRepost = model.autorRepost,
            hidePost = model.hidePost,
            viewPost = model.viewPost
        )
    }
}