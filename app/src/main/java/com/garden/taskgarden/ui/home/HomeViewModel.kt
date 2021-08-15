package com.garden.taskgarden.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * A class to represent the Home View Model
 */

class HomeViewModel : ViewModel() {
    private val mText: MutableLiveData<String?> = MutableLiveData()
    val text: LiveData<String?>
        get() = mText

    init {
        mText.value = "This is home fragment"
    }
}