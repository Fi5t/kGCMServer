package ru.freedomlogic.kgcmserver

import ru.freedomlogic.kgcmserver.api.ApiManager
import ru.freedomlogic.kgcmserver.payloads.Payload
import ru.freedomlogic.kgcmserver.payloads.SimpleData
import com.google.gson.JsonParser
import java.io.FileWriter
import com.google.gson.Gson
import java.util.ArrayList
import java.io.BufferedReader
import java.io.FileReader

var config = Config("", ArrayList<String>())

fun main(args: Array<String>) {
    when (args.firstOrNull()) {
        "-i" -> interactiveMode()
        "-send" -> println("Comming soon...")
        "-gui" -> println("Comming soon...")
        else -> printCommonHelp()
    }

    System.exit(0)
}

fun interactiveMode() {
    ApiManager.interactiveMode = true

    var cmd: String? = ""

    while(!cmd.equals("exit")) {

        print("kgcm> ")

        cmd = readLine()

        cmd?.let {
            val cmdParts = it.split("\\s")

            when (cmdParts.first()) {
                "set-key" -> config.apiKey = cmdParts[1] ?: "None"
                "show-key" -> println(config.apiKey ?: "None")

                "add-id" -> config.registrationIds?.add(cmdParts[1])
                "show-ids" -> config.registrationIds?.forEach { println(it) }

                "save-config" -> {
                    try {
                        with(FileWriter("config.json")) {
                            write(Gson().toJson(config))
                            close()
                        }
                    } catch (e: Exception) {
                        println("Cannot write to config")
                    }
                }
                "load-config" -> {
                    try {
                        val reader = BufferedReader(FileReader("config.json"))
                        config = Gson().fromJson(reader, javaClass<Config>())
                    } catch (e: Exception) {
                        println("Cannot read from config")
                    }
                }

                "send-msg" -> {

                if (config.apiKey != null && config.registrationIds!!.size() > 0) {

                    if (cmdParts.size() > 1) {
                        when (cmdParts[1]) {
                            "simple" -> {
                                print("Title: ")
                                val title = readLine() ?: "Empty title"

                                print("Message: ")
                                val message = readLine() ?: "Empty message"

                                ApiManager.sendMessage(config.apiKey!!,
                                        Payload(SimpleData(title, message).getData(),
                                                config.registrationIds!!))
                            }

                            "test" -> {
                                ApiManager.sendMessage(config.apiKey!!,
                                        Payload(SimpleData("test title", "test message").getData(),
                                                config.registrationIds!!))
                            }

                            "custom" -> {
                                print("Json string: ")
                                val jsonString = readLine() ?: """{"error":"Object is empty"}"""

                                try {
                                    val jsonObject = JsonParser().parse(jsonString)
                                    ApiManager.sendMessage(config.apiKey!!,
                                            Payload(jsonObject, config.registrationIds!!))
                                } catch (e: Exception) {
                                    println("Json parse error")
                                }
                            }

                            else -> println("Type not found")
                        }
                    } else {
                        println("You must to set a type of message: simple, custom or test ")
                    }
                } else {
                    println("You must to set api key and registration ids. Execute: set-key <api_key> and add-id <id>")
                }
            }

                "exit" -> println("Bye!")
                else -> null
            }
        }
    }
}

fun printCommonHelp() {
    println(
            with(StringBuilder()) {
                append("-i - interactive mode\n")
                append("-send <simple|test|custom> - send a message of a defined type\n")
                append("-gui - run GUI interface\n")
                append("\n\n")
            }.toString()
    )
}
