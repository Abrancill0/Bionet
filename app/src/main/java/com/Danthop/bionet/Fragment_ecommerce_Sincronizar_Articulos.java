package com.Danthop.bionet;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.Danthop.bionet.Adapters.ArticuloAdapter;
import com.Danthop.bionet.Adapters.CategoriaAdapter;
import com.Danthop.bionet.Adapters.SucursalAdapter;
import com.Danthop.bionet.Numero_sucursal;
import com.Danthop.bionet.R;
import com.Danthop.bionet.Tables.SortableArticulosTable;
import com.Danthop.bionet.Tables.SortableSucursalTable;
import com.Danthop.bionet.model.ArticuloModel;
import com.Danthop.bionet.model.CategoriaModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Fragment_ecommerce_Sincronizar_Articulos extends Fragment {


    private View v;
    private ArrayList<String> TipoPublicacionName;
    private ArrayList<String> TipoPublicacionID;
    private Spinner SpinnerTipoPublicacion;
    private String usu_id;

    private List<com.Danthop.bionet.model.ArticuloModel> Articulos;
    private String[][] ArticuloModel;

    SortableArticulosTable tb;

    public Fragment_ecommerce_Sincronizar_Articulos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.pop_up_seleccionarticulos,container, false);

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences( "DatosPersistentes", getActivity().MODE_PRIVATE );

        usu_id = sharedPref.getString("usu_id","");

        tb = v.findViewById(R.id.tablaArticulos);

        CargaArticulos();

        return v;

    }

    private void CargaArticulos(){

        try {

            String url = getString(R.string.Url);

            String ApiPath = url + "/api/articulos/index_app/?usu_id=" + usu_id + "&esApp=1";

            // prepare the Request
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ApiPath, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONArray RespuestaResultado = null;
                            JSONObject RespuestaUUID = null;
                            JSONObject RespuestaPrecio=null;
                            JSONObject RespuestaPrecioModificador=null;
                            JSONArray RespuestaModificadores = null;

                            try {

                                int EstatusApi = Integer.parseInt(response.getString("estatus"));

                                Articulos = new ArrayList<>();

                                if (EstatusApi == 1) {

                                    RespuestaResultado = response.getJSONArray("resultado");

                                    // ArticuloModel = new String[RespuestaResultado.length()][5];

                                    for (int x = 0; x < RespuestaResultado.length(); x++) {
                                        JSONObject elemento = RespuestaResultado.getJSONObject(x);

                                        RespuestaUUID = elemento.getJSONObject("art_id");

                                        String UUID = RespuestaUUID.getString( "uuid");

                                        RespuestaPrecio =  elemento.getJSONObject( "ava_precio");
                                        String Precio = RespuestaPrecio.getString( "value");

                                        String NombreCompleto="";
                                        String NombreArticulo = elemento.getString("art_nombre");
                                        String NombreVariante = elemento.getString( "ava_nombre");
                                        String Descripcion = elemento.getString( "art_descripcion");
                                        String NombreModificador="";

                                        String Modificadores = elemento.getString( "ava_tiene_modificadores");

                                        if (Modificadores == "true"){
                                            RespuestaModificadores = elemento.getJSONArray( "modificadores");

                                            for (int i = 0; i < RespuestaModificadores.length(); i++) {

                                                JSONObject elemento2 = RespuestaModificadores.getJSONObject(x);
                                                NombreModificador = elemento2.getString( "mod_nombre");
                                                RespuestaPrecioModificador = elemento2.getJSONObject( "amo_precio");

                                                Precio = RespuestaPrecioModificador.getString( "value");

                                                NombreCompleto = NombreArticulo + " " + NombreVariante + " " + NombreModificador;

                                                final ArticuloModel Articulo = new ArticuloModel(UUID,NombreCompleto, Precio);
                                                Articulos.add(Articulo);
                                            }

                                        }
                                        else
                                        {
                                            NombreCompleto = NombreArticulo + " " + NombreVariante + " " + NombreModificador;

                                            final ArticuloModel Articulo = new ArticuloModel(UUID,NombreCompleto, Precio);
                                            Articulos.add(Articulo);
                                        }
                                    }

                                    final ArticuloAdapter ArticuloAdapter = new ArticuloAdapter(getContext(), Articulos, tb);
                                    tb.setDataAdapter(ArticuloAdapter);

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


        } catch (Error e) {
            e.printStackTrace();
        }

    }

}

