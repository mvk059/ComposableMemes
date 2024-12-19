package fyi.manpreet.composablememes.di

import fyi.manpreet.composablememes.data.model.MemeTable
import fyi.manpreet.composablememes.platform.storage.StorageManager
import fyi.manpreet.composablememes.util.MemeConstants
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.io.files.Path
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual fun provideStorageModule() = module {
    single<KStore<MemeTable>> {
        val storageManager: StorageManager = get()
        storeOf(
            file = Path("${storageManager.getStorageDir()}${MemeConstants.STORAGE_FILE_NAME}"),
            default = MemeTable()
        )
    }
}

actual fun provideStorageManager() = module {
    singleOf(::StorageManager)
}
