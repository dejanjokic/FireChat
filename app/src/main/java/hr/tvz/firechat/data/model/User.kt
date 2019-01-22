package hr.tvz.firechat.data.model

// TODO: providerId ???
data class User(
    val id: String = "",
    val displayName: String? = "",
    val email: String? = "",
    val profilePicturePath: String? = "",
    val tokens: List<String> = emptyList()
)