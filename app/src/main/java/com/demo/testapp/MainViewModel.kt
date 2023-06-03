package com.demo.testapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    /**
     * Set the value of MutableLiveData
     *
     */
    fun changeMessageText(){
        _message.value = "Hello World"
    }
}