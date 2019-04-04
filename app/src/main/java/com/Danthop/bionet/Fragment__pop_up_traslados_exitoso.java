package com.Danthop.bionet;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Fragment__pop_up_traslados_exitoso extends DialogFragment {

    Button btnSalir;

    public Fragment__pop_up_traslados_exitoso() {

        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pop_up_traslado_agregar_producto,container, false);


        //btnSalir = (Button)findViewByid(R.id.btnSalir);



        return v;
}




}

