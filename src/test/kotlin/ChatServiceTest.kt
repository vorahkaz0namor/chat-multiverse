import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import ru.netology.*

class ChatServiceTest {
    private val chatMultiverse = ChatService()

    @Before
    fun fillBeforeTest() {
        fillChatMultiverse()
    }

    @Test
    fun sendMessageChatExist() {
        assertTrue(
            chatMultiverse.sendMessage(
                "Дмитрий",
                "Артем",
                "Отлично!"
            )?.javaClass == (Chat(0)).javaClass
        )
    }

    @Test
    fun sendMessageChatNotExist() {
        assertTrue(
            chatMultiverse.sendMessage(
                "Сергей",
                "Борис",
                "Привет!"
            ) == null
        )
    }

    @Test
    fun deleteChatTrue() {
        assertTrue(chatMultiverse.testRemoveChat(0))
    }

    @Test
    fun deleteChatFalse() {
        assertFalse(chatMultiverse.testRemoveChat(99))
    }

    @Test
    fun editMessageFalseChat() {
        assertFalse(chatMultiverse.editMessage(99, 0, ""))
    }

    @Test
    fun editMessageFalseMessage() {
        assertFalse(chatMultiverse.editMessage(0, 99, ""))
    }

    @Test
    fun editMessageTrue() {
        assertTrue(chatMultiverse.editMessage(0, 0, "Тест."))
    }

    @Test
    fun deleteMessageNotExistChat() {
        assertFalse(chatMultiverse.isFindChatById(99))
    }

    @Test
    fun deleteMessageNotExistMessage() {
        assertTrue(chatMultiverse.testRemoveMassage(0, 99) == 0)
    }

    @Test
    fun deleteMessageTrueSingle() {
        assertTrue(chatMultiverse.testRemoveMassage(0, 0) == 1)
    }

    @Test
    fun deleteMessageTrueAll() {
        chatMultiverse.testRemoveMassage(2, 0)
        assertTrue(chatMultiverse.testRemoveMassage(2, 1) == 2)
    }

    @Test
    fun getUnreadChatsExist() {
        assertTrue(chatMultiverse.testFilterChat("Дмитрий"))
    }

    @Test
    fun getUnreadChatsIsEmpty() {
        assertFalse(chatMultiverse.testFilterChat("Назарий"))
    }

    @Test
    fun getMessagesByChatIdNotExistChat() {
        assertFalse(chatMultiverse.isFindChatById(99))
    }

    @Test
    fun getMessagesByChatIdUserNotIn() {
        assertFalse(chatMultiverse.isUserIn("Назарий", 0))
    }

    @Test
    fun getMessagesByChatId() {
        assertTrue(chatMultiverse.isUserIn("Дмитрий", 0))
    }

    private fun fillChatMultiverse() {
        val addChat = { users: MutableList<String>, messages: List<String> ->
            for (m in messages) {
                chatMultiverse.sendMessage(users[0], users[1], m)
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
}