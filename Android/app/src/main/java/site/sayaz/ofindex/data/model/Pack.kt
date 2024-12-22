package site.sayaz.ofindex.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pack (
    val authorAvatar: String,
    val authorId: Long,
    val name: String,
    val packId: Long
)