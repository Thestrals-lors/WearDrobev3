package com.bl4nk.weardrobe.fragment;

import static com.bl4nk.weardrobe.fragment.Home.ARG_PARAM1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bl4nk.weardrobe.MainActivity;
import com.bl4nk.weardrobe.R;
import com.bl4nk.weardrobe.adapter.ImageAdapter;
import com.bl4nk.weardrobe.fragment.Closet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainCloset extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ImageButton addImage;

    ImageAdapter adapter;
    private List<File> imageFiles = new ArrayList<>();
    private List<String> imageNames = new ArrayList<>();
    private List<Bitmap> imageBitmaps = new ArrayList<>();

    private static final String WEAR_DROBE_DIRECTORY = Environment.DIRECTORY_PICTURES + "/WearDrobe";

    private MainActivity activity;

    public MainCloset() {
        // Required empty public constructor
    }

    private String mParam1;
    private String mParam2;

    public static MainCloset newInstance(String param1, String param2) {
        MainCloset fragment = new MainCloset();
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
        return inflater.inflate(R.layout.fragment_main_closet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activity = (MainActivity) getActivity();

        addImage = view.findViewById(R.id.addImage);

        RecyclerView recyclerView = view.findViewById(R.id.imageRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3)); // Set 3 columns

        refreshImageData();

        adapter = new ImageAdapter(getContext(), imageBitmaps, imageNames);
        recyclerView.setAdapter(adapter);

        adapter.setOnDeleteClickListener(new ImageAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                // Get the file to be deleted
                File fileToDelete = imageFiles.get(position);

                // Delete the file from storage
                boolean deleted = fileToDelete.delete();
                if (deleted) {
                    // If the file is deleted successfully, remove it from the lists
                    imageFiles.remove(position);
                    imageNames.remove(position);
                    imageBitmaps.remove(position);

                    // Notify the adapter of the data change
                    adapter.notifyDataSetChanged();
                }
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.receiveFragment(new Closet());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the image files, names, and bitmaps
        refreshImageData();
    }

    private void refreshImageData() {
        // Clear the current lists
        imageFiles.clear();
        imageNames.clear();
        imageBitmaps.clear();

        // Reload the image files
        imageFiles = getImageFiles(new File(Environment.getExternalStorageDirectory(), WEAR_DROBE_DIRECTORY));

        // Extract names from file names and decode bitmaps
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
        Pattern pattern = Pattern.compile("^(.+?)_.*$");

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
                    // Exclude the "Favorites" directory
                    if (!file.getName().equalsIgnoreCase("Favorites")) {
                        imageFiles.addAll(getImageFiles(file)); // Recursively scan subdirectories
                    }
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
