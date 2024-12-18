package site.sayaz.ofindex.data.remote.request

data class LoginRequest(
    val userid: Int,
    val passwd: String
)

data class RegisterRequest(
    val username: String,
    val passwd: String
)