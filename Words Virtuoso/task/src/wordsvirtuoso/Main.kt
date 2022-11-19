package wordsvirtuoso

import java.io.File
import java.util.*

val scanner = Scanner(System.`in`)

class WordsVirtuoso {
    fun readWordsFile() {
        println("Input the words file:")
        val path = scanner.nextLine()
        val file = File(path)
        if (!file.exists()) println("Error: The words file $path doesn't exist.")
        else {
            val words = file.readLines()
            var invalids = 0
            words.forEach { if (!it.checkWord()) invalids++ }
            println(
                if (invalids == 0) "All words are valid!" else
                    "Warning: $invalids invalid words were found in the $path file."
            )
        }
    }

    /*fun getWord() {
        println("Input a 5-letter string:")
        val word = scanner.nextLine()

        println(
            when {
                word.length != 5 -> "The input isn't a 5-letter string."

                !Regex("([A-Z]|[a-z])+").matches(word) -> "The input has invalid characters."

                word.toSet().size != 5 -> "The input has duplicate letters."

                else -> "The input is a valid string."
            }
        )
    }*/

    private fun String.checkWord(): Boolean =
        Regex("[a-zA-Z]{5}").matches(this) && this.toSet().size == 5
}

fun main() {
    val game = WordsVirtuoso()
    game.readWordsFile()
}
