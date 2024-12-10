package fyi.manpreet.composablememes.di

import fyi.manpreet.composablememes.platform.filemanager.FileManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual fun provideFileManagerModule() = module {
    singleOf(::FileManager)
}