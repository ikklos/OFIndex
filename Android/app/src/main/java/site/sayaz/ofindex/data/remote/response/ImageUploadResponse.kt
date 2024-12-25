package site.sayaz.ofindex.data.remote.response

data class ImageUploadResponse (
    val success: Boolean,
    val code: String,
    val message: String,
    val data: ImageData,
    val RequestId: String
)

data class ImageData (
    val file_id: Int,
    val width: Int,
    val height: Int,
    val filename: String,
    val storename: String,
    val size: Int,
    val path: String,
    val hash: String,
    val url: String,
    val delete: String,
    val page: String
)
/*
{
    "success": true,
    "code": "success",
    "message": "Upload success.",
    "data": {
        "file_id": 0,
        "width": 4677,
        "height": 3307,
        "filename": "luo.jpg",
        "storename": "D5VpWCKFElUsPcR.jpg",
        "size": 801933,
        "path": "/2019/12/16/D5VpWCKFElUsPcR.jpg",
        "hash": "Q6vLIbCGZojrMhO2e7BmgFuXRV",
        "url": "https://vip1.loli.net/2019/12/16/D5VpWCKFElUsPcR.jpg",
        "delete": "https://sm.ms/delete/Q6vLIbCGZojrMhO2e7BmgFuXRV",
        "page": "https://sm.ms/image/D5VpWCKFElUsPcR"
    },
    "RequestId": "8A84DDCA-96B3-4363-B5DF-524E95A5201A"
}
 */