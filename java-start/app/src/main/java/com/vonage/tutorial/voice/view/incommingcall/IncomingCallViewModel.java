package com.vonage.tutorial.voice.view.incommingcall;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavDirections;
import com.nexmo.client.NexmoCall;
import com.nexmo.client.request_listener.NexmoApiError;
import com.nexmo.client.request_listener.NexmoRequestListener;
import com.vonage.tutorial.voice.util.CallManager;
import com.vonage.tutorial.voice.util.NavManager;

public class IncomingCallViewModel extends ViewModel {

    private NavManager navManager = NavManager.getInstance();
    private CallManager callManager = CallManager.getInstance();

    String otherUserName;

    private MutableLiveData<String> otherUserNameMutableLiveData = new MutableLiveData<>();
    public LiveData<String> otherUserNameLiveData = otherUserNameMutableLiveData;

    private MutableLiveData<String> toastMutableLiveData = new MutableLiveData<>();
    public LiveData<String> toastLiveData = toastMutableLiveData;

    public void hangup() {
        hangupInternal(true);
    }

    @SuppressLint("MissingPermission")
    public void answer() {
        callManager.getOnGoingCall().answer(new NexmoRequestListener<NexmoCall>() {

            @Override
            public void onSuccess(@Nullable NexmoCall call) {
                NavDirections navDirections =
                        IncomingCallFragmentDirections.actionIncomingCallFragmentToOnCallFragment(otherUserName);
                navManager.navigate(navDirections);
            }

            @Override
            public void onError(@NonNull NexmoApiError apiError) {
                toastMutableLiveData.postValue(apiError.getMessage());
            }
        });
    }

    public void onBackPressed() {
        hangupInternal(false);
    }

    public void hangupInternal(Boolean popBackStack) {
        callManager.getOnGoingCall().hangup(new NexmoRequestListener<NexmoCall>() {
            @Override
            public void onSuccess(@Nullable NexmoCall nexmoCall) {
                callManager.setOnGoingCall(null);

                if (popBackStack) {
                    navManager.popBackStack();
                }
            }

            @Override
            public void onError(@NonNull NexmoApiError nexmoApiError) {
                toastMutableLiveData.postValue(nexmoApiError.getMessage());
            }
        });
    }

    public void onInit(IncomingCallFragmentArgs args) {
        otherUserName = args.getOtherUserName();
        otherUserNameMutableLiveData.postValue(otherUserName);
    }
}

