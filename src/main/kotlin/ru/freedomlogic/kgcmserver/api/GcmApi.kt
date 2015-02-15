package ru.freedomlogic.kgcmserver.api

import retrofit.http.*
import ru.freedomlogic.kgcmserver.payloads.Payload
import rx.Observable

public trait GcmApi {
    Header("Content-Type: application/json")
    POST("/send")
    fun sendMessage(Header("Authorization") authorization: String, Body payload: Payload): Observable<GcmResponse>
}