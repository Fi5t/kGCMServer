package ru.freedomlogic.kgcmserver.api

import java.util.ArrayList

/**
 * @param multicast_id Unique ID (number) identifying the multicast message.
 * @param success Number of messages that were processed without an error.
 * @param failure Number of messages that could not be processed.
 * @param canonical_ids Number of results that contain a canonical registration ID.
 *                      See the <a href = "https://developer.android.com/google/gcm/gcm.html#canonical">Overview</a>
 *                      for more discussion of this topic.
 * @param results Array of objects representing the status of the messages processed
 */
class GcmResponse(val multicast_id: Double,
                  val success: Int,
                  val failure: Int,
                  val canonical_ids: Int,
                  val results: ArrayList<Results>?)

/**
 * The objects are listed in the same order as the request (i.e., for each registration ID in the
 * request, its result is listed in the same index in the response).
 * @param message_id String specifying a unique ID for each successfully processed message.
 * @param registration_id Optional string specifying the canonical registration ID for the client app
 *                        that the message was processed and sent to. Sender should use this
 *                        value as the Registration ID for future requests. Otherwise, the messages
 *                        might be rejected
 * @param error String specifying the error that occurred when processing the message for the recipient.
 */
class Results(val message_id: String, val registration_id: String?, val error: String)