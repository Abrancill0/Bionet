package com.Danthop.bionet;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.Danthop.bionet.model.Caja_notificacion;
import com.Danthop.bionet.Adapters.NotificacionAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_notificaciones extends Fragment {

    private ArrayList<Caja_notificacion> mNotificacionList;
    private RecyclerView mRecyclerView;
    private NotificacionAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Integer> posicionesDelete = new ArrayList();



    String text = "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem.";


    public Fragment_notificaciones() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notificaciones,container, false);
        createExampleList();
        buildRecyclerView(v);


        Button delete = v.findViewById(R.id.btn_eliminar);

        delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                for(int i = 0;i<posicionesDelete.size();i++){
                    removeItem(posicionesDelete.get(i));
                    refresh();
                }
                posicionesDelete.clear();

            }
        });

        return v;

    }

    public void refresh(){
        for(int i = 0;i<posicionesDelete.size();i++){
            posicionesDelete.set(i,posicionesDelete.get(i)-1);
            System.out.println(posicionesDelete.get(i));
        }
    }

    public void removeItem(int position) {
            mNotificacionList.remove(position);
            mAdapter.notifyItemRemoved(position);
    }

    public void createExampleList() {
        mNotificacionList = new ArrayList<>();
        mNotificacionList.add(new Caja_notificacion("22/12/2018","12:32",text));
        mNotificacionList.add(new Caja_notificacion("25/12/2018","11:32",text));
        mNotificacionList.add(new Caja_notificacion("26/12/2018","06:32",text));
        mNotificacionList.add(new Caja_notificacion("25/12/2018","11:32",text));
        mNotificacionList.add(new Caja_notificacion("26/12/2018","06:32",text));
    }

    public void buildRecyclerView(View v) {
        mRecyclerView = v.findViewById(R.id.RecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new NotificacionAdapter(mNotificacionList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new NotificacionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mNotificacionList.get(position);
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_notificacion_detail()).commit();
            }

            @Override
            public void onDeleteClick(int position) {
                int x;
                int y=0;
                if(posicionesDelete.size()>0)
                {
                    for(x=1;x<=posicionesDelete.size();x++)
                    {
                        if(position==posicionesDelete.get(x-1)){
                            posicionesDelete.remove(x-1);
                            y=1;
                            break;
                        }
                    }
                    x--;
                    if(y==0)
                    {
                        posicionesDelete.add(position);
                        System.out.println(posicionesDelete);
                    }
                }
                else
                {
                    posicionesDelete.add(position);
                }
                System.out.println(posicionesDelete);


            }
        });
    }

}
