package com.example.suitupdaily.filter;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    MutableLiveData<String> mutableLiveData = new MutableLiveData<>();

    //Create set text method
    public void setText(String s) {
        mutableLiveData.setValue(s);
    }

    public MutableLiveData<String> getText() {
        return mutableLiveData;
    }
}
