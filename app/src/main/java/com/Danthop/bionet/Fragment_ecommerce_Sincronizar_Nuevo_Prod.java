package com.Danthop.bionet;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.Danthop.bionet.Adapters.CategoriaAdapter;
import com.Danthop.bionet.Adapters.SincronizarAdapter;
import com.Danthop.bionet.Tables.SortableSincronizarTable;
import com.Danthop.bionet.model.CategoriaModel;
import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.SincronizarModel;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import de.codecrafters.tableview.listeners.SwipeToRefreshListener;

public class Fragment_ecommerce_Sincronizar_Nuevo_Prod extends Fragment {

    private EditText TextNombreArticulo;
    private EditText TextDescripcionArticulo;
    private EditText TextprecioArticulo;
    private ElegantNumberButton TextCantidad;
    private EditText TextGarantia;
    private View v;

    private String UserML;
    private String AccesToken;
    private String TokenLife;
    private String[][] SincornizarModel;
    private Dialog crear_Producto_dialog;
    private Button btn_alta_articulo;
    private String usu_id;

    private RadioButton RadioUsado;
    private RadioButton RadioNuevo;

    private Button BtnGuardaArticulo;
    private Button Btn_Seleccionar_Categorias;

    private Spinner SpinnerArticulo;
    private Spinner SpinnerVariante;

    private ArrayList<String> TipoPublicacionName;
    private ArrayList<String> TipoPublicacionID;
    private Spinner SpinnerTipoPublicacion;

    private List<CategoriaModel> categorias;

    private ListView listacategoria1;
    private Dialog pop_up_categoria1;

    private List<SincronizarModel> Sincronizaciones;

