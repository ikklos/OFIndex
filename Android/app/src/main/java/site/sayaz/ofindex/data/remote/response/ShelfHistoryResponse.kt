// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json                 = Json { allowStructuredMapKeys = true }
// val shelfHistoryResponse = json.parse(ShelfHistoryResponse.serializer(), jsonString)

package site.sayaz.ofindex.data.remote.response

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

/*
阅读历史
{
    "count": 1,
    "items": [
        {
            "bookId": 0,
            "addTime": "2024-12-20T17:21:45",
            "name": "LearnCSharp"
        }
    ]
}
 */
@Serializable
data class ShelfHistoryResponse(
    val count: Long,
    val items: List<ShelfHistoryItem>,
    val message: String? = null,
    val result: Boolean
)

@Serializable
data class ShelfHistoryItem(
    val addTime: String? = null,
    val bookId: Long? = null,
    val name: String? = null
)