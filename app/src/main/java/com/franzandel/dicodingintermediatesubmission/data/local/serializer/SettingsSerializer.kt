package com.franzandel.dicodingintermediatesubmission.data.local.serializer

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.example.application.AuthenticationSession
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

/**
 * Created by Franz Andel
 * on 29 April 2022.
 */

object SettingsSerializer : Serializer<AuthenticationSession> {
    override val defaultValue: AuthenticationSession = AuthenticationSession.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): AuthenticationSession {
        try {
            return AuthenticationSession.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: AuthenticationSession,
        output: OutputStream
    ) = t.writeTo(output)
}

val Context.settingsDataStore: DataStore<AuthenticationSession> by dataStore(
    fileName = "settings.pb",
    serializer = SettingsSerializer
)

