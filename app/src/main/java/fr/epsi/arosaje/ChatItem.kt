package fr.epsi.arosaje

data class ChatItem(
    val id: Int,
    val userId: Int,
    val messageText: String,
    val timestamp: String
)