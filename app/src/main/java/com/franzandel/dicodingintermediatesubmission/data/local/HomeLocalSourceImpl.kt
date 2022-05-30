package com.franzandel.dicodingintermediatesubmission.data.local

import androidx.datastore.core.DataStore
import com.example.application.AuthenticationSession
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.utils.suspendTryCatch
import javax.inject.Inject

class HomeLocalSourceImpl @Inject constructor(
    private val settingsDataStore: DataStore<AuthenticationSession>
) : HomeLocalSource {

    override suspend fun clearStorage(): Result<Unit> =
        suspendTryCatch {
            settingsDataStore.updateData {
                it.toBuilder().clear().build()
            }
            Result.Success(Unit)
        }
}
