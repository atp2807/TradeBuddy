package com.eeo.tradebuddy.features.message.read
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.Telephony

/** flow
 * step1. read the sms (this file)
 * step2. broker sms filter (
 * step3. real trade sms filter
 * step4. send to server
 */
data class SmsMessage(
    val address: String,    // 보낸 사람 (ex. 유진증권)
    val body: String,       // 메시지 본문
    val timestamp: Long     // 수신 시간 (epoch millis)
)

object SmsMessageReader {

    fun readSmsMessages(
        context: Context,
        startTimeMillis: Long = 0L,
        endTimeMillis: Long = Long.MAX_VALUE
    ): List<SmsMessage> {
        val smsList = mutableListOf<SmsMessage>()

        val uri: Uri = Telephony.Sms.CONTENT_URI
        val projection = arrayOf("address", "body", "date")
        val selection = "date >= ? AND date <= ?"
        val selectionArgs = arrayOf(startTimeMillis.toString(), endTimeMillis.toString())

        val cursor: Cursor? = context.contentResolver.query(
            uri,
            projection,
            null,
            null,
            "date DESC"  // 최신 순 정렬
        )

        cursor?.use {
            val addressIdx = it.getColumnIndex("address")
            val bodyIdx = it.getColumnIndex("body")
            val dateIdx = it.getColumnIndex("date")

            while (it.moveToNext()) {
                val address = it.getString(addressIdx) ?: ""
                val body = it.getString(bodyIdx) ?: ""
                val timestamp = it.getLong(dateIdx)

                smsList.add(SmsMessage(address, body, timestamp))
            }
        }

        return smsList
    }
}