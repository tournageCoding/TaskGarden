package com.garden.taskgarden.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * A class to represent a gallery view model.
 *
 * @author Jacob Gear
 */

class GalleryViewModel : ViewModel() {
    private val mText: MutableLiveData<String?> = MutableLiveData()
    val text: LiveData<String?>
        get() = mText

    init {
        mText.value = "This is gallery fragment"
    }
}