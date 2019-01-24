package com.aplication.bionet;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.aplication.bionet.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_pop_up_direccion_fiscal extends DialogFragment {


    public Fragment_pop_up_direccion_fiscal() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pop_up_direccion_fiscal,container, false);
        Button guardar = (Button) v.findViewById(R.id.btn_guardar_cliente);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_clientes()).commit();
                dismiss();
            }
        });
        return v;

    }

}
