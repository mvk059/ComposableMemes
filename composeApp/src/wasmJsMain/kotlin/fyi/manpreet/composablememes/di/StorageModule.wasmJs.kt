package fyi.manpreet.composablememes.di

import fyi.manpreet.composablememes.data.model.MemeTable
import fyi.manpreet.composablememes.platform.storage.StorageManager
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.storage.storeOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual fun provideStorageManager() = module {
    single<KStore<MemeTable>> {
        storeOf("memes", MemeTable())
    }
}

actual fun provideStorageModule() = module {
    singleOf(::StorageManager)
}