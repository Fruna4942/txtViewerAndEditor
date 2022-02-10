package com.toyproject.txtviewerandeditor.ui.file_explore;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.toyproject.txtviewerandeditor.R;
import com.toyproject.txtviewerandeditor.databinding.FragmentFileExploreBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class FileExploreFragment extends Fragment{

    private FileExploreViewModel fileExploreViewModel;
    private FragmentFileExploreBinding binding;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private final OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            String presentPath = recyclerViewAdapter.getPresentPath();
            if (!presentPath.equals(Environment.getExternalStorageDirectory().getPath())) {
                File file = new File(presentPath);
                File parentFile = file.getParentFile();
                presentPath = parentFile.getPath();
                recyclerViewAdapter.changeDirectory(presentPath, getRecyclerViewItemList(presentPath));
                recyclerView.setAdapter(recyclerViewAdapter);
                recyclerView.refreshDrawableState();
            }
            else {
                NavDirections navDirections = FileExploreFragmentDirections.actionNavFileExploreToNavViewAndEdit();
                Navigation.findNavController(FileExploreFragment.super.getView()).navigate(navDirections);
            }
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fileExploreViewModel =
                new ViewModelProvider(this).get(FileExploreViewModel.class);

        binding = FragmentFileExploreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerView = binding.recyclerView;

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        String presentPath = Environment.getExternalStorageDirectory().getPath();
        recyclerViewAdapter = new RecyclerViewAdapter(presentPath, getRecyclerViewItemList(presentPath));
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), 1));

        recyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                // TODO: 2022-02-10 txt 파일 열기 구현 필요
                String presentPath = recyclerViewAdapter.getRecyclerViewItemArrayList().get(pos).getFile().getPath();

                File file = new File(presentPath);
                if (file.isDirectory()) {
                    recyclerViewAdapter.changeDirectory(presentPath, getRecyclerViewItemList(presentPath));
                    recyclerView.setAdapter(recyclerViewAdapter);
                    recyclerView.refreshDrawableState();
                } else if (MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString()).equals("txt")) {
                    NavDirections navDirections = FileExploreFragmentDirections.actionNavFileExploreToNavViewAndEdit();
                    Navigation.findNavController(view).navigate(navDirections);
                }
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
    public void onPause() {
        super.onPause();

        onBackPressedCallback.setEnabled(false);
    }

    public ArrayList<RecyclerViewItem> getRecyclerViewItemList(String presentPath) {
        ArrayList<RecyclerViewItem> recyclerViewItemArrayList = new ArrayList<>();
        File file = new File(presentPath);
        File[] fileList = file.listFiles();

        if (file != null) {
            if (fileList != null)
                Arrays.sort(fileList);

            for (int i = 0; i < fileList.length; i++) {
                File file_i = fileList[i];
                int drawableId = -1;

                if (file_i.isDirectory())
                    drawableId = R.drawable.ic_baseline_folder_24_black;
                else if (MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file_i).toString()).equals("txt"))
                    drawableId = R.drawable.ic_baseline_text_snippet_24_black;

                if (drawableId != -1) {
                    RecyclerViewItem recyclerViewItem = new RecyclerViewItem(file_i, ContextCompat.getDrawable(getContext(), drawableId));
                    recyclerViewItemArrayList.add(recyclerViewItem);
                }
            }
        }

        return recyclerViewItemArrayList;
    }
}