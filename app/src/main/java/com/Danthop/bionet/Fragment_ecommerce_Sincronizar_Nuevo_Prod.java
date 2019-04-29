package com.Danthop.bionet;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
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
import android.widget.ImageView;
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
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
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
    private String Imagen1;
    private String Imagen2;
    private String remaining_listings;
    private String idpublicacion;
    private String publicacion;
    private RadioButton RadioUsado;
    private RadioButton RadioNuevo;
    private ImageView FotoArticulo1;
    private ImageView FotoArticulo2;
    private Button Guardar_articulo;
    private TextView Categoria;
    private TextView  Publicaciones;
    private String IDTipoPublicacion;

    private Spinner SpinnerTipoPublicacion;
    private List<CategoriaModel> categorias;
    private ListView listacategoria1;
    private Dialog pop_up_categoria1;
    private List<ArticuloModel> Articulos;
    private String[][] ArticuloModel;
    private String id_categoria;
    ProgressDialog progreso;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private ImageLoader imageLoader2 = ImageLoader.getInstance();
    private String Remaining_listings;

    private String Sucursal;
    private String Sucursal_UUID;
    private String Exi_ID;

    public Fragment_ecommerce_Sincronizar_Nuevo_Prod() {
        // Required empty public constructor
    }

    @Override
    public void sendInput(String nameCategoria) {
        System.out.println(nameCategoria);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.pop_up_ecommerce_nuevo_producto,container, false);

        Bundle bundle = getArguments();

//datos alta de articulos en mercado libre
        String nombre = bundle.getString( "nombre");
        String descripcion = bundle.getString( "descripcion");
        String precio = bundle.getString( "precio");
        String nombre_categoria = bundle.getString("categoria");
        String TipoPublicacionName = bundle.getString("name");
        String Cantidadinventario = bundle.getString("cantidad");
        Sucursal = bundle.getString("Sucursal");
        Sucursal_UUID = bundle.getString("Sucursal_UUID");
        Exi_ID = bundle.getString("Exi_ID");

        IDTipoPublicacion = bundle.getString("id");
        id_categoria = bundle.getString("id_categoria");

        Remaining_listings = bundle.getString("Remaining_listings");

        Imagen1 = "http://187.189.192.150:8010" + bundle.getString( "image1");
        Imagen2 = "http://187.189.192.150:8010" + bundle.getString( "image2");

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences( "DatosPersistentes", getActivity().MODE_PRIVATE );
        UserML = sharedPref.getString( "UserIdML", "" );
        AccesToken = sharedPref.getString( "AccessToken", "" );
        TokenLife = sharedPref.getString( "TokenLifetime", "" );
        usu_id = sharedPref.getString("usu_id","");

        FotoArticulo1 = (ImageView) v.findViewById(R.id.foto_articulo1);
        FotoArticulo2 = (ImageView) v.findViewById(R.id.foto_articulo2);
        imageLoader.displayImage(Imagen1,FotoArticulo1);
        imageLoader2.displayImage(Imagen2,FotoArticulo2);

        TextNombreArticulo = (EditText) v.findViewById( R.id.text_nombre_articulo );
        TextDescripcionArticulo = (EditText) v.findViewById( R.id.text_descripcion_articulo );
        TextprecioArticulo = (EditText) v.findViewById( R.id.text_precio_articulo );
        TextCantidad = (ElegantNumberButton) v.findViewById( R.id.text_cantidad );
        TextGarantia = (EditText) v.findViewById( R.id.text_garantia );

        if (Integer.parseInt(Cantidadinventario) > Integer.parseInt(Remaining_listings))
        {
            TextCantidad.setNumber(String.valueOf(Remaining_listings) );

            TextCantidad.setRange( 1, Integer.valueOf( Remaining_listings ) );
        }
        else
        {
            TextCantidad.setNumber(String.valueOf(Cantidadinventario) );
            TextCantidad.setRange( 1, Integer.valueOf( Cantidadinventario ) );
        }




        textCategoriaSeleccionada = (TextView) v.findViewById( R.id.textCategoriaSeleccionada );
        TextNombreArticulo.setText(nombre);
        TextDescripcionArticulo.setText(descripcion);
        TextprecioArticulo.setText(precio);
        RadioUsado = (RadioButton) v.findViewById( R.id.radioButton_Usado );
        RadioNuevo = (RadioButton) v.findViewById( R.id.radioButton_Nuevo );
        //SpinnerTipoPublicacion=(Spinner) v.findViewById( R.id.Spinner_Tipo_Publicacion );

        Publicaciones= (TextView) v.findViewById( R.id.TextPublicaciones );
        Publicaciones.setText(TipoPublicacionName);

        Guardar_articulo = (Button) v.findViewById( R.id.Guardar_articulo );
        Categoria = (TextView) v.findViewById( R.id.Categoria );
        Categoria.setText(nombre_categoria);
        RadioUsado.setChecked(true);

        //SpinnerCargaPublicaciones();

        Guardar_articulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Guarda();
                CargaCategorias();
            }
        });
            return v;
    }

