package utils

import java.time.LocalTime

object Logger {

    @JvmStatic
    fun log(message: String) {
        println(LocalTime.now().toString() + " " + message)
    }
}