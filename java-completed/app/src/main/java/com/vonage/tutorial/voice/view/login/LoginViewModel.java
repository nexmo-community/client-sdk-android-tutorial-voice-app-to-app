package com.vonage.tutorial.voice.view.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavDirections;
import com.nexmo.client.NexmoClient;
import com.nexmo.client.request_listener.NexmoConnectionListener;
import com.nexmo.client.request_listener.NexmoConnectionListener.ConnectionStatus;
import com.vonage.tutorial.voice.User;
import com.vonage.tutorial.voice.util.NavManager;
import com.vonage.tutorial.voice.util.StringUtils;

public class LoginViewModel extends ViewModel {

    private NexmoClient client = NexmoClient.get();

    NavManager navManager = NavManager.getInstance();

    private MutableLiveData<ConnectionStatus> connectionStatusMutableLiveData = new MutableLiveData<ConnectionStatus>();
    public LiveData<ConnectionStatus> _connectionStatusLiveData = connectionStatusMutableLiveData;

    private User user;


    public LoginViewModel() {
        client.setConnectionListener(new NexmoConnectionListener() {
            @Override
            public void onConnectionStatusChange(@NonNull ConnectionStatus connectionStatus, @NonNull ConnectionStatusReason connectionStatusReason) {
                if (connectionStatus == ConnectionStatus.CONNECTED) {
                    navigate();
                    return;
                }

                connectionStatusMutableLiveData.postValue(connectionStatus);
            }
        });
    }

    private void navigate() {
        String userName = user.getName();

        if(userName == null) {
            throw new RuntimeException("user name is null");
        }

        NavDirections navDirections = LoginFragmentDirections.actionLoginFragmentToVoiceFragment(userName);
        navManager.navigate(navDirections);
    }

    void onLoginUser(User user) {
        if (!StringUtils.isBlank(user.jwt)) {
            this.user = user;
            client.login(user.jwt);
        }
    }
}
