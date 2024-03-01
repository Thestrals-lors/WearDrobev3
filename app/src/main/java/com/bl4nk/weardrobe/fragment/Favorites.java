package com.bl4nk.weardrobe.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bl4nk.weardrobe.R;
import com.bl4nk.weardrobe.adapter.FavoriteImaegAdapter;
import com.bl4nk.weardrobe.adapter.ImageAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Favorites extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private FavoriteImaegAdapter adapter;
    private List<File> imageFiles = new ArrayList<>();
    private List<String> imageNames = new ArrayList<>();
    private List<Bitmap> imageBitmaps = new ArrayList<>();

    private static final String FAVORITES_DIRECTORY = Environment.DIRECTORY_PICTURES + "/WearDrobe/Favorites";

    public Favorites() {
        // Required empty public constructor
    }

    public static Favorites newInstance(String param1, String param2) {
        Favorites fragment = new Favorites();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.imageRecyclerView);
//        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3)); // Set 3 columns

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
        refreshImageData();

        adapter = new FavoriteImaegAdapter(getContext(), imageBitmaps, imageNames);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshImageData();
    }

    private void refreshImageData() {
        imageFiles.clear();
        imageNames.clear();
        imageBitmaps.clear();

        imageFiles = getImageFiles(new File(Environment.getExternalStorageDirectory(), FAVORITES_DIRECTORY));

        for (File file : imageFiles) {
            imageNames.add(extractNameFromFileName(file.getName()));
            imageBitmaps.add(BitmapFactory.decodeFile(file.getAbsolutePath()));
        }

        // Notify the adapter of the data change
        if (adapter != null) {
            adapter.setImageBitmaps(imageBitmaps);
            adapter.setImageNames(imageNames);
            adapter.notifyDataSetChanged();
        }
    }

    private String extractNameFromFileName(String fileName) {
        // Define the pattern to match the name
        Pattern pattern = Pattern.compile("^([a-zA-Z]+).*");

        // Create a matcher object
        Matcher matcher = pattern.matcher(fileName);

        // Check if the pattern matches the file name
        if (matcher.matches()) {
            // Extract the name group from the matcher
            return matcher.group(1);
        } else {
            // If the pattern does not match, return the original file name
            return fileName;
        }
    }

    private List<File> getImageFiles(File directory) {
        List<File> imageFiles = new ArrayList<>();
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    imageFiles.addAll(getImageFiles(file));
                } else {
                    String filePath = file.getAbsolutePath();
                    if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg") || filePath.endsWith(".png")) {
                        imageFiles.add(file);
                    }
                }
            }
        }
        return imageFiles;
    }
}
