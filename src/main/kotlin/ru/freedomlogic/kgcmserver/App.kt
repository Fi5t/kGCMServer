package ru.freedomlogic.kgcmserver

import ru.freedomlogic.kgcmserver.api.ApiManager
import ru.freedomlogic.kgcmserver.payloads.Payload
import ru.freedomlogic.kgcmserver.payloads.SimpleData
import com.google.gson.JsonParser
import java.io.FileWriter
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.FileReader
import ru.freedomlogic.kgcmserver.utils.CmdUtils
import java.util.ArrayList

var config = Config("", ArrayList<String>())

fun main(args: Array<String>) {
    val cmd = CmdUtils.parse(args)

    cmd?.let {
        if (it.hasOption(CmdUtils.ARG_INTERACTIVE)) {
            interactiveMode()
        } else if (it.hasOption(CmdUtils.ARG_SEND)) {
            if (loadConfig()) {
                sendTest()
            } else if (it.hasOption(CmdUtils.ARG_APIKEY) && it.hasOption(CmdUtils.ARG_REGIDS)) {
                config.apiKey = it.getOptionValue(CmdUtils.ARG_APIKEY)
                config.regIds = it.getOptionValues(CmdUtils.ARG_REGIDS).toArrayList()

                sendTest()
            } else {
                println("Usage:\n1. java -jar kGCMServer.jar -send -apikey <KEY> -regids id1 id2 id3 ... idN\n2. java -jar kGCMServer.jar -configure")
            }
        } else if (it.hasOption(CmdUtils.ARG_GUI)) {
            //TODO Run gui
        } else if (it.hasOption(CmdUtils.ARG_CONFIGURE)) {
            createConfig()
        }

        System.exit(0)
    }
}

fun interactiveMode() {
    ApiManager.interactiveMode = true

    var cmd: String? = ""

    while (!cmd.equals("exit")) {

        print("kgcm> ")

        cmd = readLine()

        cmd?.let {
            if (it.isNotEmpty()) {
                val cmdParts = it.split("\\s")
                val size = cmdParts.size()

                when (cmdParts.first()) {
                    "set-key" -> config.apiKey = if (size > 1) cmdParts.get(1) else ""
                    "show-key" -> println(config.apiKey)

                    "add-id" -> if (size > 1) config.regIds.add(cmdParts.get(1))
                    "show-ids" -> config.regIds.forEach { println(it) }

                    "save-config" -> saveConfig()
                    "load-config" -> loadConfig()

                    "send-msg" -> {
                        if (config.apiKey.isNotEmpty() && config.regIds.isNotEmpty()) {
                            if (size > 1) {
                                when (cmdParts.get(1)) {
                                    "simple" -> sendSimple()
                                    "test" -> sendTest()
                                    "custom" -> sendCustom()
                                    else -> println("Type not found. Usage: send-msg <simple|test|custom>")
                                }
                            } else {
                                println("You must to set a type of message: simple, custom or test\n")
                            }
                        } else {
                            println("You must to set api key and registration ids. Execute: `set-key <api_key>` and `add-id <id>`\n")
                        }
                    }

                    "exit" -> println("Bye!")
                }
            }
        }
    }
}

private fun sendCustom() {
    print("Json string: ")
    val jsonString = readLine() ?: """{"error":"Object is empty"}"""

    try {
        val jsonObject = JsonParser().parse(jsonString)
        ApiManager.sendMessage(config.apiKey, Payload(jsonObject, config.regIds))
    } catch (e: Exception) {
        println("Json parse error")
    }
}

private fun sendTest() {
    ApiManager.sendMessage(config.apiKey,
            Payload(SimpleData("test title", "test message").getData(), config.regIds))
}

private fun sendSimple() {
    print("Title: ")
    val title = readLine() ?: "Empty title"

    print("Message: ")
    val message = readLine() ?: "Empty message"

    ApiManager.sendMessage(config.apiKey,
            Payload(SimpleData(title, message).getData(), config.regIds))
}

private fun loadConfig(): Boolean {
    var loadState = false

    try {
        val reader = BufferedReader(FileReader("config.json"))
        config = Gson().fromJson(reader, javaClass<Config>())

        loadState = true
    } catch (e: Exception) {
        println("Cannot read from config\n")
    }

    return loadState
}

private fun saveConfig(): Boolean {
    var saveState = false

    try {
        with(FileWriter("config.json")) {
            write(Gson().toJson(config))
            close()
        }

        saveState = true
    } catch (e: Exception) {
        println("Cannot write to config\n")
    }

    return saveState
}

fun createConfig() {
    val ids = ArrayList<String>()

    print("API key:")
    val key = readLine()?.trim()

    print("id:")
    var cmd = readLine()?.trim()

    while (cmd.isNotEmpty()) {
        ids.add(cmd)

        print("id:")
        cmd = readLine()?.trim()
    }

    val isValidKey = key != null && key.isNotEmpty()
    if (isValidKey && ids.isNotEmpty()) {
        config.apiKey = key!!
        config.regIds = ids

        saveConfig()
    } else {
        println("Configuration creation error")
    }
}
