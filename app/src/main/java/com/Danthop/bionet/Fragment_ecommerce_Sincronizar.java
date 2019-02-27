package com.Danthop.bionet;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_ecommerce_Sincronizar extends Fragment {
    private TableView tabla_sincronizar;
    private View v;
    private String usu_id;
    private Button btn_pestania_ordenes;
    private Button btn_pestania_preguntas;
    private ArrayList<String> Categoria;
    private ArrayList<String> Articulo;
    private ArrayList<String> Variante;
    private Spinner SpinnerCategoria;
    private Spinner SpinnerArticulo;
    private Spinner SpinnerVariante;
    private String UserML;
    private String AccesToken;
    private String TokenLife;
    private String[][] SincornizarModel;
    private Dialog crear_Producto_dialog;
    private Button btn_alta_articulo;

    private EditText TextNombreArticulo;
    private EditText TextDescripcionArticulo;
    private EditText TextprecioArticulo;
    private EditText TextCantidad;
    private EditText TextGarantia;

    private Spinner SpinnerCategoriaArticulo;
    private Spinner SpinnerTipoPublicacion;

    private RadioButton RadioUsado;
    private RadioButton RadioNuevo;

    private Button BtnGuardaArticulo;

    public Fragment_ecommerce_Sincronizar() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_ecommerce_sincronizar,container, false);

        crear_Producto_dialog=new Dialog(getContext());
        crear_Producto_dialog.setContentView(R.layout.fragment_popup_ecommerce_nuevo_producto );

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences( "DatosPersistentes", getActivity().MODE_PRIVATE );

        UserML = sharedPref.getString( "UserIdML", "" );
        AccesToken = sharedPref.getString( "AccessToken", "" );
        TokenLife = sharedPref.getString( "TokenLifetime", "" );
        usu_id = sharedPref.getString("usu_id","");

        btn_alta_articulo = (Button)v.findViewById(R.id.btn_alta_articulo);

        btn_alta_articulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crear_Producto_dialog.show();

                TextNombreArticulo = (EditText) crear_Producto_dialog.findViewById(R.id.text_nombre_articulo);
                TextDescripcionArticulo= (EditText) crear_Producto_dialog.findViewById(R.id.text_descripcion_articulo);
                TextprecioArticulo= (EditText) crear_Producto_dialog.findViewById(R.id.text_precio_articulo);
                TextCantidad = (EditText) crear_Producto_dialog.findViewById(R.id.text_cantidad);
                TextGarantia = (EditText) crear_Producto_dialog.findViewById(R.id.text_garantia);

                SpinnerCategoriaArticulo = (Spinner) crear_Producto_dialog.findViewById(R.id.Spinner_categoria_articulo);
                SpinnerTipoPublicacion = (Spinner) crear_Producto_dialog.findViewById(R.id.Spinner_tipo_publicacion_articulo);

                RadioUsado = (RadioButton) crear_Producto_dialog.findViewById(R.id.radioButton_Usado);
                RadioNuevo = (RadioButton) crear_Producto_dialog.findViewById(R.id.radioButton_Nuevo);

                BtnGuardaArticulo = (Button) crear_Producto_dialog.findViewById(R.id.Guardar_articulo);

                BtnGuardaArticulo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

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

                        // category_id:SubCategoriaArticulo
                        // price:
                        // currency_id: "MXN",
                        // available_quantity: CantidadArticulo,
                        // condition: condition{new,used},
                        // buying_mode: "buy_it_now",
                        // warranty: DescripcionGarantia
                        // listing_type_id: selectTipoPulicacion
                        // description: tDescripcionArticulo
                        // pictures: ImagenArticulo

                        smr.addStringParam("access_token", AccesToken);
                        smr.addStringParam("title", String.valueOf(TextNombreArticulo.getText()));
                        smr.addStringParam("category_id", "");
                        smr.addStringParam("price", String.valueOf(TextprecioArticulo.getText()));
                        smr.addStringParam("currency_id", "MXN");
                        smr.addStringParam("available_quantity", String.valueOf(TextCantidad.getText()));
                        smr.addStringParam("condition", Condiciones);
                        smr.addStringParam("buying_mode", "buy_it_now");
                        smr.addStringParam("warranty", String.valueOf(TextGarantia.getText()));
                        smr.addStringParam("listing_type_id", "lo que sea");
                        smr.addStringParam("description", String.valueOf(TextDescripcionArticulo.getText()));
                        smr.addFile("pictures", "1");

                        RequestQueue mRequestQueue = Volley.newRequestQueue(getContext());
                        mRequestQueue.add(smr);

                    }
                });

            }
        });

        LoadTable();
        LoadButtons();
        LoadSpinners();

        return v;
    }

    public void LoadTable(){

       try {
           tabla_sincronizar = (TableView) v.findViewById(R.id.tabla_sincronizar);
           final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(),  "Artículo", "Disponible", "Envio Gratis", "Precio");
           simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

           final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(4);
           tableColumnWeightModel.setColumnWeight(0, 2);
           tableColumnWeightModel.setColumnWeight(1, 2);
           tableColumnWeightModel.setColumnWeight(2, 2);
           tableColumnWeightModel.setColumnWeight(3, 2);


           tabla_sincronizar.setHeaderAdapter(simpleHeader);
           tabla_sincronizar.setColumnModel(tableColumnWeightModel);

           final String url = "http://187.189.192.150:8010/api/ecomerce/inicio_app/?accesstoken=" + AccesToken  + "&user_id=" + UserML;

           // prepare the Request
           JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                   new Response.Listener<JSONObject>()
                   {
                       @Override
                       public void onResponse(JSONObject response) {
                           // display response
                           JSONObject RespuestaDatos = null;
                           JSONObject RespuestaObjeto = null;
                           JSONObject Respuestaespecificaciones = null;
                           JSONObject Respuestapicture = null;
                           JSONObject Respuestashipping = null;

                           String Titulo;
                           String Disponibilidad;
                           String Precio;
                           String Imagen;
                           String Envio;

                           int x =0;

                           try
                           {

                               int EstatusApi = Integer.parseInt( response.getString("estatus") );

                               if (EstatusApi == 1) {

                                   RespuestaDatos= response.getJSONObject("aDatos");

                                   int numeroregistro =RespuestaDatos.length();

                                   SincornizarModel = new String[numeroregistro][4];

                                   Iterator<String> keys = RespuestaDatos.keys();
                                   while( keys.hasNext() )
                                   {
                                       String key = keys.next();

                                       RespuestaObjeto = RespuestaDatos.getJSONObject(key);

                                       Respuestaespecificaciones = RespuestaObjeto.getJSONObject("especificaciones");

                                       Titulo = Respuestaespecificaciones.getString( "title" );
                                       Disponibilidad = Respuestaespecificaciones.getString( "available_quantity" );
                                       Precio = Respuestaespecificaciones.getString( "price" );

                                       //  Respuestapicture = Respuestaespecificaciones.getJSONObject("pictures");
                                       //  Imagen = Respuestapicture.getString( "url" );

                                       Respuestashipping = Respuestaespecificaciones.getJSONObject( "shipping" );
                                       Envio = Respuestashipping.getString( "free_shipping" );

                                       if (Envio == "true"){
                                           Envio="Si";
                                       }
                                       else
                                       {
                                           Envio="No";
                                       }


                                       SincornizarModel[x][0] = Titulo;
                                       SincornizarModel[x][1] = Disponibilidad;
                                       SincornizarModel[x][2] = Envio;
                                       SincornizarModel[x][3] = Precio;

                                       x += 1 ;

                                   }

                               }

                               tabla_sincronizar.setDataAdapter(new SimpleTableDataAdapter(getContext(), SincornizarModel));


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

       }


    }

    public void LoadButtons(){
        btn_pestania_ordenes = v.findViewById(R.id.btn_pestania_ordenes);
        btn_pestania_ordenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_ecomerce()).commit();
            }
        });
        btn_pestania_preguntas = v.findViewById(R.id.btn_pestania_preguntas);
        btn_pestania_preguntas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_popup_ecommerce_preguntas()).commit();
            }
        });
    }

    public void LoadSpinners(){
        Categoria=new ArrayList<>();
        Articulo=new ArrayList<>();
        Variante=new ArrayList<>();

        SpinnerCategoria = v.findViewById(R.id.Combo_categoria);
        SpinnerArticulo = v.findViewById(R.id.Combo_articulo);
        SpinnerVariante = v.findViewById(R.id.Combo_variante);


        Categoria.add("Categoria 1");
        Categoria.add("Categoria 2");
        Categoria.add("Categoria 3");

        Articulo.add("Artículo 1");
        Articulo.add("Artículo 2");
        Articulo.add("Artículo 3");

        Variante.add("Variante 1");
        Variante.add("Variante 2");
        Variante.add("Variante 3");

        SpinnerCategoria.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,Categoria));
        SpinnerArticulo.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,Articulo));
        SpinnerVariante.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,Variante));

    }

}
