package fyi.manpreet.composablememes

import android.app.Application
import fyi.manpreet.composablememes.di.initKoin

class MemeApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin(this)
    }
}