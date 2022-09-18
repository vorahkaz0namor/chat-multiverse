package ru.netology

fun main() {
    val chatMultiverse = ChatService()

    chatMultiverse.editMessage(0, 3, "<сообщение изменено>")
    println(chatMultiverse.getMessagesByChatId("Дмитрий", 0))
    println(chatMultiverse.getMessagesByChatId("Дмитрий", 0))
    println(chatMultiverse.getUnreadChats("Ксения"))
    println(chatMultiverse)
    println(chatMultiverse.deleteChat(0))
    println(chatMultiverse.deleteMessage(2, 0))
    println(chatMultiverse.deleteMessage(2, 1))
    println(chatMultiverse)
}