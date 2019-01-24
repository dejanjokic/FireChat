package hr.tvz.firechat.data.model

data class User(
    val id: String = "",
    val displayName: String? = "",
    val email: String? = "",
    val profilePicturePath: String? = ""
)