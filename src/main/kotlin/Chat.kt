package ru.netology

data class Chat(
    val id: Int,
    val messages: MutableList<Message> = mutableListOf(),
    val messageNewId: Int = 0
) {
    override fun toString(): String {
        return """Чат '$id':
            |участники - ${messages[0].senderId}, ${messages[0].targetId},
            |количество сообщений - ${messages.size}.
            |
            |""".trimMargin()
    }
}
