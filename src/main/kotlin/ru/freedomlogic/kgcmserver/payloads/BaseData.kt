package ru.freedomlogic.kgcmserver.payloads

import com.google.gson.JsonElement
import com.google.gson.Gson

open class BaseData {
    fun getData(): JsonElement = Gson().toJsonTree(this)
}