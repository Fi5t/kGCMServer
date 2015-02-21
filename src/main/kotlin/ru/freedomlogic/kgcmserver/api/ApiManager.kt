package ru.freedomlogic.kgcmserver.api

import ru.freedomlogic.kgcmserver.payloads.Payload
import java.util.concurrent.TimeUnit
import retrofit.RestAdapter

object ApiManager {
    var interactiveMode: Boolean = false

    private val mRestAdapter = RestAdapter.Builder()
        .setLogLevel(RestAdapter.LogLevel.FULL)
        .setEndpoint("https://android.googleapis.com/gcm")
        .build()
        .create(javaClass<GcmApi>())

    fun sendMessage(apiKey: String, payload: Payload) {
        mRestAdapter.sendMessage("key=${apiKey}", payload)
                .retry(3)
                .timeout(5, TimeUnit.SECONDS)
                .onErrorReturn { error ->
                    error.printStackTrace()
                    null
                } subscribe { response ->
                    response?.let {
                        if (!interactiveMode)
                            println("\nSuccess: ${response.success}\nFailure: ${response.failure}")
                    }
                }
    }
}