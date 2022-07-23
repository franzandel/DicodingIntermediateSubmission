package com.franzandel.dicodingintermediatesubmission.domain.usecase

import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.core.usecase.BaseUseCase
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.domain.repository.HomeRepository
import com.franzandel.dicodingintermediatesubmission.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Franz Andel
 * on 23 July 2022.
 */

class GetLocationPreferenceUseCase @Inject constructor(
    private val repository: HomeRepository,
    coroutineThread: CoroutineThread
) : BaseUseCase<Result<Flow<Int>>>(coroutineThread) {

    override suspend fun getOperation(): Result<Flow<Int>> {
        return repository.getLocationPreference()
    }
}
