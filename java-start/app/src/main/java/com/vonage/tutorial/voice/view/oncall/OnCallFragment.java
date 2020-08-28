package com.vonage.tutorial.voice.view.oncall;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vonage.tutorial.R;
import com.vonage.tutorial.voice.BackPressHandler;

public class OnCallFragment extends Fragment implements BackPressHandler {

    OnCallViewModel viewModel;

    FloatingActionButton hangupFab;

    TextView otherUserNameTextView;

    public OnCallFragment() {
        super(R.layout.fragment_on_call);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(OnCallViewModel.class);

        assert getArguments() != null;
        OnCallFragmentArgs args = OnCallFragmentArgs.fromBundle(getArguments());
        viewModel.onInit(args);

        hangupFab = view.findViewById(R.id.hangupFab);
        otherUserNameTextView = view.findViewById(R.id.otherUserNameTextView);

        viewModel.toastLiveData.observe(getViewLifecycleOwner(), it -> Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT));
        viewModel.otherUserNameLiveData.observe(getViewLifecycleOwner(), it -> otherUserNameTextView.setText(it));

        hangupFab.setOnClickListener(view1 -> viewModel.hangup());
    }


    @Override
    public void onBackPressed() {
        viewModel.onBackPressed();
    }
}
