package com.bl4nk.weardrobe.fragment;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;

import org.apache.commons.io.FileUtils;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bl4nk.weardrobe.MainActivity;
import com.bl4nk.weardrobe.R;
import com.bl4nk.weardrobe.UserViewModel;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class Recomendation extends Fragment {

    private TextView selectedStyleTv;
    private Button acceptBtn;
    private Button declineBtn;
    private ImageView imageView;
    private ImageView topClothes;
    private ImageView bottomClothes;
    UserViewModel userViewModel;

    private File randomTopFile;
    private File randomBottomFile;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Recomendation() {
        // Required empty public constructor
    }

    public static Recomendation newInstance(String param1, String param2) {
        Recomendation fragment = new Recomendation();
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

        MainActivity activity = (MainActivity) requireActivity();
        userViewModel = activity.getSharedViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recomendation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        selectedStyleTv = view.findViewById(R.id.selectedStyleTv);
        acceptBtn = view.findViewById(R.id.acceptBtn);
        declineBtn = view.findViewById(R.id.declineBtn);
        imageView = view.findViewById(R.id.imageView);
        topClothes = view.findViewById(R.id.topClothes);
        bottomClothes = view.findViewById(R.id.bottomClothes);

        declineBtn.setOnClickListener(v -> {
            String style = selectedStyleTv.getText().toString();
            loadRandomImages(style);
        });

        acceptBtn.setOnClickListener(v -> {
            String style = selectedStyleTv.getText().toString();
            copyToFavoriteFolder(style, randomTopFile, randomBottomFile);
        });

        userViewModel.getSelectedStyle().observe(getViewLifecycleOwner(), style -> {
            if (style != null) {
                selectedStyleTv.setText(style);
                // Load random images from the appropriate directory based on the selected style
                loadRandomImages(style);
            } else {
                // If style is null, display "Select style in home"
                selectedStyleTv.setText("Select style in home");
            }
        });

    }

    //    private void copyToFavoriteFolder(String style) {
//        String sourceDirectory = "WearDrobe/" + style;
//        File sourceDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), sourceDirectory);
//        File destinationDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "WearDrobe/Favorites");
//
//        if (!destinationDir.exists()) {
//            // Create the "Favorites" folder if it doesn't exist
//            if (!destinationDir.mkdirs()) {
//                Toast.makeText(getContext(), "Failed to create Favorites folder", Toast.LENGTH_SHORT).show();
//                return;
//            }
//        }
//
//        if (sourceDir.exists() && destinationDir.exists()) {
//            File[] sourceFiles = sourceDir.listFiles();
//
//            if (sourceFiles != null && sourceFiles.length > 0) {
//                // Get the selected image
//                File selectedImage = topClothes.getDrawable() != null ? new File(topClothes.getTag().toString()) : null;
//                if (selectedImage == null || !selectedImage.exists()) {
//                    selectedImage = bottomClothes.getDrawable() != null ? new File(bottomClothes.getTag().toString()) : null;
//                }
//                if (selectedImage != null && selectedImage.exists()) {
//                    // Copy the selected image to the "Favorites" folder with the same name
//                    File destinationFile = new File(destinationDir, selectedImage.getName());
//                    // Use MediaStore API to save the image
//                    ContentValues values = new ContentValues();
//                    values.put(MediaStore.Images.Media.DISPLAY_NAME, selectedImage.getName());
//                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
//                    values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/WearDrobe/Favorites");
//
//                    // Insert the image into the MediaStore
//                    Uri imageUri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//                    if (imageUri != null) {
//                        try {
//                            OutputStream outputStream = getContext().getContentResolver().openOutputStream(imageUri);
//                            if (outputStream != null) {
//                                // Copy the selected image to the output stream
//                                FileUtils.copyFile(selectedImage, outputStream);
//                                outputStream.close();
//                                Toast.makeText(getContext(), "Image copied to Favorites", Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                } else {
//                    Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Toast.makeText(getContext(), "No images found in " + style + " directory", Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            Toast.makeText(getContext(), "Source or destination directory does not exist", Toast.LENGTH_SHORT).show();
//        }
//    }

    //    private void copyToFavoriteFolder(String style) {
//        // Check if top and bottom directories exist
//        String topDirectory = "Top " + style;
//        String bottomDirectory = "Bottom " + style;
//        File topDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "WearDrobe/" + topDirectory);
//        File bottomDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "WearDrobe/" + bottomDirectory);
//
//        if (!topDir.exists() || !bottomDir.exists()) {
//            // Top or bottom directory doesn't exist
//            Toast.makeText(getContext(), "Top or bottom directory does not exist", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Create the "Favorites" folder if it doesn't exist
//        File destinationDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "WearDrobe/Favorites");
//        if (!destinationDir.exists()) {
//            if (!destinationDir.mkdirs()) {
//                Toast.makeText(getContext(), "Failed to create Favorites folder", Toast.LENGTH_SHORT).show();
//                return;
//            }
//        }
//
//        // Get the selected images from both top and bottom directories
//        File selectedTopImage = topClothes.getTag() != null ? new File(topClothes.getTag().toString()) : null;
//        File selectedBottomImage = bottomClothes.getTag() != null ? new File(bottomClothes.getTag().toString()) : null;
//
//        // Check if the selected images exist and copy them to the "Favorites" folder
//        if (selectedTopImage != null && selectedTopImage.exists()) {
//            copyImageToFavorite(selectedTopImage, destinationDir);
//        } else {
//            Toast.makeText(getContext(), "No top image selected", Toast.LENGTH_SHORT).show();
//        }
//
//        if (selectedBottomImage != null && selectedBottomImage.exists()) {
//            copyImageToFavorite(selectedBottomImage, destinationDir);
//        } else {
//            Toast.makeText(getContext(), "No bottom image selected", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void copyToFavoriteFolder(String style, File topFile, File bottomFile) {
        // Create the "Favorites" folder if it doesn't exist
        File destinationDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "WearDrobe/Favorites");
        if (!destinationDir.exists()) {
            if (!destinationDir.mkdirs()) {
                Toast.makeText(getContext(), "Failed to create Favorites folder", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Copy the selected images to the "Favorites" folder with the same name
        copyImageToFavorite(topFile, "Top" + style);
        copyImageToFavorite(bottomFile, "Bottom" + style);
        Toast.makeText(getContext(), "Images copied to Favorites", Toast.LENGTH_SHORT).show();
    }
    private void copyImageToFavorite(File selectedImage, String style) {
        // Check if the image exists and if it's not null
        if (selectedImage != null && selectedImage.exists()) {
            // Create the "Favorites" folder if it doesn't exist
            File destinationDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "WearDrobe/Favorites");
            if (!destinationDir.exists()) {
                if (!destinationDir.mkdirs()) {
                    Toast.makeText(getContext(), "Failed to create Favorites folder", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // Generate a unique identifier to pair the images
            String pairIdentifier = style + System.currentTimeMillis(); // Use current time as an identifier

            // Rename the selected image with the pair identifier
            String originalFileName = selectedImage.getName();
            String newFileName = pairIdentifier + "_" + originalFileName;
            File renamedFile = new File(destinationDir, newFileName);

            // Copy the selected image to the "Favorites" folder with the new name
            try {
                FileUtils.copyFile(selectedImage, renamedFile);
                // Refresh the MediaStore to show the copied image in gallery apps
//                MediaStore.Images.Media.insertImage(getContext().getContentResolver(), renamedFile.getAbsolutePath(), renamedFile.getName(), null);
                Toast.makeText(getContext(), "Clothes add to Favorites", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Failed to add clothes to Favorites", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void loadRandomImages(String style) {
        String topDirectory = "Top " + style;
        String bottomDirectory = "Bottom " + style;
        File topDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "WearDrobe/" + topDirectory);
        File bottomDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "WearDrobe/" + bottomDirectory);

        if (topDir.exists() && bottomDir.exists()) {
            File[] topFiles = topDir.listFiles();
            File[] bottomFiles = bottomDir.listFiles();

            if (topFiles != null && topFiles.length > 0 && bottomFiles != null && bottomFiles.length > 0) {
                // Get random images from top and bottom directories
                Random random = new Random();
                randomTopFile = topFiles[random.nextInt(topFiles.length)];
                randomBottomFile = bottomFiles[random.nextInt(bottomFiles.length)];

                // Load images into ImageViews
                topClothes.setImageURI(Uri.fromFile(randomTopFile));
                bottomClothes.setImageURI(Uri.fromFile(randomBottomFile));
            } else {
                Toast.makeText(getContext(), "No images found in " + style + " directories", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Directories for " + style + " do not exist", Toast.LENGTH_SHORT).show();
        }
    }
}
