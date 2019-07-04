package com.Danthop.bionet;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_ver_cliente extends Fragment {


    public Fragment_ver_cliente() {
        // Required empty public constructor
    }

    private Button btn_informacion_general;
    private Button btn_cuentas_cobrar;
    private View layout_informacion_general;
    private View layout_cuentas_cobrar;
    private ImageView Regresar;
    private FragmentTransaction fr;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_clientes_ver, container, false);
        LoadLayoutsAndButtons(v);
        return v;
    }

    private void LoadLayoutsAndButtons(View v)
    {
        fr = getFragmentManager().beginTransaction();
        btn_informacion_general = v.findViewById(R.id.btn_informacion_general);
        btn_cuentas_cobrar = v.findViewById(R.id.btn_cuentas_por_cobrar);
        layout_informacion_general = v.findViewById(R.id.layout_informacion);
        layout_cuentas_cobrar = v.findViewById(R.id.layout_cuentas_cobrar);
        Regresar = v.findViewById(R.id.atras);

        btn_informacion_general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_informacion_general.setBackgroundColor(getResources().getColor(R.color.fondo_azul));
                btn_cuentas_cobrar.setBackgroundResource(R.drawable.pestanas_desplegables);
                layout_informacion_general.setVisibility(View.VISIBLE);
                layout_cuentas_cobrar.setVisibility(View.GONE);
            }
        });

        btn_cuentas_cobrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_cuentas_cobrar.setBackgroundColor(getResources().getColor(R.color.fondo_azul));
                btn_informacion_general.setBackgroundResource(R.drawable.pestanas_desplegables);
                layout_cuentas_cobrar.setVisibility(View.VISIBLE);
                layout_informacion_general.setVisibility(View.GONE);
            }
        });

        Regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_clientes fragment_clientes = new Fragment_clientes();
                fr.replace(R.id.fragment_container, fragment_clientes).commit();
            }
        });

    }

}
