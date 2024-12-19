package fyi.manpreet.composablememes.di

import fyi.manpreet.composablememes.data.model.MemeTable
import fyi.manpreet.composablememes.platform.storage.StorageManager
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual fun provideStorageModule() = module {
    single<KStore<MemeTable>> {
        val storageManager: StorageManager = get()
        val directory = storageManager.getStorageDir()

        storeOf(
            file = directory,
            default = MemeTable()
        )
    }
}

actual fun provideStorageManager() = module {
    singleOf(::StorageManager)
}
