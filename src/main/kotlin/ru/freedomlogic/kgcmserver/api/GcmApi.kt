package ru.freedomlogic.kgcmserver.api

public trait GcmApi {
    retrofit.http.Header("Content-Type: application/json")
    retrofit.http.POST("/send")
    fun sendMessage(retrofit.http.Header("Authorization") authorization: String, retrofit.http.Body payload: ru.freedomlogic.kgcmserver.payloads.Payload): rx.Observable<ru.freedomlogic.kgcmserver.api.GcmResponse>
}