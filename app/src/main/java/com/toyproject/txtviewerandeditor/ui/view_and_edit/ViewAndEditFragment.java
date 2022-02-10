package com.toyproject.txtviewerandeditor.ui.view_and_edit;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.toyproject.txtviewerandeditor.MainActivity;
import com.toyproject.txtviewerandeditor.databinding.FragmentViewAndEditBinding;

public class ViewAndEditFragment extends Fragment {

    private ViewAndEditViewModel viewAndEditViewModel;
    private FragmentViewAndEditBinding binding;
    private final OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.finishAffinity();
            System.runFinalization();
            System.exit(0);
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewAndEditViewModel =
                new ViewModelProvider(this).get(ViewAndEditViewModel.class);

        binding = FragmentViewAndEditBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        viewAndEditViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        onBackPressedCallback.setEnabled(true);
        requireActivity().getOnBackPressedDispatcher().addCallback(onBackPressedCallback);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        onBackPressedCallback.setEnabled(false);
    }
}