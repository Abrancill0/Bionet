package com.Danthop.bionet;


import android.app.Dialog;
import android.app.ProgressDialog;
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
    private ProgressDialog progressDialog;
    private JSONArray Roles;
    private boolean Elimina_Notificacion=false;
    private boolean Listado_Notificacion=false;
    private Button delete;
    private String code="";


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
        code = sharedPref.getString("sso_code","");


        //Funcion para Obtener Permisos
        try {
            Roles = new JSONArray(sharedPref.getString("sso_Roles", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        boolean Aplica = false;
        boolean Aplica_Permiso = false;
        String fun_id = "";

        String rol_id = "";

        for (int i = 0; i < Roles.length(); i++) {
            try {

                JSONObject Elemento = Roles.getJSONObject(i);
                rol_id = Elemento.getString("rol_id");

                if (rol_id.equals("cbcf943b-ed1e-11e8-8a6e-cb097f5c03df")) {
                    Aplica = Elemento.getBoolean("rol_aplica_en_version");
                    Aplica_Permiso = Elemento.getBoolean("rol_permiso");

                    if (Aplica == true) {
                        if (Aplica_Permiso == true) {

                            JSONArray Elemento1 = Elemento.getJSONArray("rol_funciones");

                            for (int x = 0; x < Elemento1.length(); x++) {

                                JSONObject Elemento2 = Elemento1.getJSONObject(x);

                                fun_id = Elemento2.getString("fun_id");

                                switch (fun_id) {

                                    case "6c4f52e5-4f69-4fe4-b9f8-9ec0bce58aaa":

                                        Elimina_Notificacion = Elemento2.getBoolean("fun_permiso");
                                        break;

                                    case "5ba36503-0ab7-49ea-9ec8-91d2ee9af17d":

                                        Listado_Notificacion = Elemento2.getBoolean("fun_permiso");
                                        break;

                                    default:
                                        break;
                                }
                            }
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Espere un momento por favor");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        LoadNotificaciones();

        delete = v.findViewById(R.id.btn_eliminar);

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
                    request.put("code",code);
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
                                progressDialog.dismiss();
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

        LoadPermisosFunciones();

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
            String ApiPath = "http://187.189.192.150:8010/api/notificaciones/index?usu_id=" + usu_id + "&esApp=1&code="+code;

            // prepare the Request
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ApiPath, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                int EstatusApi = Integer.parseInt(response.getString("estatus"));

                                if (EstatusApi == 1) {

                                    progressDialog.dismiss();

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
                    request.put("code",code);
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

    private void LoadPermisosFunciones()
    {
        if(Elimina_Notificacion=true)
        {

        }else
        {
            delete.setEnabled(false);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast toast1 =
                            Toast.makeText(getContext(), "No cuentas con los permisos necesarios para \n realizar esta acción", Toast.LENGTH_LONG);

                    toast1.show();
                }
            });
        }

        if(Listado_Notificacion=true)
        {

        }else{
            mNotificacionList.clear();
            mAdapter.notifyDataSetChanged();
        }
    }



}
