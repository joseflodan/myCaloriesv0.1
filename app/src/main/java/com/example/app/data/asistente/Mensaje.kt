package com.example.app.data.asistente

import com.example.app.utils.Roles
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Mensaje(
    var id          : String?            = null,
    @SerialName("created_at")
    var createdAt   : Int?               = null,
    var assistantId : String?            = null,
    var threadId    : String?            = null,
    var runId       : String?            = null,
    var role        : Roles?             = null,
    var content     : ArrayList<Content> = arrayListOf(),
    var attachments : ArrayList<String>  = arrayListOf(),
)

@Serializable
data class Content (
    var type : String? = null,
    var text : Text?   = Text()
)

@Serializable
data class Text (
    var value       : String?           = null,
    var annotations : ArrayList<String> = arrayListOf()
)
