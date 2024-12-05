package fyi.manpreet.composablememes.di

import fyi.manpreet.composablememes.data.database.MemeDatabase
import fyi.manpreet.composablememes.database.geMemeDatabase
import org.koin.dsl.module

actual fun provideDatabaseModule() = module {
    single<MemeDatabase> { geMemeDatabase() }
}