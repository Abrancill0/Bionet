package com.Danthop.bionet;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import lecho.lib.hellocharts.view.PieChartView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PantallaPrincipal extends Fragment {


    public PantallaPrincipal() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pantalla_principal,container, false);
        PieChartView pieChartView = v.findViewById(R.id.chart);
        return v;
    }

}
