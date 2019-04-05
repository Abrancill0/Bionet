package com.Danthop.bionet;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class Fragment_pop_up_traslado_exitoso extends DialogFragment {
    public Fragment_pop_up_traslado_exitoso() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pop_up_traslado_exitoso,container, false);

        Button back = v.findViewById(R.id.btnAceptarTraslado);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroyView();
            }
        });

        return v;
    }
    }
