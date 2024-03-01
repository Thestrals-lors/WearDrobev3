package com.bl4nk.weardrobe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserViewModel extends ViewModel {

    private MutableLiveData<String> selectedStyle = new MutableLiveData<>();

    public void setSelectedStyle(String style) {
        selectedStyle.setValue(style);
    }

    public LiveData<String> getSelectedStyle() {
        return selectedStyle;
    }
}
