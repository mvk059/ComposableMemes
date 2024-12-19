package fyi.manpreet.composablememes.di

import org.koin.core.module.Module

//expect fun provideDatabaseModule(): Module

object DatabaseConstants {
    const val DB_NAME = "composablememes.db"
    const val SLASH = "/"
    const val TABLE_NAME = "meme_table"
}