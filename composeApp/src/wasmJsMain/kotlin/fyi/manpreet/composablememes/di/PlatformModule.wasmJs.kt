package fyi.manpreet.composablememes.di

import fyi.manpreet.composablememes.platform.platform.Platforms
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual fun providePlatformModule() = module {
    singleOf(::Platforms)
}