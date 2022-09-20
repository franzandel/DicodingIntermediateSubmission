package com.franzandel.dicodingintermediatesubmission.data.local.serializer

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.example.application.HomeSession
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

/**
 * Created by Franz Andel
 * on 23 July 2022.
 */

@Suppress("BlockingMethodInNonBlockingContext")
object HomeSerializer : Serializer<HomeSession> {

    override val defaultValue: HomeSession = HomeSession.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): HomeSession {
        try {
            return HomeSession.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: HomeSession,
        output: OutputStream
    ) = t.writeTo(output)
}

val Context.homeDataStore: DataStore<HomeSession> by dataStore(
    fileName = "home_data_store.pb",
    serializer = HomeSerializer
)

