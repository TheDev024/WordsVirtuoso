package wordsvirtuoso

import java.io.File
import java.lang.System.currentTimeMillis
import java.util.*
import kotlin.properties.Delegates

val scanner = Scanner(System.`in`)
val generator = Random()

class WordsVirtuoso {
    fun cmd(args: Array<String>) = if (args.size != 2) println("Error: Wrong number of arguments.") else {
        val allWordsPath = args[0]
        val allWordsFile = File(allWordsPath)
        if (allWordsFile.exists()) {
            val candidateWordsPath = args[1]
            val candidateWordsFile = File(candidateWordsPath)
            if (candidateWordsFile.exists()) {
                val allWords = allWordsFile.readLines().map { it.lowercase() }
                var invalids = 0
                allWords.forEach { if (!it.checkWord()) invalids++ }
                if (invalids == 0) {
                    val candidateWords = candidateWordsFile.readLines().map { it.lowercase() }
                    invalids = 0
                    candidateWords.forEach { if (!it.checkWord()) invalids++ }
                    if (invalids == 0) {
                        var notContained = 0
                        candidateWords.forEach { if (!allWords.contains(it)) notContained++ }
                        if (notContained == 0) {
                            println("Words Virtuoso\n")
                            val secretIndex = generator.nextInt(candidateWords.size)
                            val secretWord = candidateWords[secretIndex]
                            var tries = 0
                            var wrongChars = setOf<Char>()
                            var startTime by Delegates.notNull<Long>()
                            var output = ""
                            game@ while (true) {
                                val word = getWord()
                                if (tries == 0) startTime = currentTimeMillis()
                                if (word != null) {
                                    if (word == "exit") {
                                        println("The game is over.")
                                        break@game
                                    }
                                    tries++
                                    if (word == secretWord) {
                                        if (tries == 1) println("${secretWord.uppercase()}\n\nCorrect!\nAmazing luck! The solution was found at once.")
                                        else {
                                            val duration =
                                                ((currentTimeMillis() - startTime).toDouble() / 1000.0).toLong()
                                            println(output)
                                            println("${secretWord.uppercase()}\n\nCorrect!\nThe solution was found after $tries tries in $duration seconds.")
                                        }
                                        break@game
                                    }
                                    if (!allWords.contains(word)) {
                                        println("The input word isn't included in my words list.")
                                        continue@game
                                    }
                                    word.forEach {
                                        output += if (secretWord.contains(it)) {
                                            if (secretWord.indexOf(it) == word.indexOf(it)) {
                                                it.uppercase()
                                            } else it.lowercase()
                                        } else {
                                            wrongChars = wrongChars.plus(it.uppercaseChar())
                                            '_'
                                        }
                                    }
                                    println(output)
                                    println(wrongChars.sorted().joinToString(""))
                                    output += '\n'
                                }
                            }
                        } else println("Error: $notContained candidate words are not included in the $allWordsPath file.")
                    } else println("Error: $invalids invalid words were found in the $candidateWordsPath file.")
                } else println("Error: $invalids invalid words were found in the $allWordsPath file.")
            } else println("Error: The candidate words file $candidateWordsPath doesn't exist.")
        } else println("Error: The words file $allWordsPath doesn't exist.")
    }

    /*fun readWordsFile() {
        println("Input the words file:")
        val path = scanner.nextLine()
        val file = File(path)
        if (!file.exists()) println("Error: The words file $path doesn't exist.")
        else {
            val words = file.readLines()
            var invalids = 0
            words.forEach { if (!it.checkWord()) invalids++ }
            println(
                if (invalids == 0) "All words are valid!" else "Warning: $invalids invalid words were found in the $path file."
            )
        }
    }*/

    private fun getWord(): String? {
        println("Input a 5-letter word:")
        val word = scanner.nextLine()

        return when {
            word == "exit" -> word

            word.length != 5 -> {
                println("The input isn't a 5-letter word.")
                null
            }

            !Regex("[a-zA-Z]+").matches(word) -> {
                println("One or more letters of the input aren't valid.")
                null
            }

            word.toSet().size != 5 -> {
                println("The input has duplicated letters.")
                null
            }

            else -> word
        }
    }

    private fun String.checkWord(): Boolean = Regex("[a-zA-Z]{5}").matches(this) && this.toSet().size == 5
}

fun main(args: Array<String>) {
    val game = WordsVirtuoso()
    game.cmd(args)
}