    public Fragment_ecommerce_Sincronizar_Nuevo_Prod() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.pop_up_ecommerce_nuevo_producto,container, false);

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences( "DatosPersistentes", getActivity().MODE_PRIVATE );

        UserML = sharedPref.getString( "UserIdML", "" );
        AccesToken = sharedPref.getString( "AccessToken", "" );
        TokenLife = sharedPref.getString( "TokenLifetime", "" );
        usu_id = sharedPref.getString("usu_id","");

                TextNombreArticulo = (EditText) v.findViewById( R.id.text_nombre_articulo );
                TextDescripcionArticulo = (EditText) v.findViewById( R.id.text_descripcion_articulo );
                TextprecioArticulo = (EditText) v.findViewById( R.id.text_precio_articulo );
                TextCantidad = (ElegantNumberButton) v.findViewById( R.id.text_cantidad );
                TextGarantia = (EditText) v.findViewById( R.id.text_garantia );

                RadioUsado = (RadioButton) v.findViewById( R.id.radioButton_Usado );
                RadioNuevo = (RadioButton) v.findViewById( R.id.radioButton_Nuevo );

                SpinnerTipoPublicacion=(Spinner) v.findViewById( R.id.Spinner_Tipo_Publicacion );

                BtnGuardaArticulo = (Button) v.findViewById( R.id.Guardar_articulo );
                Btn_Seleccionar_Categorias = (Button) v.findViewById( R.id.Btn_Seleccionar_Categorias );

                CargaPublicaciones();

                Btn_Seleccionar_Categorias.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        pop_up_categoria1 = new Dialog(getContext());
                        pop_up_categoria1.setContentView(R.layout.popupcategoriaone);
                        pop_up_categoria1.show();

                        listacategoria1 = (ListView) pop_up_categoria1.findViewById(R.id.listviewcat1);

                        CargaCategorias();
                    }
                });

            return v;

            }

    public void CargaPublicaciones(){


        final String url = "http://187.189.192.150:8010/api/ecomerce/create_app/access_token=" + AccesToken  + "&expires_in=21600&user_id=" + UserML + "&domains=localhost";

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

                                TipoPublicacionID=new ArrayList<>();
                                TipoPublicacionName = new ArrayList<>();

                                categorias = new ArrayList<CategoriaModel>();

                                RespuestaTiposPublicacion = response.getJSONArray("aListaTiposPublicacion");

                               // RespuestaCategoria = response.getJSONArray("aCategorias");

                                for(int x = 0; x < RespuestaTiposPublicacion.length(); x++){
                                    JSONObject jsonObject1 = RespuestaTiposPublicacion.getJSONObject(x);
                                    String idpublicacion = jsonObject1.getString("id");
                                    String publicacion = jsonObject1.getString("name");

                                    TipoPublicacionID.add(idpublicacion);
                                    TipoPublicacionName.add(publicacion);
                                }


                                SpinnerTipoPublicacion.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,TipoPublicacionName));

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

    public void CargaCategoriaNivel2()
    {

        try
        {
            String Cat1 ="";// CategoriaID1.get(SpinnerCategoriaArticulo.getSelectedItemPosition());

            final String url = "http://187.189.192.150:8010/api/ecomerce/obtenerSubcategoriasMercadoLibre?sIdCategoria=" +  Cat1;

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

                                   // SpinnerCategoriaArticulo2.setVisibility( Integer.parseInt( "1" ) );

                                    RespuestaCategoria = response.getJSONArray("resultado");

                                    for(int x = 0; x < RespuestaCategoria.length(); x++){
                                        JSONObject jsonObject1 = RespuestaCategoria.getJSONObject(x);
                                        String idcategoria = jsonObject1.getString("id");
                                        String categoria = jsonObject1.getString("name");


                                    }

                                   // SpinnerCategoriaArticulo2.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,CategoriaName2));

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
        catch (Error e)
        {   e.printStackTrace();
        }

    }

    public void CargaCategoriaNivel3()
    {

        try
        {

            String Cat2 =""; //CategoriaID2.get(SpinnerCategoriaArticulo.getSelectedItemPosition());

            final String url = "http://187.189.192.150:8010/api/ecomerce/obtenerSubcategoriasMercadoLibre?sIdCategoria=" +  Cat2;

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

                                   // SpinnerCategoriaArticulo3.setVisibility( Integer.parseInt( "1" ) );



                                    RespuestaCategoria = response.getJSONArray("resultado");

                                    for(int x = 0; x < RespuestaCategoria.length(); x++){
                                        JSONObject jsonObject1 = RespuestaCategoria.getJSONObject(x);
                                        String idcategoria = jsonObject1.getString("id");
                                        String categoria = jsonObject1.getString("name");


                                    }

                                    //SpinnerCategoriaArticulo3.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,CategoriaName3));

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
        catch (Error e)
        {
            e.printStackTrace();
        }


    }

    public void CargaCategoriaNivel4()
    {

        try
        {
            String Cat3 =""; //CategoriaID3.get(SpinnerCategoriaArticulo.getSelectedItemPosition());

            final String url = "http://187.189.192.150:8010/api/ecomerce/obtenerSubcategoriasMercadoLibre?sIdCategoria=" +  Cat3;

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



                                    RespuestaCategoria = response.getJSONArray("resultado");

                                    for(int x = 0; x < RespuestaCategoria.length(); x++){
                                        JSONObject jsonObject1 = RespuestaCategoria.getJSONObject(x);
                                        String idcategoria = jsonObject1.getString("id");
                                        String categoria = jsonObject1.getString("name");


                                    }

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
        catch (Error e)
        {   e.printStackTrace();
        }

    }

    public void CargaCategoriaNivel5()
    {
        try
        {
        }
        catch (Error e)
        {   e.printStackTrace();
        }
    }

    public void CargaCategoriaNivel6()
    {
        try
        {
        }
        catch (Error e)
        {   e.printStackTrace();
        }
    }

    private void CargaCategorias()
    {
        final String url = "http://187.189.192.150:8010/api/ecomerce/create_app/access_token=" + AccesToken  + "&expires_in=21600&user_id=" + UserML + "&domains=localhost";

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

                                CategoriaAdapter adapter = new CategoriaAdapter(getContext(), R.layout.caja_categoria,arrayList );

                                listacategoria1.setAdapter(adapter);//sets the adapter for listView
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

    public void Guarda() {

        String ApiPath = "https://api.mercadolibre.com/items";

        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, ApiPath,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jObj = null;
                        try {
                            jObj = new JSONObject(response);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(getContext(), "Se Agrego correctamente el producto", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        String Condiciones="";

        if (RadioNuevo.isChecked()==true)
        {
            Condiciones = "new";
        }
        else if (RadioUsado.isChecked()==true)
        {
            Condiciones = "used";
        }

        String TipoPublicacion;

        // TipoPublicacion = TipoPublicacionID.get(SpinnerTipoPublicacion.getSelectedItemPosition());

        smr.addStringParam("access_token", AccesToken);
        smr.addStringParam("title", String.valueOf(TextNombreArticulo.getText()));
        smr.addStringParam("category_id", "");
        smr.addStringParam("price", String.valueOf(TextprecioArticulo.getText()));
        smr.addStringParam("currency_id", "MXN");
        smr.addStringParam("available_quantity", String.valueOf(TextCantidad.getNumber()));
        smr.addStringParam("condition", Condiciones);
        smr.addStringParam("buying_mode", "buy_it_now");
        smr.addStringParam("warranty", String.valueOf(TextGarantia.getText()));
        smr.addStringParam("listing_type_id", "");
        smr.addStringParam("description", String.valueOf(TextDescripcionArticulo.getText()));
        smr.addFile("pictures", "1");

        RequestQueue mRequestQueue = Volley.newRequestQueue(getContext());
        mRequestQueue.add(smr);

    }

    }

