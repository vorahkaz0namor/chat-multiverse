package ru.netology

fun main() {
    val chatMultiverse = ChatService()

    chatMultiverse.sendMessage("Коля", "Витя", "Витек, привет!")
    chatMultiverse.sendMessage("Витя", "Коля", "О! Здарова, братан!")
    chatMultiverse.sendMessage("Витя", "Коля", "Чё мутишь?")
    chatMultiverse.sendMessage("Коля", "Витя", "Булки мутные.")
    chatMultiverse.sendMessage("Коля", "Витя", "Давай ко мне!")
    chatMultiverse.sendMessage("Сеня", "Дуся", "Кукушеньки!")
    chatMultiverse.editMessage(0, 3, "Мутки мутные...")
    println(chatMultiverse.getMessagesByChatId("Витя", 0))
    println(chatMultiverse.getMessagesByChatId("Витя", 0))
    println(chatMultiverse.getUnreadChats("Коля"))
    println(chatMultiverse.getMessagesByChatId("Дуся", 1))
    println(chatMultiverse)
    println(chatMultiverse.deleteChat(1))
    println(chatMultiverse.deleteMessage(0, 4))
    chatMultiverse.deleteMessage(0, 2)
    println(chatMultiverse)
}