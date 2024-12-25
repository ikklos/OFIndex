package site.sayaz.ofindex.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SimpleBooklist (
    @SerialName("booklistId")
    val booklistID: Long,

    val name: String
)