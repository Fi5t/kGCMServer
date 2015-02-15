package ru.freedomlogic.kgcmserver.payloads

import com.google.gson.JsonElement
import java.util.ArrayList

class Payload(val data: JsonElement, val registration_ids: ArrayList<String>)