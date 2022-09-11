package ru.netology

import java.text.SimpleDateFormat
import java.util.*

data class Message(
    val id: Int,
    val timeStamp: Long = System.currentTimeMillis(),
    val editTimeStamp: Long? = null,
    val senderId: String = "Sender",
    val targetId: String = "Recipient",
    val text: String = "Hello!",
    val hasRead: Boolean = false
) {
    override fun toString(): String {
        val unixTimeInDateTime = { time: Long ->
            SimpleDateFormat("dd MMM y, H:mm:ss").format(Date(time))
        }
        return """Сообщение от $senderId для $targetId, ${
                    if (editTimeStamp == null)
                        unixTimeInDateTime(timeStamp)
                    else
                        "отредактировано ${unixTimeInDateTime(editTimeStamp)}"
                    } 
            |"$text"
            |
            |""".trimMargin()
    }
}
