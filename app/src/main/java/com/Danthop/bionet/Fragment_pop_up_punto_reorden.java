package com.Danthop.bionet;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class Fragment_pop_up_punto_reorden extends DialogFragment {

    public Fragment_pop_up_punto_reorden() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pop_up_punto_reorden,container, false);


        Button agregar = (Button) v.findViewById(R.id.btnAgregar);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_pop_up_traslados dialog = new Fragment_pop_up_traslados();  //
                dialog.setTargetFragment(Fragment_pop_up_punto_reorden.this, 1);
                dialog.show(getFragmentManager(), "MyCustomDialog");
                onDestroyView();
            }
        });

        Button back = v.findViewById(R.id.btnSalir);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    onDestroyView();
            }
        });

        return v;
    }
}

