package site.sayaz.ofindex.data.model

import kotlinx.serialization.Serializable

/*
{
  "note": [
    {
      "id": "67dc8bae-4343-4de7-8fd8-268574eed106",
      "name": "1111",
      "rect": null,
      "children": [

      ]
    },
    {
      "id": "cbd6e4ce-a5e8-4ccb-90d2-ec580717fbac",
      "name": "1111",
      "rect": {
        "page": 1,
        "x": 100,
        "y": 200,
        "width": 300,
        "height": 400
      },
      "children": [

      ]
    }
  ],
  "diagram": {
    "nodeData": {
      "id": "c9ee977189f3b1f1",
      "topic": "Root",
      "root": true,
      "children": [

      ]
    }
  }
}
*/


@Serializable
data class PackContent(
    val note: List<Note>,
    val diagram: Diagram
)

@Serializable
data class Note(
    val id: String,
    val name: String,
    val rect: Rect?,
    val children: List<Note>
)

@Serializable
data class Rect(
    val page: Int,
    val x: Double,
    val y: Double,
    val width: Double,
    val height: Double
)

@Serializable
data class Diagram(
    val nodeData: NodeData
)

@Serializable
data class NodeData(
    val id: String,
    val topic: String,
    val root: Boolean? = null,
    val children: List<NodeData>? = null
)



