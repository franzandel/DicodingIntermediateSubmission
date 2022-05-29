package com.franzandel.dicodingintermediatesubmission.data.local

import androidx.datastore.core.DataStore
import com.example.application.AuthenticationSession
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.utils.suspendTryCatch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LoginLocalSourceImpl @Inject constructor(
    private val settingsDataStore: DataStore<AuthenticationSession>
) : LoginLocalSource {

    override suspend fun saveToken(token: String): Result<Unit> {
        return suspendTryCatch {
            settingsDataStore.updateData { settings ->
                settings.toBuilder()
                    .setToken(token)
                    .build()
            }
            Result.Success(Unit)
        }
    }

    override suspend fun getToken(): Result<Flow<String>> {
        return suspendTryCatch {
            Result.Success(
                settingsDataStore.data
                    .map { settings ->
                        settings.token
                    }
            )
        }
    }
}
