package com.vonage.tutorial.voice.view.main;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavDirections;
import com.nexmo.client.NexmoCall;
import com.nexmo.client.NexmoCallHandler;
import com.nexmo.client.NexmoClient;
import com.nexmo.client.NexmoIncomingCallListener;
import com.nexmo.client.request_listener.NexmoApiError;
import com.nexmo.client.request_listener.NexmoRequestListener;
import com.vonage.tutorial.voice.Config;
import com.vonage.tutorial.voice.util.CallManager;
import com.vonage.tutorial.voice.util.NavManager;
import timber.log.Timber;

public class MainViewModel extends ViewModel {

    private NexmoClient client = NexmoClient.get();
    private CallManager callManager = CallManager.getInstance();
    private NavManager navManager = NavManager.getInstance();

    // SDK does not expose this info on call success
    private String lastCalledUserName = "";

    private MutableLiveData<String> toastMutableLiveData = new MutableLiveData<>();
    public LiveData<String> toastLiveData = toastMutableLiveData;

    private MutableLiveData<Boolean> loadingMutableLiveData = new MutableLiveData<>();
    public LiveData<Boolean> loadingLiveData = loadingMutableLiveData;

    private MutableLiveData<String> currentUserNameMutableLiveData = new MutableLiveData<>();
    public LiveData<String> currentUserNameLiveData = currentUserNameMutableLiveData;

    private MutableLiveData<String> otherUserNameMutableLiveData = new MutableLiveData<>();
    public LiveData<String> otherUserLiveData = otherUserNameMutableLiveData;

    private NexmoIncomingCallListener incomingCallListener = call -> {
        callManager.setOnGoingCall(call);
        String otherUserName = call.getCallMembers().get(0).getUser().getName();
        NavDirections navDirections = MainFragmentDirections.actionMainFragmentToIncomingCallFragment(otherUserName);
        navManager.navigate(navDirections);
    };

    private NexmoRequestListener<NexmoCall> callListener = new NexmoRequestListener<NexmoCall>() {
        @Override
        public void onSuccess(@Nullable NexmoCall call) {
            callManager.setOnGoingCall(call);

            loadingMutableLiveData.postValue(false);

            NavDirections navDirections = MainFragmentDirections.actionMainFragmentToOnCallFragment(lastCalledUserName);
            navManager.navigate(navDirections);
        }

        @Override
        public void onError(@NonNull NexmoApiError apiError) {
            Timber.e(apiError.getMessage());
            toastMutableLiveData.postValue(apiError.getMessage());
            loadingMutableLiveData.postValue(false);
        }
    };

    public void onInit(MainFragmentArgs mainFragmentArgs) {
        String currentUserName = mainFragmentArgs.getUserName();
        currentUserNameMutableLiveData.postValue(currentUserName);

        String otherUserName = Config.getOtherUserName(currentUserName);
        otherUserNameMutableLiveData.postValue(otherUserName);

        //The same callback can be registered twice, so we are removing all callbacks to be save
        client.removeIncomingCallListeners();
        client.addIncomingCallListener(incomingCallListener);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    @SuppressLint("MissingPermission")
    public void startAppToAppCall() {
        String otherUserName = otherUserLiveData.getValue();
        lastCalledUserName = otherUserName;
        client.call(otherUserName, NexmoCallHandler.IN_APP, callListener);
        loadingMutableLiveData.postValue(true);
    }

    public void onBackPressed() {
        client.logout();
    }
}
