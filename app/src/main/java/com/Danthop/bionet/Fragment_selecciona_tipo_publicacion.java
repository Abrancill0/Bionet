package com.Danthop.bionet;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.Danthop.bionet.Adapters.CategoriaAdapter;
import com.Danthop.bionet.Adapters.PublicacionAdapter;
import com.Danthop.bionet.Class.MyFirebaseInstanceService;
import com.Danthop.bionet.Tables.SortableArticulosTable;
import com.Danthop.bionet.model.ArticuloModel;
import com.Danthop.bionet.model.CategoriaModel;
import com.Danthop.bionet.model.PublicacionModel;
import com.Danthop.bionet.model.CategoriaExcepcionModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.google.gson.JsonArray;
import com.imagpay.v;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.listeners.TableDataClickListener;

public class Fragment_selecciona_tipo_publicacion extends Fragment {
    private String UserML;
    private String AccesToken;
    private String usu_id;
    private String remaining_listings;
    private String exceptions_by_category;
    private String category_id;
    private String category_name;
    private String idpublicacion;
    private String publicacion;
    private String nombre;
    public String name;
    private String Precio;
    private String Imagen1;
    private String Imagen2;
    public String id;
    public String IdConfiguration;
    private String IdPublicacion;
    private String descripcion;
    private Bundle bundle;
    FragmentTransaction fr;
    private ArrayList<String> TipoPublicacionName;
    private ArrayList<String> TipoPublicacionID;
    private ListView listacategoria;
    private ListView ListaPublicacion;
    private ImageView back;
    private String idVacio;
    private TextView TextView;
    SortableArticulosTable tb;
    SortableArticulosTable tp;

    public Fragment_selecciona_tipo_publicacion() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_selecciona_tipo_publicacion, container, false);

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", getActivity().MODE_PRIVATE);
        fr = getFragmentManager().beginTransaction();
        UserML = sharedPref.getString("UserIdML", "");
        AccesToken = sharedPref.getString("AccessToken", "");
        usu_id = sharedPref.getString("usu_id", "");

        ListaPublicacion = (ListView) v.findViewById(R.id.listView_selecciona_publicacion);
        tb = v.findViewById(R.id.tablaArticulos);
        TipoPublicacionID = new ArrayList<>();
        TipoPublicacionName = new ArrayList<>();

        bundle = getArguments();
        nombre = bundle.getString( "nombre");
        descripcion = bundle.getString( "descripcion");

        CargaPublicaciones();

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

    public void CargaPublicaciones(){
        final String url = "http://187.189.192.150:8010/api/ecommerce/create_app?accesstoken=" + AccesToken  + "&user_id_mercado_libre=" + UserML + "&usu_id=" + usu_id + "&esApp=1";
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject RespuestaTiposPublicacion = null;
                JSONArray  Respuestaavailable = null;
                JSONArray RespuestaCategoria = null;
                JSONArray RespuestaConfiguracion = null;
                JSONArray RespuestaExcepcionCategoria = null;

                try {
                    int EstatusApi = Integer.parseInt( response.getString("estatus") );
                    if (EstatusApi == 1) {
                        ArrayList arrayList = new ArrayList<>();
                        ArrayList arrayList2 = new ArrayList<>();
                        RespuestaTiposPublicacion = response.getJSONObject("aListaTiposPublicacion");
                        Respuestaavailable = RespuestaTiposPublicacion.getJSONArray("available");

                        //REAMING LISTINGS
                        for(int x = 0; x < Respuestaavailable.length(); x++){
                            JSONObject elemento = Respuestaavailable.getJSONObject(x);
                            remaining_listings = elemento.getString("remaining_listings");
                            if (remaining_listings  != ("null")){
                            idpublicacion = elemento.getString("id");
                            publicacion = elemento.getString("name");

                            //CONFIGURATION
                            // entrar a nodo conf
                                //hacer un for del nodo config
                                //dentro del for hacer un if para solo entrar a la categoria q se esta recorriendo
                                RespuestaConfiguracion = RespuestaTiposPublicacion.getJSONArray("configuration");
                                for(int z = 0; z < RespuestaConfiguracion.length(); z++){
                                    JSONObject elementito = RespuestaConfiguracion.getJSONObject(z);

                                    IdConfiguration = elementito.getString("id");

                                   if (idpublicacion.equals(IdConfiguration))
                                    {
                                    RespuestaExcepcionCategoria = elementito.getJSONArray("exceptions_by_category");
                                     for(int y = 0; y < RespuestaExcepcionCategoria.length(); y++){
                                           JSONObject elemento3 = RespuestaExcepcionCategoria.getJSONObject(y);
                                           category_id = elemento3.getString("category_id");
                                           category_name = elemento3.getString("category_name");


                                         CategoriaExcepcionModel cat = new CategoriaExcepcionModel(category_id, category_name,idpublicacion );
                                         arrayList2.add(cat);
                                     }
                                   }
                                  // return;
                                }

                                PublicacionModel pub = new PublicacionModel(idpublicacion, publicacion,arrayList2);
                                arrayList.add(pub);

                           }
                        }//ListViewTipoPublicacion.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,TipoPublicacionName));
                        PublicacionAdapter adapter = new PublicacionAdapter(getContext(), R.layout.caja_publicaciones,arrayList,ListaPublicacion,fr,bundle);
                        ListaPublicacion.setAdapter(adapter);
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
//---------------------------------------------------------------------------------------------------

}
