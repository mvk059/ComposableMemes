package fyi.manpreet.composablememes.di

import fyi.manpreet.composablememes.data.datasource.MemeLocalDataSource
import fyi.manpreet.composablememes.data.datasource.MemeLocalDataSourceImpl
import fyi.manpreet.composablememes.data.repository.MemeRepository
import fyi.manpreet.composablememes.data.repository.MemeRepositoryImpl
import fyi.manpreet.composablememes.ui.home.HomeViewModel
import fyi.manpreet.composablememes.ui.meme.MemeViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module

fun initKoin(config: KoinAppDeclaration? = null) =
    startKoin {
        config?.invoke(this)
        modules(
            provideLocalDataSourceModule,
            provideRepositoryModule,
            provideViewModelModule,
            provideDatabaseModule(),
            provideFileManagerModule(),
        )
    }

val provideViewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::MemeViewModel)
}

val provideLocalDataSourceModule = module {
    singleOf(::MemeLocalDataSourceImpl).bind(MemeLocalDataSource::class)
}

val provideRepositoryModule = module {
    singleOf(::MemeRepositoryImpl).bind(MemeRepository::class)
}
