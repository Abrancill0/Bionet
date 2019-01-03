package com.example.jl.bionet;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_crear_cliente extends Fragment {

    Dialog DireccionFiscal;
    public Fragment_crear_cliente() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crear_cliente,container, false);
        Button guardar = (Button) v.findViewById(R.id.verificar_fiscal);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_direccion_fiscal dialog = new Fragment_direccion_fiscal();
                dialog.setTargetFragment(Fragment_crear_cliente.this, 1);
                dialog.show(getFragmentManager(), "MyCustomDialog");
            }
        });
        return v;
    }


}
