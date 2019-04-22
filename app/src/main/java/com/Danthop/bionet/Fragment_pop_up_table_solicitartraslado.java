package com.Danthop.bionet;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Fragment_pop_up_table_solicitartraslado extends DialogFragment {
    private Button solicitarTraslado;
    private Button CancelarTraslado;
    private TextView articulos;
    public Fragment_pop_up_table_solicitartraslado() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pop_up_table_solicitartraslado,container, false);

        articulos=(TextView)v.findViewById(R.id.articulos);
        solicitarTraslado = (Button) v.findViewById(R.id.solicitarTraslado);

        solicitarTraslado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Fragment_pop_up_traslado_exitoso dialog = new Fragment_pop_up_traslado_exitoso();
                dialog.setTargetFragment(Fragment_pop_up_table_solicitartraslado.this, 1);
                dialog.show(getFragmentManager(), "MyCustomDialog");
                onDestroyView();
            }
        });

        CancelarTraslado = v.findViewById(R.id.CancelarTraslado);
        CancelarTraslado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroyView();
            }
        });

        return v;
    }
    }
