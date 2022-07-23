package com.franzandel.dicodingintermediatesubmission.data.local

import androidx.datastore.core.DataStore
import com.example.application.AuthenticationSession
import com.example.application.HomeSession
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.data.dao.HomeDao
import com.franzandel.dicodingintermediatesubmission.data.dao.RemoteKeysDao
import com.franzandel.dicodingintermediatesubmission.utils.suspendTryCatch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomeLocalSourceImpl @Inject constructor(
    private val settingsDataStore: DataStore<AuthenticationSession>,
    private val homeDataStore: DataStore<HomeSession>,
    private val homeDao: HomeDao,
    private val remoteKeysDao: RemoteKeysDao
) : HomeLocalSource {

    override suspend fun clearStorage(): Result<Unit> =
        suspendTryCatch {
            settingsDataStore.updateData {
                it.toBuilder().clear().build()
            }
            homeDao.deleteAll()
            remoteKeysDao.deleteAll()
            Result.Success(Unit)
        }

    override suspend fun saveLocationPreference(locationPreference: Int): Result<Unit> =
        suspendTryCatch {
            homeDataStore.updateData { settings ->
                settings.toBuilder()
                    .setLocationPreference(locationPreference)
                    .build()
            }
            Result.Success(Unit)
        }

    override suspend fun getLocationPreference(): Result<Flow<Int>> =
        suspendTryCatch {
            Result.Success(
                homeDataStore.data
                    .map { settings ->
                        settings.locationPreference
                    }
            )
        }
}
