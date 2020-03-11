package br.com.calcard.android.app.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import br.com.calcard.android.app.R;


public class Onboard2 extends Fragment {
    public Onboard2() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_onboard_2, container, false);
        if (container == null) {
            return null;
        }
        return view;

    }
}
