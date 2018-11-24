package com.phanton.testpoint;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Android on 17/4/17.
 */

public class MyFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        LayoutInflater layoutInflater=LayoutInflater.from(getActivity());
        return layoutInflater.inflate(R.layout.item,null);
    }
}