//---------------------------------------------------------------------------------------------------

//---------------------------------------------------------------------------------------------------

    private void CargaCategorias(){
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

                                    CategoriaModel cat = new CategoriaModel(idcategoria, categoria );
                                    arrayList.add(cat);
                                }
                                //CategoriaAdapter adapter = new CategoriaAdapter(getContext(), R.layout.caja_categoria,arrayList,pop_up_categoria1 );
                                //listacategoria1.setAdapter(adapter);//sets the adapter for listView
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
//---------------------------------------------------------------------------------------------------

    public void Guarda() {

        progreso = new ProgressDialog( getContext() );
        progreso.setMessage( "Guardando..." );
        progreso.show();

        String ApiPath = "https://api.mercadolibre.com/items?access_token=" + AccesToken;

        String Condiciones="";

        if(TextDescripcionArticulo.getText().length()==0) {
            Toast.makeText(getContext(), "Campo descripcion obligatorio", Toast.LENGTH_LONG).show();
            progreso.hide();
            return;
        }

        if (RadioNuevo.isChecked()==true) {
            Condiciones = "new";
        }
        else if (RadioUsado.isChecked()==true) {
            Condiciones = "used";
        }

        JSONObject json1= new JSONObject();
        try {
            json1.put("source",Imagen1);
            json1.put("source",Imagen2);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(json1);

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//
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
            jsonBodyObj.put("listing_type_id", IDTipoPublicacion);
            jsonBodyObj.put("description", String.valueOf(TextDescripcionArticulo.getText()));
            jsonBodyObj.put("pictures",  jsonArray);

        }catch (JSONException e){
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>(){
            @Override    public void onResponse(JSONObject response) {

                ApartaInventario();

            Toast.makeText(getContext(), "Se Agrego correctamente el producto", Toast.LENGTH_LONG).show();
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_ecommerce_Sincronizar()).commit();


                //api/inventario/apartar_mercado_libre


                progreso.hide();
            }
        }, new Response.ErrorListener() {
            @Override    public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),  error.toString(), Toast.LENGTH_LONG).show();
                progreso.hide();
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

    private void ApartaInventario(){
        JSONObject request = new JSONObject();
        try
        {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");

            request.put("exi_id", Exi_ID);
            request.put("suc_id", Sucursal_UUID);
            request.put("mar_cantidad", TextCantidad.getNumber());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/inventario/apartar_mercado_libre";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1)
                    {
                        System.out.println(Mensaje);
                    }
                    else
                    {
                        System.out.println(Mensaje);

                    }

                } catch (JSONException e) {
                    System.out.println(e);
                }

            }

        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        System.out.println(error);

                    }
                }
        );

        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);

    }
//---------------------------------------------------------------------------------------------------
}

