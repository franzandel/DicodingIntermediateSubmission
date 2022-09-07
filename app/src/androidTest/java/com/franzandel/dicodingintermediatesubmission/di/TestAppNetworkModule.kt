package com.franzandel.dicodingintermediatesubmission.di

import com.franzandel.dicodingintermediatesubmission.di.app.AppNetworkModule
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

/**
 * Created by Franz Andel
 * on 03 September 2022.
 */

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppNetworkModule::class]
)
class TestAppNetworkModule: AppNetworkModule() {
    override val baseUrl: String
        get() = "http://localhost:8080/"
}
