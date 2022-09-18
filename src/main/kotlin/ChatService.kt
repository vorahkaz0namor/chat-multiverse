package ru.netology

class ChatService {
    private var chatList = mutableListOf<Chat>()
    private var chatNewId = 0
    private val chatById = { chatId: Int ->
        chatList.asSequence().withIndex()
            .find { it.value.id == chatId }
    }
    private val messageById = { chat: Chat, messageId: Int ->
        chat.messages.asSequence().withIndex()
            .find { it.value.id == messageId }
    }
    private val filterChat = { userId: String ->
        chatList.asSequence()
            .filter {
                it.messages.any { m ->
                    m.targetId == userId && !m.hasRead
                }
            }
            .toList()
    }
    private val instanceList = { list: List<Any> ->
        val stringBuilder = StringBuilder()
        for (instance in list)
            stringBuilder.append(instance)
        stringBuilder
    }
    init {
        val addChat = { users: MutableList<String>, messages: List<String> ->
            for (m in messages) {
                this.sendMessage(users[0], users[1], m)
                val temp = users[0]
                users[0] = users[1]
                users[1] = temp
            }
        }

        val usersChatOne = mutableListOf("Дмитрий", "Артем")
        val messagesChatOne = listOf(
            "Добрый вечер. Делаю ДЗ на тему дженериков и решил добавить в WallService функцию удаления поста из коллекции. Что делаю не так?",
            "Здравствуйте, Дмитрий! Что-то на глаз не вижу проблем. Разве что можно попробовать posts.remove(target), чтобы лишний раз индекс не считать. Если не в этом дело, то может не тот id пытаетесь удалить?",
            "Изменил на posts.remove(target). На выходе все равно false",
            "У вас в posts два поста с id 1 и 2, а удалить вы пытаетесь пост с id 0. поэтому не удаляет",
            "Спасибо! Забыл что ID генерирует WallService в моем коде и нужно использовать его."
        )

        val usersChatTwo = mutableListOf("Ксения", "Михаил")
        val messagesChatTwo = listOf(
            "Как думаете почему выражение может не работать тут: else if (musicAddict === true && purchase < 1001) println(Сумма покупки со скидкой: discountMusicAddict3)",
            "а чуть больше контекста можно? откуда else if? перед ним что идёт?",
            "Перед ним идет сначала иф, потом несколько элс ифов.",
            "логично. но я имел в виду, что там, возможно, кроется корень проблемы. может до этого вот самого else if просто не доходит. дебаггером не пробовали пройтись по шагам?",
            "Мне не помогает. Я не понимаю, чего от меня хотят. Короче я сначала объявила переменные, в которых прописала сумму покупки, буль со значениеи тру, и несколько переменных с разными скидками. Далее пошли ифы с условиями по сумме покупки. Все скидки выводятся в консоль нормально при изменении суммы покупки, а на буль ноль внимания"
        )

        val usersChatThree = mutableListOf("Татьяна", "Наталья")
        val messagesChatThree = listOf(
            "Здравствуйте, подскажите, что не так?",
            "Я не могу точно сказать, почему не так, но опишу что мне не понятно в вашем коде. 1 В параметрах вашей функции status, а внутри функции userstatus. 2 получается параметр статус не   задействован, только time : Int, а в вашей функции результат String, значит нужно указать что это String"
        )

        addChat(usersChatOne, messagesChatOne)
        addChat(usersChatTwo, messagesChatTwo)
        addChat(usersChatThree, messagesChatThree)
    }

    fun sendMessage(senderId: String, targetId: String, text: String): Chat? {
        val chatExist = chatList.withIndex()
            .find {
                it.value.messages
                    .find { m ->
                        m.senderId == senderId ||
                        m.senderId == targetId
                    } != null
            }
        val addMessage = { chat: Chat ->
            chat.messages.add(
                Message(
                    id = chat.messageNewId,
                    senderId = senderId,
                    targetId = targetId,
                    text = text
                )
            )
            chat.copy(messageNewId = chat.messageNewId + 1)
        }
        if (chatExist == null)
            chatList.add(addMessage(Chat(id = chatNewId++)))
        else
            chatList[chatExist.index] = addMessage(chatExist.value)
        return chatExist?.value
    }

    fun deleteChat(chatId: Int): String {
        return if (chatList.remove(chatById(chatId)?.value))
                    "Чат '$chatId' удален."
        else "Чата '$chatId' не существует."
    }

    fun editMessage(chatId: Int, messageId: Int, text: String): Boolean {
        val chat = chatById(chatId) ?: return false
        val message = messageById(chat.value, messageId) ?: return false
        chat.value.messages[message.index] =
            message.value.copy(
                editTimeStamp = System.currentTimeMillis(),
                text = text
            )
        return true
    }

    fun deleteMessage(chatId: Int, messageId: Int): String {
        val chat = chatById(chatId) ?: return "Чата '$chatId' не существует."
        return if (chat.value.messages.remove(messageById(chat.value, messageId)?.value)) {
            if (chat.value.messages.isEmpty()) {
                chatList.remove(chat.value)
                "Чат '$chatId' удален полностью."
            }
            else "Сообщение '$messageId' удалено из чата '$chatId'."
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
        chat.value.messages.find {it.targetId == userId}
            ?: return "$userId не участвует в чате '$chatId'."
        val filterMessage = chat.value.messages.asSequence().withIndex()
            .filter { it.value.targetId == userId && !it.value.hasRead }
            .onEach { message ->
                chat.value.messages[message.index] =
                    message.value.copy(hasRead = true)
            }
            .map { readedMessage -> readedMessage.value }.toList()
        return """ЧАТ '$chatId' СОДЕРЖИТ ${filterMessage.count()} НЕПРОЧИТАННЫХ СООБЩЕНИЙ ДЛЯ $userId:
            |${instanceList(filterMessage)}""".trimMargin() // .map { it.value })
    }

    override fun toString(): String {
        return """СПИСОК ВСЕХ ЧАТОВ:
            |${instanceList(chatList)}""".trimMargin()
    }

    fun testRemoveChat(chatId: Int): Boolean {
        return chatList.remove(chatById(chatId)?.value)
    }

    fun isFindChatById(chatId: Int): Boolean {
        return chatById(chatId) != null
    }

    fun testRemoveMassage(chatId: Int, messageId: Int): Int {
        val chat = chatById(chatId)
        return if (chat?.value?.messages
                ?.remove(messageById(chat.value, messageId)?.value) == true)
            if (chat.value.messages.isEmpty()) 2 else 1
        else 0
    }

    fun testFilterChat(userId: String): Boolean {
        return (filterChat(userId).isNotEmpty())
    }

    fun isUserIn(userId: String, chatId: Int): Boolean {
        return (chatById(chatId)?.value?.messages?.find {it.senderId == userId ||
                it.targetId == userId} != null)
    }
}