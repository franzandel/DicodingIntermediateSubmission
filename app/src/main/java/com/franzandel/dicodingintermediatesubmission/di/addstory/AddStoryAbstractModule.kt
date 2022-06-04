package com.franzandel.dicodingintermediatesubmission.di.addstory

import androidx.lifecycle.ViewModel
import com.franzandel.dicodingintermediatesubmission.core.annotation.ViewModelKey
import com.franzandel.dicodingintermediatesubmission.data.remote.AddStoryRemoteSource
import com.franzandel.dicodingintermediatesubmission.data.remote.AddStoryRemoteSourceImpl
import com.franzandel.dicodingintermediatesubmission.data.repository.AddStoryRepositoryImpl
import com.franzandel.dicodingintermediatesubmission.domain.repository.AddStoryRepository
import com.franzandel.dicodingintermediatesubmission.ui.addstory.AddStoryViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.multibindings.IntoMap

/**
 * Created by Franz Andel
 * on 30 May 2022.
 */

@Module
@InstallIn(ViewModelComponent::class)
abstract class AddStoryAbstractModule {

    @Binds
    @IntoMap
    @ViewModelKey(AddStoryViewModel::class)
    abstract fun provideAddStoryViewModel(addStoryViewModel: AddStoryViewModel): ViewModel

    @Binds
    @ViewModelScoped
    abstract fun provideAddStoryRepository(addStoryRepositoryImpl: AddStoryRepositoryImpl): AddStoryRepository

    @Binds
    @ViewModelScoped
    abstract fun provideAddStoryRemoteSource(addStoryRemoteSourceImpl: AddStoryRemoteSourceImpl): AddStoryRemoteSource
}
