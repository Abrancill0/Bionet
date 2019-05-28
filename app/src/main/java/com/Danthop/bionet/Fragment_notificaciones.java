package com.Danthop.bionet;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.Adapters.NotificacionAdapter;
import com.Danthop.bionet.model.NotificacionModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_notificaciones extends Fragment {

    private List<NotificacionModel> mNotificacionList;
    private RecyclerView mRecyclerView;
    private NotificacionAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Integer> posicionesDelete = new ArrayList();
    private String usu_id;
    private View v;
    private TextView NumNotificaciones;
    private Dialog pop_detail_notification;


    String text = "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem.";


    public Fragment_notificaciones() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_notificaciones,container, false);
        NumNotificaciones = v.findViewById(R.id.num_notificaciones);
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id", "");
        LoadNotificaciones();


        Button delete = v.findViewById(R.id.btn_eliminar);

        delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                JSONArray aNotificaciones = new JSONArray();
                for (int j=0; j<posicionesDelete.size();j++)
                {
                    aNotificaciones.put(mNotificacionList.get(j).getID());
                }

                System.out.println(aNotificaciones);

                JSONObject request = new JSONObject();
                try
                {
                    request.put("usu_id", usu_id);
                    request.put("esApp", "1");
                    request.put("aNotificaciones",aNotificaciones);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

                String url = getString(R.string.Url);

                String ApiPath = url + "/api/notificaciones/destroy_notificaciones";

                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONObject Respuesta = null;
                        JSONArray NodoConfiguraciones;

                        try {

                            int status = Integer.parseInt(response.getString("estatus"));
                            String Mensaje = response.getString("mensaje");


                            if (status == 1)
                            {
                                for(int i = 0;i<posicionesDelete.size();i++){
                                    removeItem(posicionesDelete.get(i));
                                    refresh();
                                }
                                posicionesDelete.clear();
                                LoadNotificaciones();
                            }
                            else
                            {
                                Toast toast1 =
                                        Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);

                                toast1.show();


                            }

                        } catch (JSONException e) {

                            Toast toast1 =
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);

                            toast1.show();


                        }

                    }

                },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast toast1 =
                                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG);

                                toast1.show();


                            }
                        }
                );

                VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);

            }
        });

        return v;

    }

    public void refresh(){
        for(int i = 0;i<posicionesDelete.size();i++){
            posicionesDelete.set(i,posicionesDelete.get(i)-1);
        }
    }

    public void removeItem(int position) {
            mNotificacionList.remove(position);
            mAdapter.notifyItemRemoved(position);
    }

    public void LoadNotificaciones() {
        mNotificacionList = new ArrayList<>();
        try {
            String ApiPath = "http://187.189.192.150:8010/api/notificaciones/index?usu_id=" + usu_id + "&esApp=1";

            // prepare the Request
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ApiPath, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                int EstatusApi = Integer.parseInt(response.getString("estatus"));

                                if (EstatusApi == 1) {

                                    JSONObject resultado = response.getJSONObject("resultado");
                                    JSONArray NodoNotificaciones = resultado.getJSONArray("aNotificaciones");
                                    for(int x=0;x<NodoNotificaciones.length();x++)
                                    {
                                        JSONObject elemento = NodoNotificaciones.getJSONObject(x);
                                        JSONObject NodoID = elemento.getJSONObject("nen_id");
                                        String ID = NodoID.getString("uuid");
                                        String Titulo = elemento.getString("nen_titulo");
                                        String Fecha = elemento.getString("nen_fecha_hora_creo");
                                        String Mensaje = elemento.getString("nen_mensaje");
                                        String Visto = elemento.getString("nen_visto");
                                        NotificacionModel Notificacion = new NotificacionModel(Titulo,ID,Fecha,Mensaje,Visto);
                                        mNotificacionList.add(Notificacion);
                                    }
                                    NumNotificaciones.setText("("+mNotificacionList.size()+")");
                                    buildRecyclerView(v);
                                }
                            } catch (JSONException e) {
                                Toast toast1 =
                                        Toast.makeText(getContext(),
                                                String.valueOf(e), Toast.LENGTH_LONG);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast toast1 =
                                    Toast.makeText(getContext(),
                                            String.valueOf(error), Toast.LENGTH_LONG);
                        }
                    }
            );
            getRequest.setShouldCache(false);

            VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(getRequest);
        } catch (Error e) {
            e.printStackTrace();
        }
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
                pop_detail_notification = new Dialog(getContext());
                pop_detail_notification.setContentView(R.layout.pop_up_notificacion_detail);
                pop_detail_notification.show();
                TextView Titulo = pop_detail_notification.findViewById(R.id.TextDetailTitulo);
                TextView Fecha = pop_detail_notification.findViewById(R.id.TextDetailFecha);
                TextView Mensaje = pop_detail_notification.findViewById(R.id.TextDetailMensaje);

                Titulo.setText(mNotificacionList.get(position).getTitulo());
                Fecha.setText(mNotificacionList.get(position).getFecha());
                Mensaje.setText(mNotificacionList.get(position).getMensaje());


                JSONObject request = new JSONObject();
                try
                {
                    request.put("usu_id", usu_id);
                    request.put("esApp", "1");
                    request.put("nen_id",mNotificacionList.get(position).getID());
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

                String url = getString(R.string.Url);

                String ApiPath = url + "/api/notificaciones/marcar_visto";

                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONObject Respuesta = null;
                        JSONArray NodoConfiguraciones;

                        try {

                            int status = Integer.parseInt(response.getString("estatus"));
                            String Mensaje = response.getString("mensaje");


                            if (status == 1)
                            {
                                LoadNotificaciones();
                            }
                            else
                            {
                                Toast toast1 =
                                        Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);

                                toast1.show();


                            }

                        } catch (JSONException e) {

                            Toast toast1 =
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);

                            toast1.show();


                        }

                    }

                },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast toast1 =
                                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG);

                                toast1.show();


                            }
                        }
                );

                VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);



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
                    }
                }
                else
                {
                    posicionesDelete.add(position);
                }


            }
        });
    }

}
