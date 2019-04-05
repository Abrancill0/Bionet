package com.Danthop.bionet;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_pop_up_traslados extends DialogFragment {


    public Fragment_pop_up_traslados() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pop_up_traslados,container, false);


       Button trasladar = (Button) v.findViewById(R.id.ImgAceptar);
        trasladar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_pop_up_traslado_exitoso dialog = new Fragment_pop_up_traslado_exitoso();
                dialog.setTargetFragment(Fragment_pop_up_traslados.this, 1);
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
