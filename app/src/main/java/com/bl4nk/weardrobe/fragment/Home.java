package com.bl4nk.weardrobe.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.bl4nk.weardrobe.MainActivity;
import com.bl4nk.weardrobe.R;
import com.bl4nk.weardrobe.UserViewModel;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {

    private Button formalBtn;
    private Button casualBtn;
    private Button sportswearBtn;
    private ImageButton addImage;
    UserViewModel userViewModel;
    MainActivity activity;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activity = (MainActivity) getActivity();

        userViewModel = activity.getSharedViewModel();

        formalBtn = view.findViewById(R.id.formalBtn);
        addImage = view.findViewById(R.id.addImage);
        casualBtn = view.findViewById(R.id.casualBtn);
        sportswearBtn = view.findViewById(R.id.sportswearBtn);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);



        formalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userViewModel.setSelectedStyle("Formal");
                activity.notifyPagerAdapterDataSetChanged();
                TabLayout.Tab tab = activity.tabLayout.getTabAt(1); // Index 1 represents the "Recommendation" tab
                if (tab != null) {
                    tab.select();
                }
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.receiveFragment(new Closet());
            }
        });

        casualBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userViewModel.setSelectedStyle("Casual");
                activity.notifyPagerAdapterDataSetChanged();
                TabLayout.Tab tab = activity.tabLayout.getTabAt(1); // Index 1 represents the "Recommendation" tab
                if (tab != null) {
                    tab.select();
                }
            }
        });

        sportswearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userViewModel.setSelectedStyle("Sports Wear");
                activity.notifyPagerAdapterDataSetChanged();
                TabLayout.Tab tab = activity.tabLayout.getTabAt(1); // Index 1 represents the "Recommendation" tab
                if (tab != null) {
                    tab.select();
                }
            }
        });
    }

}