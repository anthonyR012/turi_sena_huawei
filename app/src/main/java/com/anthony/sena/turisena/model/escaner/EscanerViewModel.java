package com.anthony.sena.turisena.model.escaner;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;



public class EscanerViewModel extends ViewModel {

        private MutableLiveData<String> mText;



        public EscanerViewModel() {
            mText = new MutableLiveData<>();
            mText.setValue("This is notifications fragment");
        }

        public LiveData<String> getText() {
            return mText;
        }




    }