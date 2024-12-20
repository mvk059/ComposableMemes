package fyi.manpreet.composablememes.di

import fyi.manpreet.composablememes.data.datasource.MemeLocalDataSource
import fyi.manpreet.composablememes.data.datasource.MemeLocalDataSourceImpl
import fyi.manpreet.composablememes.data.datasource.MemeRemoteDataSource
import fyi.manpreet.composablememes.data.datasource.MemeRemoteDataSourceImpl
import fyi.manpreet.composablememes.data.repository.MemeRepository
import fyi.manpreet.composablememes.data.repository.MemeRepositoryImpl
import fyi.manpreet.composablememes.ui.home.HomeViewModel
import fyi.manpreet.composablememes.ui.meme.MemeViewModel
import fyi.manpreet.composablememes.usecase.SaveImageUseCase
import fyi.manpreet.composablememes.usecase.MemeEditorConfigUseCase
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun initKoin(config: KoinAppDeclaration? = null) =
    startKoin {
        config?.invoke(this)
        modules(
            provideDataSourceModule,
            provideRepositoryModule,
            provideViewModelModule,
            provideUseCaseModule,
            provideNetworkModule,
            provideFileManagerModule(),
            provideStorageManager(),
            provideStorageModule(),
        )
    }

val provideViewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::MemeViewModel)
}

val provideDataSourceModule = module {
    singleOf(::MemeLocalDataSourceImpl).bind(MemeLocalDataSource::class)
    singleOf(::MemeRemoteDataSourceImpl).bind(MemeRemoteDataSource::class)
}

val provideRepositoryModule = module {
    singleOf(::MemeRepositoryImpl).bind(MemeRepository::class)
}

val provideUseCaseModule = module {
    factoryOf(::SaveImageUseCase)
    factoryOf(::MemeEditorConfigUseCase)
}

val provideNetworkModule = module {
    single { createJson() }
    single { createHttpClient(get(), true) }
}

fun createJson() = Json { isLenient = true; ignoreUnknownKeys = true }

fun createHttpClient(json: Json, enableNetworkLogs: Boolean) = HttpClient {
    install(ContentNegotiation) {
        json(json)
    }
    if (enableNetworkLogs) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.NONE
        }
    }
}