package fyi.manpreet.composablememes.usecase

import androidx.activity.ComponentActivity

class MainActivityUseCase {
    private var activity: ComponentActivity? = null

    fun setActivity(activity: ComponentActivity? = null) {
        this.activity = activity
    }

    fun requireActivity() = activity!!
}
