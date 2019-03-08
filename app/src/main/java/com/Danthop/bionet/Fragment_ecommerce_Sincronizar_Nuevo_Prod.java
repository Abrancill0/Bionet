package com.Danthop.bionet;


import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.Adapters.CategoriaAdapter;
import com.Danthop.bionet.model.ArticuloModel;
import com.Danthop.bionet.model.CategoriaModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fragment_ecommerce_Sincronizar_Nuevo_Prod extends Fragment implements CategoriaAdapter.NameCategoriaSelcted{

    private EditText TextNombreArticulo;
    private EditText TextDescripcionArticulo;
    private EditText TextprecioArticulo;
    private TextView textCategoriaSeleccionada;
    private ElegantNumberButton TextCantidad;
    private EditText TextGarantia;
    private View v;

    private String UserML;
    private String AccesToken;
    private String TokenLife;
    private String usu_id;

    private RadioButton RadioUsado;
    private RadioButton RadioNuevo;

    private Button BtnGuardaArticulo;
    private TextView Categoria;

    private ArrayList<String> TipoPublicacionName;
    private ArrayList<String> TipoPublicacionID;
    private Spinner SpinnerTipoPublicacion;

    private List<CategoriaModel> categorias;

    private ListView listacategoria1;
    private Dialog pop_up_categoria1;

    private List<ArticuloModel> Articulos;
    private String[][] ArticuloModel;

    private String id_categoria;

    public Fragment_ecommerce_Sincronizar_Nuevo_Prod() {
        // Required empty public constructor
    }

    @Override
    public void sendInput(String nameCategoria) {
        System.out.println(nameCategoria);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.pop_up_ecommerce_nuevo_producto,container, false);

        Bundle bundle = getArguments();

        String nombre = bundle.getString( "nombre");
        String descripcion = bundle.getString( "descripcion");
        String precio = bundle.getString( "precio");
        String nombre_categoria = bundle.getString("categoria");
        id_categoria = bundle.getString("id_categoria");

        System.out.println(nombre_categoria);

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

                textCategoriaSeleccionada = (TextView) v.findViewById( R.id.textCategoriaSeleccionada );

                TextNombreArticulo.setText(nombre);
                TextDescripcionArticulo.setText(descripcion);
                TextprecioArticulo.setText(precio);

                RadioUsado = (RadioButton) v.findViewById( R.id.radioButton_Usado );
                RadioNuevo = (RadioButton) v.findViewById( R.id.radioButton_Nuevo );

                SpinnerTipoPublicacion=(Spinner) v.findViewById( R.id.Spinner_Tipo_Publicacion );

                BtnGuardaArticulo = (Button) v.findViewById( R.id.Guardar_articulo );
                Categoria = (TextView) v.findViewById( R.id.Categoria );

                Categoria.setText(nombre_categoria);

                CargaPublicaciones();


        BtnGuardaArticulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Guarda();

            }
        });



            return v;

            }

    public void CargaPublicaciones(){


        final String url = "http://187.189.192.150:8010/api/ecomerce/create_app/access_token=" + AccesToken  + "&expires_in=21600&user_id=" + UserML + "&domains=localhost"+ "&?usu_id=" + usu_id + "&esApp=1";

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

                                //CategoriaAdapter adapter = new CategoriaAdapter(getContext(), R.layout.caja_categoria,arrayList,pop_up_categoria1 );

                                //listacategoria1.setAdapter(adapter);//sets the adapter for listView


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

        String ApiPath = "https://api.mercadolibre.com/items?access_token=" + AccesToken;

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

         TipoPublicacion = TipoPublicacionID.get(SpinnerTipoPublicacion.getSelectedItemPosition());

        JSONObject json1= new JSONObject();

        try {
            json1.put("source","http://upload.wikimedia.org/wikipedia/commons/f/fd/Ray_Ban_Original_Wayfarer.jpg");
            json1.put("source","http://upload.wikimedia.org/wikipedia/commons/f/fd/Ray_Ban_Original_Wayfarer.jpg");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(json1);

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        JSONObject jsonBodyObj = new JSONObject();
        String url = "https://api.mercadolibre.com/items?access_token=" + AccesToken;
        try{
            jsonBodyObj.put("title", String.valueOf(TextNombreArticulo.getText()));
            jsonBodyObj.put("category_id", id_categoria);
            jsonBodyObj.put("price", String.valueOf(TextprecioArticulo.getText()));
            jsonBodyObj.put("currency_id","MXN" );
            jsonBodyObj.put("available_quantity", String.valueOf(TextCantidad.getNumber()));
            jsonBodyObj.put("condition", Condiciones);
            jsonBodyObj.put("buying_mode", "buy_it_now");
            jsonBodyObj.put("warranty", String.valueOf(TextGarantia.getText()));
            jsonBodyObj.put("listing_type_id", TipoPublicacion);
            jsonBodyObj.put("description", String.valueOf(TextDescripcionArticulo.getText()));
            jsonBodyObj.put("pictures",  jsonArray);


        }catch (JSONException e){
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONObject>(){
            @Override    public void onResponse(JSONObject response) {

            Toast.makeText(getContext(), "Se Agrego correctamente el producto", Toast.LENGTH_LONG).show();
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_ecommerce_Sincronizar()).commit();
            }
        }, new Response.ErrorListener() {
            @Override    public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),  error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override    public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override    public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }


        };

        requestQueue.add(jsonObjectRequest);


    }


    }

