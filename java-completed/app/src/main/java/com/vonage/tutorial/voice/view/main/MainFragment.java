package com.vonage.tutorial.voice.view.main;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.vonage.tutorial.R;
import com.vonage.tutorial.voice.BackPressHandler;

public class MainFragment extends Fragment implements BackPressHandler {

    MainViewModel viewModel;

    Button startAppToAppCallButton;

    ProgressBar progressBar;

    TextView currentUserNameTextView;

    private Observer<String> toastObserver = it -> Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show();

    private Observer<Boolean> loadingObserver = this::setDataLoading;

    private Observer<String> currentUserObserver = it -> currentUserNameTextView.setText(getResources().getString(R.string.hi, it));

    private Observer<String> otherUserObserver = it -> {
        String label = getResources().getString(R.string.start_app_to_app_call_with, it);
        startAppToAppCallButton.setText(label);
    };

    public MainFragment() {
        super(R.layout.fragment_main);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        startAppToAppCallButton = view.findViewById(R.id.startAppToAppCallButton);
        progressBar = view.findViewById(R.id.progressBar);
        currentUserNameTextView = view.findViewById(R.id.currrentUserNameTextView);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        assert getArguments() != null;
        MainFragmentArgs args = MainFragmentArgs.fromBundle(getArguments());
        viewModel.onInit(args);

        viewModel.toastLiveData.observe(getViewLifecycleOwner(), toastObserver);
        viewModel.loadingLiveData.observe(getViewLifecycleOwner(), loadingObserver);
        viewModel.currentUserNameLiveData.observe(getViewLifecycleOwner(), currentUserObserver);
        viewModel.otherUserLiveData.observe(getViewLifecycleOwner(), otherUserObserver);

        startAppToAppCallButton.setOnClickListener(it -> viewModel.startAppToAppCall());
    }

    @Override
    public void onBackPressed() {
        viewModel.onBackPressed();
    }

    private void setDataLoading(Boolean dataLoading) {
        startAppToAppCallButton.setEnabled(!dataLoading);

        int visibility;

        if (dataLoading) {
            visibility = View.VISIBLE;
        } else {
            visibility = View.GONE;
        }

        progressBar.setVisibility(visibility);
    }
}
