package com.vonage.tutorial.voice.view.oncall

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nexmo.client.NexmoCall
import com.nexmo.client.NexmoCallEventListener
import com.nexmo.client.NexmoCallMember
import com.nexmo.client.NexmoCallMemberStatus
import com.nexmo.client.NexmoMediaActionState
import com.nexmo.client.request_listener.NexmoApiError
import com.nexmo.client.request_listener.NexmoRequestListener
import com.vonage.tutorial.R
import com.vonage.tutorial.voice.extension.asLiveData
import com.vonage.tutorial.voice.util.CallManager
import com.vonage.tutorial.voice.util.NavManager
import timber.log.Timber

class OnCallViewModel : ViewModel() {
    private val callManager = CallManager
    private val navManager = NavManager

    private val toastMutableLiveData = MutableLiveData<String>()
    val toastLiveData = toastMutableLiveData.asLiveData()

    private val otherUserNameMutableLiveData = MutableLiveData<String>()
    val otherUserNameLiveData = otherUserNameMutableLiveData.asLiveData()

    private val callEventListener = object : NexmoCallEventListener {
        override fun onMemberStatusUpdated(callMemberStatus: NexmoCallMemberStatus, callMember: NexmoCallMember) {
            Timber.d("CallEventListener.onMemberStatusUpdated: ${callMember.user.name} : $callMemberStatus")

            if (callMemberStatus == NexmoCallMemberStatus.COMPLETED || callMemberStatus == NexmoCallMemberStatus.CANCELED) {
                callManager.onGoingCall = null
                navManager.popBackStack(R.id.mainFragment, false)
            }
        }

        override fun onMuteChanged(mediaActionState: NexmoMediaActionState, callMember: NexmoCallMember) {
            Timber.d("CallEventListener.onMuteChanged: ${callMember.user.name} : $mediaActionState")
        }

        override fun onEarmuffChanged(mediaActionState: NexmoMediaActionState, callMember: NexmoCallMember) {
            Timber.d("CallEventListener.onEarmuffChanged: ${callMember.user.name} : $mediaActionState")
        }

        override fun onDTMF(dtmf: String, callMember: NexmoCallMember) {
            Timber.d("CallEventListener.onDTMF: ${callMember.user.name} : $dtmf")
        }
    }

    init {
        val onGoingCall = checkNotNull(callManager.onGoingCall) { "Call is null" }
        onGoingCall.addCallEventListener(callEventListener)
    }

    override fun onCleared() {
        super.onCleared()

        callManager.onGoingCall?.removeCallEventListener(callEventListener)
    }

    fun onBackPressed() {
        hangupInternal()
    }

    fun hangup() {
        hangupInternal()
    }

    private fun hangupInternal() {
        callManager.onGoingCall?.hangup(object : NexmoRequestListener<NexmoCall> {
            override fun onSuccess(call: NexmoCall?) {
                callManager.onGoingCall = null
            }

            override fun onError(apiError: NexmoApiError) {
                toastMutableLiveData.postValue(apiError.message)
            }
        })
    }

    fun onInit(args: OnCallFragmentArgs) {
        otherUserNameMutableLiveData.postValue(args.otherUserName)
    }
}
