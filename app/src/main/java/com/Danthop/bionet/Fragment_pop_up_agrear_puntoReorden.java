package com.Danthop.bionet;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Fragment_pop_up_agrear_puntoReorden extends DialogFragment {

    public Fragment_pop_up_agrear_puntoReorden() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pop_up_punto_reorden,container, false);


        Button agregar = (Button) v.findViewById(R.id.btnAgregar);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_pop_up_agregar_producto_exitoso dialog = new Fragment_pop_up_agregar_producto_exitoso();  //
                dialog.setTargetFragment(Fragment_pop_up_agrear_puntoReorden.this, 1);
                dialog.show(getFragmentManager(), "MyCustomDialog");
            }
        });

        return v;
    }
}

