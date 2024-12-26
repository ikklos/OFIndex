package site.sayaz.ofindex.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SimpleBooklist (
    val booklistId: Long,
    val name: String
)