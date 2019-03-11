package com.Danthop.bionet;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.Danthop.bionet.Adapters.CategoriaAdapter;
import com.Danthop.bionet.model.CategoriaModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_selecciona_categoria extends Fragment {

    private ListView listacategoria;
    private String UserML;
    private String AccesToken;
    private Fragment_selecciona_categoria fragment;
    private String nombre;
    private String descripcion;
    private String usu_id;
    private Bundle bundle;
    FragmentTransaction fr;
    private Button back;
    private String idVacio;


    public Fragment_selecciona_categoria() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_selecciona_categoria,container, false);
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences( "DatosPersistentes", getActivity().MODE_PRIVATE );
        UserML = sharedPref.getString( "UserIdML", "" );
        AccesToken = sharedPref.getString( "AccessToken", "" );
        fr = getFragmentManager().beginTransaction();
        back = v.findViewById(R.id.atras);
        idVacio="";

        bundle = getArguments();

        nombre = bundle.getString( "nombre");
        descripcion = bundle.getString( "descripcion");
        usu_id = sharedPref.getString( "usu_id", "" );


        listacategoria = (ListView) v.findViewById(R.id.listView_selecciona);
        cargaCategorias();

        return v;
    }


    public void cargaCategorias(){
        {
            final String url = "http://187.189.192.150:8010/api/ecomerce/create_app/access_token=" + AccesToken  + "&expires_in=21600&user_id=" + UserML + "&domains=localhost" + "?&usu_id=" + usu_id + "&esApp=1";

            // prepare the Request
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response) {
                            // display response
                            JSONArray RespuestaTiposPublicacion = null;
                            JSONArray RespuestaCategoria = null;

                            try
                            {

                                int EstatusApi = Integer.parseInt( response.getString("estatus") );

                                if (EstatusApi == 1) {

                                    RespuestaCategoria = response.getJSONArray("aCategorias");

                                    ArrayList arrayList = new ArrayList<>();

                                    for(int x = 0; x < RespuestaCategoria.length(); x++){
                                        JSONObject jsonObject1 = RespuestaCategoria.getJSONObject(x);
                                        String idcategoria = jsonObject1.getString("id");
                                        String categoria = jsonObject1.getString("name");

                                        CategoriaModel cat = new CategoriaModel(idcategoria, categoria );

                                        arrayList.add(cat);//add the hashmap into arrayList

                                    }

                                    CategoriaAdapter adapter = new CategoriaAdapter(getContext(), R.layout.caja_categoria,arrayList,listacategoria,bundle,fr,back,idVacio);

                                    listacategoria.setAdapter(adapter);//sets the adapter for listView


                                }


                            }
                            catch (JSONException e)
                            {   e.printStackTrace();    }


                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error.Response", String.valueOf(error));
                        }
                    }
            );

            VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(getRequest);

        }
    }

}
