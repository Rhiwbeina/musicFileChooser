package com.example.davidladd.musicfilechooser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class dfragment extends Fragment {
    @Nullable
    Button mybutt;
    TextView mytext;


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.dfragment, container, false);
        mytext = view.findViewById(R.id.textView3);
        mybutt = view.findViewById(R.id.buttonFrag);
        mybutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mytext.setText("heeelllooo");

            }
        });
        return view;
    }


}
