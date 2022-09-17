import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import ru.netology.*

class ChatServiceTest {
    private val chatMultiverse = ChatService()

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

 }