package ru.netology

class ChatService {
    private var chatList = mutableListOf<Chat>()
    private var chatNewId = 0
    private val chatById = { chatId: Int ->
        chatList.find { it.id == chatId }
    }
    private val messageById = { chat: Chat, messageId: Int ->
        chat.messages.find { it.id == messageId }
    }
    private val filterChat = { userId: String ->
        chatList.filter {
            it.messages.any { m ->
                m.targetId == userId && !m.hasRead
            }
        }
    }
    private val instanceList = { list: List<Any> ->
        val stringBuilder = StringBuilder()
        for (instance in list)
            stringBuilder.append(instance)
        stringBuilder
    }

    fun sendMessage(senderId: String, targetId: String, text: String): Chat? {
        val chatExist = chatList.find {
            it.messages.find { m ->
                m.senderId == senderId ||
                m.senderId == targetId
            } != null
        }
        val addMessage = { chat: Chat, messageId: Int ->
            chat.copy(messages = (chat.messages + Message(
                                    id = messageId,
                                    senderId = senderId,
                                    targetId = targetId,
                                    text = text
                                  )).toMutableList(),
                      messageNewId = messageId + 1
            )
        }
        if (chatExist == null) {
            val newChat = Chat(id = chatNewId++)
            chatList.add(addMessage(newChat, newChat.messageNewId))
        } else {
            val index = chatList.indexOf(chatExist)
            val messageId = chatList[index].messageNewId
            chatList[index] = addMessage(chatExist, messageId)
        }
        return chatExist
    }

    fun deleteChat(chatId: Int): String {
        return if (chatList.remove(chatById(chatId)))
                    "Чат '$chatId' удален."
        else "Чата '$chatId' не существует."
    }

    fun editMessage(chatId: Int, messageId: Int, text: String): Boolean {
        val chat = chatList.find { it.id == chatId } ?: return false
        val message = chat.messages.find { it.id == messageId } ?: return false
        chatList[chatList.indexOf(chat)].messages[chat.messages.indexOf(message)] =
            message.copy(
                editTimeStamp = System.currentTimeMillis(),
                text = text
            )
        return true
    }

    fun deleteMessage(chatId: Int, messageId: Int): String {
        val chat = chatById(chatId) ?: return "Чата '$chatId' не существует."
        return if (chat.messages.remove(messageById(chat, messageId))) {
            if (chat.messages.isEmpty()) {
                chatList.remove(chat)
                "Чат '$chatId' удален полностью."
            } else
                "Сообщение '$messageId' удалено из чата '$chatId'."
        }
        else "Сообщения '$messageId' не существует."
    }

    fun getUnreadChats(userId: String): String {
        val unreadChats = filterChat(userId)
        return """СПИСОК ЧАТОВ С НЕПРОЧИТАННЫМИ СООБЩЕНИЯМИ ДЛЯ $userId:
            |${
                if (unreadChats.isNotEmpty())
                    instanceList(unreadChats)
                else "отсутствуют"
            }""".trimMargin()
    }

    fun getMessagesByChatId(userId: String, chatId: Int): String {
        val chat = chatById(chatId) ?: return "Чата '$chatId' не существует."
        val filterMessage = chat.messages.filter { it.targetId == userId && !it.hasRead }
        chat.messages.find {it.senderId == userId ||
                it.targetId == userId} ?: return "$userId не участвует в чате '$chatId'."
        filterMessage.forEach {
            chat.messages[chat.messages.indexOf(it)] =
                it.copy(hasRead = true)
        }
        return """ЧАТ '$chatId' СОДЕРЖИТ ${filterMessage.size} НЕПРОЧИТАННЫХ СООБЩЕНИЙ ДЛЯ $userId:
            |${instanceList(filterMessage)}""".trimMargin()
    }

    override fun toString(): String {
        return """СПИСОК ВСЕХ ЧАТОВ:
            |${instanceList(chatList)}""".trimMargin()
    }

    fun testRemoveChat(chatId: Int): Boolean {
        return chatList.remove(chatById(chatId))
    }

    fun isFindChatById(chatId: Int): Boolean {
        return chatById(chatId) != null
    }

    fun testRemoveMassage(chatId: Int, messageId: Int): Int {
        val chat = chatById(chatId)
        return if (chat?.messages?.remove(messageById(chat, messageId)) == true)
            if (chat.messages.isEmpty()) 2 else 1
        else 0
    }

    fun testFilterChat(userId: String): Boolean {
        return (filterChat(userId).isNotEmpty())
    }

    fun isUserIn(userId: String, chatId: Int): Boolean {
        return (chatById(chatId)?.messages?.find {it.senderId == userId ||
                it.targetId == userId} != null)
    }
}