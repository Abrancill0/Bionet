package com.Danthop.bionet;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.util.List;

public class Fragment_selecciona_tipo_publicacion extends Fragment {
    private String UserML;
    private String AccesToken;
    private String usu_id;
    private String remaining_listings;
    private String idpublicacion;
    private String publicacion;
    FragmentTransaction fr;
    private ArrayList<String> TipoPublicacionName;
    private ArrayList<String> TipoPublicacionID;
    private ListView ListViewTipoPublicacion;
    private TextView TextView;

    public Fragment_selecciona_tipo_publicacion() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_selecciona_tipo_publicacion, container, false);
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences( "DatosPersistentes", getActivity().MODE_PRIVATE );
        fr = getFragmentManager().beginTransaction();
        UserML = sharedPref.getString( "UserIdML", "" );
        AccesToken = sharedPref.getString( "AccessToken", "" );
        usu_id = sharedPref.getString( "usu_id", "" );

        ListViewTipoPublicacion=(ListView) v.findViewById( R.id.listView_selecciona_publicacion );
        TextView = (TextView)v.findViewById(R.id.Textv);
        TipoPublicacionID = new ArrayList<>();
        TipoPublicacionName = new ArrayList<>();

        CargaPublicaciones();
        return v;
    }

    public void CargaPublicaciones(){
        final String url = "http://187.189.192.150:8010/api/ecommerce/create_app?accesstoken=" + AccesToken  + "&user_id_mercado_libre=" + UserML + "&usu_id=" + usu_id + "&esApp=1";

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject RespuestaTiposPublicacion = null;
                JSONArray  Respuestaavailable = null;
                try {
                    int EstatusApi = Integer.parseInt( response.getString("estatus") );
                    if (EstatusApi == 1) {

                        TipoPublicacionID=new ArrayList<>();
                        TipoPublicacionName = new ArrayList<>();
                        RespuestaTiposPublicacion = response.getJSONObject("aListaTiposPublicacion");
                        Respuestaavailable = RespuestaTiposPublicacion.getJSONArray("available");

                        for(int x = 0; x < Respuestaavailable.length(); x++){
                            JSONObject elemento = Respuestaavailable.getJSONObject(x);

                            remaining_listings = elemento.getString("remaining_listings");
                            //int remaining = Integer.parseInt(remaining_listings);

                            if (remaining_listings !=null){
                            idpublicacion = elemento.getString("id");
                            publicacion = elemento.getString("name");

                            TipoPublicacionID.add(idpublicacion);
                            TipoPublicacionName.add(publicacion);
                           }

                        }ListViewTipoPublicacion.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,TipoPublicacionName));
                    }
                }
                catch (JSONException e) {
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
