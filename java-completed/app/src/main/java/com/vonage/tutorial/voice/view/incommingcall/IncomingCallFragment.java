package com.vonage.tutorial.voice.view.incommingcall;

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

public class IncomingCallFragment extends Fragment implements BackPressHandler {

    private IncomingCallViewModel viewModel;

    FloatingActionButton hangupFab;

    FloatingActionButton answerFab;

    TextView otherUserNameTextView;

    public IncomingCallFragment() {
        super(R.layout.fragment_incoming_call);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(IncomingCallViewModel.class);

        hangupFab = view.findViewById(R.id.hangupFab);
        answerFab = view.findViewById(R.id.answerFab);
        otherUserNameTextView = view.findViewById(R.id.otherUserNameTextView);

        IncomingCallFragmentArgs args = IncomingCallFragmentArgs.fromBundle(getArguments());
        viewModel.onInit(args);

        viewModel.toastLiveData.observe(getViewLifecycleOwner(), it -> Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT));
        viewModel.otherUserNameLiveData.observe(getViewLifecycleOwner(), it -> otherUserNameTextView.setText(it));

        hangupFab.setOnClickListener(it -> viewModel.hangup());

        answerFab.setOnClickListener(it -> viewModel.answer());
    }


    @Override
    public void onBackPressed() {
        viewModel.onBackPressed();
    }
}
