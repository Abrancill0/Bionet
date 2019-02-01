package com.Danthop.bionet;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Danthop.bionet.model.Caja_notificacion;
import com.Danthop.bionet.model.NotificacionAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_notificaciones extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    String text = "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem.";


    public Fragment_notificaciones() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notificaciones,container, false);
        ArrayList<Caja_notificacion> arrayNotificacion = new ArrayList<Caja_notificacion>();
        arrayNotificacion.add(new Caja_notificacion("22/12/2018","12:32",text));
        arrayNotificacion.add(new Caja_notificacion("25/12/2018","11:32",text));
        arrayNotificacion.add(new Caja_notificacion("26/12/2018","06:32",text));
        arrayNotificacion.add(new Caja_notificacion("25/12/2018","11:32",text));
        arrayNotificacion.add(new Caja_notificacion("26/12/2018","06:32",text));

        mRecyclerView = v.findViewById(R.id.RecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new NotificacionAdapter(arrayNotificacion);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        return v;

    }

}
