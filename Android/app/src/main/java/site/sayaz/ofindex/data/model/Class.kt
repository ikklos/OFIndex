package site.sayaz.ofindex.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Class (
    val id: Long,
    val name: String
)