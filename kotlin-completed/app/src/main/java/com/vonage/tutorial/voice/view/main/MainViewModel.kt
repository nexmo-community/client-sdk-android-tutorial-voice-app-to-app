package com.vonage.tutorial.voice.view.main

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nexmo.client.NexmoCall
import com.nexmo.client.NexmoCallHandler
import com.nexmo.client.NexmoClient
import com.nexmo.client.NexmoIncomingCallListener
import com.nexmo.client.request_listener.NexmoApiError
import com.nexmo.client.request_listener.NexmoRequestListener
import com.vonage.tutorial.voice.Config
import com.vonage.tutorial.voice.extension.asLiveData
import com.vonage.tutorial.voice.util.CallManager
import com.vonage.tutorial.voice.util.NavManager
import com.vonage.tutorial.voice.util.observer
import timber.log.Timber

class MainViewModel : ViewModel() {

    private val client = NexmoClient.get()
    private val callManager = CallManager
    private val navManager = NavManager

    private var otherUserName: String by observer("") {
        _otherUserNameMutableLiveData.postValue(it)
    }

    // SDK does not expose this info on call success
    private var lastCalledUserName = ""

    private val toastMutableLiveData = MutableLiveData<String>()
    val toastLiveData = toastMutableLiveData.asLiveData()

    private val loadingMutableLiveData = MutableLiveData<Boolean>()
    val loadingLiveData = loadingMutableLiveData.asLiveData()

    private val _currentUserNameMutableLiveData = MutableLiveData<String>()
    val currentUserNameLiveData = _currentUserNameMutableLiveData.asLiveData()

    private val _otherUserNameMutableLiveData = MutableLiveData<String>()
    val otherUserLiveData = _otherUserNameMutableLiveData.asLiveData()

    private val incomingCallListener = NexmoIncomingCallListener { call ->
        callManager.onGoingCall = call
        val otherUserName = call.callMembers.first().user.name
        val navDirections = MainFragmentDirections.actionMainFragmentToIncomingCallFragment(otherUserName)
        navManager.navigate(navDirections)
    }

    private val callListener = object : NexmoRequestListener<NexmoCall> {
        override fun onSuccess(call: NexmoCall?) {
            callManager.onGoingCall = call

            loadingMutableLiveData.postValue(false)

            val navDirections = MainFragmentDirections.actionMainFragmentToOnCallFragment(lastCalledUserName)
            navManager.navigate(navDirections)
        }

        override fun onError(apiError: NexmoApiError) {
            Timber.e(apiError.message)
            toastMutableLiveData.postValue(apiError.message)
            loadingMutableLiveData.postValue(false)
        }
    }

    fun onInit(arg: MainFragmentArgs) {
        val currentUserName = arg.userName
        _currentUserNameMutableLiveData.postValue(currentUserName)
        otherUserName = Config.getOtherUserName(currentUserName)

        //The same callback can be registered twice, so we are removing all callbacks to be save
        client.removeIncomingCallListeners()
        client.addIncomingCallListener(incomingCallListener)
    }

    override fun onCleared() {
        client.removeIncomingCallListeners()
    }

    @SuppressLint("MissingPermission")
    fun startAppToAppCall() {
        lastCalledUserName = otherUserName
        client.call(otherUserName, NexmoCallHandler.IN_APP, callListener)
        loadingMutableLiveData.postValue(true)
    }

    fun onBackPressed() {
        client.logout()
    }
}
