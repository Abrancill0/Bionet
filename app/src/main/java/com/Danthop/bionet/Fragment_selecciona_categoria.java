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
import android.widget.ImageView;
import android.widget.ListView;
import com.Danthop.bionet.Adapters.CategoriaAdapter;
import com.Danthop.bionet.model.CategoriaExcepcionModel;
import com.Danthop.bionet.model.CategoriaModel;
import com.Danthop.bionet.model.PublicacionModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

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
    private ImageView back;
    private String idVacio;
    private String idpub;
    public ArrayList ex;
    public Fragment_selecciona_categoria() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_selecciona_categoria,container, false);
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences( "DatosPersistentes", getActivity().MODE_PRIVATE );
        UserML = sharedPref.getString( "UserIdML", "" );
        AccesToken = sharedPref.getString( "AccessToken", "" );
        usu_id = sharedPref.getString( "usu_id", "" );

        fr = getFragmentManager().beginTransaction();
        idVacio="";

        bundle = getArguments();
        nombre = bundle.getString( "nombre");
        descripcion = bundle.getString( "descripcion");
        ex = bundle.getStringArrayList( "ex");

        idpub = bundle.getString("id");

        listacategoria = (ListView) v.findViewById(R.id.listView_selecciona);
        cargaCategorias();

        back = (ImageView) v.findViewById(R.id.atras);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new Fragment_ecommerce_Sincronizar_Articulos()).commit();
            }
        });

        return v;
    }

//---------------------------------------------------------------------------------------------------
    public void cargaCategorias(){ {
            final String url = "http://187.189.192.150:8010/api/ecommerce/create_app?accesstoken=" + AccesToken  + "&user_id_mercado_libre=" + UserML + "&usu_id=" + usu_id + "&esApp=1";
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONArray RespuestaTiposPublicacion = null;
                            JSONArray RespuestaCategoria = null;

                            try {
                                int EstatusApi = Integer.parseInt( response.getString("estatus") );
                                if (EstatusApi == 1) {

                                    RespuestaCategoria = response.getJSONArray("aCategorias");
                                    ArrayList arrayList = new ArrayList<>();

                                    for(int x = 0; x < RespuestaCategoria.length(); x++){

                                        JSONObject jsonObject1 = RespuestaCategoria.getJSONObject(x);

                                        String idcategoria = jsonObject1.getString("id");
                                        String categoria = jsonObject1.getString("name");

                                        boolean QuitaCategoria = false;

                                        Iterator<CategoriaExcepcionModel> itrcat = ex.iterator();
                                        while(itrcat.hasNext()){
                                            CategoriaExcepcionModel Cat = itrcat.next();

                                            String idcat = Cat.getCategoriaID();
                                            String tipopublicacion = Cat.getipopublicacion();

                                            if (idcategoria.equals(idcat)  && idpub.equals(tipopublicacion))
                                            {
                                                QuitaCategoria = true;

                                                break;
                                            }

                                        }


                                        if (QuitaCategoria ==  false)
                                        {
                                            CategoriaModel cat = new CategoriaModel(idcategoria, categoria );
                                            arrayList.add(cat);
                                        }
                                        else
                                        {
                                            QuitaCategoria =  false;
                                        }


                                    }
                                    CategoriaAdapter adapter = new CategoriaAdapter(getContext(), R.layout.caja_categoria,arrayList,listacategoria,bundle,fr,back,idVacio);
                                    listacategoria.setAdapter(adapter);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error.Response", String.valueOf(error));
                        }
                    }
            );
            VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(getRequest);
        }
    }
//---------------------------------------------------------------------------------------------------
}
