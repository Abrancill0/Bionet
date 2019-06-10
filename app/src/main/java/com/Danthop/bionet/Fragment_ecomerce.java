package com.Danthop.bionet;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.Adapters.OrdenEcommerceAdapter;
import com.Danthop.bionet.Tables.SortableOrdenEcommerceTable;
import com.Danthop.bionet.model.Ecommerce_orden_Model;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.listeners.SwipeToRefreshListener;
import de.codecrafters.tableview.listeners.TableDataClickListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_ecomerce extends Fragment {
    private SortableOrdenEcommerceTable tabla_ecomerce;
    private View v;
    private String usu_id;
    private Button btn_pestania_sincronizar;
    private Button btn_pestania_preguntas;
    private Button btn_sincronizar;
    private Dialog dialog;
    private static final int REQUEST_CODE = 999;
    private String UserML;
    private String AccesToken;
    private String TokenLife;
    private JSONObject RespuestaTodo = null;
    private String RespuestaTodoString;
    private OrdenEcommerceAdapter ordenAdapter;
    private SearchView Buscar;

    private List<Ecommerce_orden_Model> Ordenes;
    private TableDataClickListener<Ecommerce_orden_Model> tablaListener;

    ProgressDialog progreso;

    private String[][] OrdenesModel;

    public Fragment_ecomerce() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate( R.layout.fragment_ecomerce, container, false );

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences( "DatosPersistentes", getActivity().MODE_PRIVATE );
        Ordenes = new ArrayList<>();

        UserML = sharedPref.getString( "UserIdML", "" );
        AccesToken = sharedPref.getString( "AccessToken", "" );
        TokenLife = sharedPref.getString( "TokenLifetime", "" );
        usu_id = sharedPref.getString( "usu_id", "" );

        progreso=new ProgressDialog(getContext());
        progreso.setMessage("Espere un momento por favor");
        progreso.setCanceledOnTouchOutside(false);
        progreso.show();

        Bundle bundle = getArguments();

        if (bundle != null) {
            String json = bundle.getString( "Resultado" );

            RespuestaTodoString = bundle.getString( "Resultado" );

            try {
                JSONObject obj = new JSONObject( json );
                CargaDatos( obj );

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            LoadTable();
        }


        LoadButtons();
        LoadListenerTable();

        tabla_ecomerce.setSwipeToRefreshEnabled( true );
        tabla_ecomerce.setSwipeToRefreshListener( new SwipeToRefreshListener() {
            @Override
            public void onRefresh(final RefreshIndicator refreshIndicator) {
                tabla_ecomerce.postDelayed( new Runnable() {
                    @Override
                    public void run() {
                        Ordenes.clear();
                        LoadTable();
                        refreshIndicator.hide();
                    }
                }, 2000 );
            }
        } );

        tabla_ecomerce.setEmptyDataIndicatorView( v.findViewById( R.id.Tabla_vacia ) );
        tabla_ecomerce.addDataClickListener(tablaListener);

        Buscar = v.findViewById(R.id.BuscarOrden);

        Buscar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ordenAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return v;
    }

    private void LoadListenerTable(){
        tablaListener = new TableDataClickListener<Ecommerce_orden_Model>() {
            @Override
            public void onDataClicked(int rowIndex, final Ecommerce_orden_Model clickedData) {
                final Dialog pop_up1 = new Dialog( getContext() );
                pop_up1.setContentView( R.layout.pop_up_ecommerce_detalle_orden );
                pop_up1.show();

                TextView Orden = pop_up1.findViewById( R.id.text_ordenname );
                TextView Cliente = pop_up1.findViewById( R.id.text_clientename );
                TextView TipoPago = pop_up1.findViewById( R.id.text_tipo_pago );
                TextView Envio = pop_up1.findViewById( R.id.text_costo_envio );
                TextView Importe = pop_up1.findViewById( R.id.text_importe );
                TextView OrdenEstado = pop_up1.findViewById( R.id.text_estado );
                TextView Fecha = pop_up1.findViewById( R.id.text_fecha );

                double Envioformat = Double.parseDouble( clickedData.getEnvio() );
                NumberFormat formatter = NumberFormat.getCurrencyInstance();

                double importeformat = Double.parseDouble( clickedData.getImporte() );
                NumberFormat formatter2 = NumberFormat.getCurrencyInstance();

                Orden.setText( clickedData.getArticulo() );
                Cliente.setText( clickedData.getCliente() );
                TipoPago.setText( clickedData.getTipoPago() );
                Envio.setText( formatter.format( Envioformat ) );
                Importe.setText( formatter2.format( importeformat ) );
                OrdenEstado.setText( clickedData.getEstatus() );
                Fecha.setText( clickedData.getFecha() );

                Button BtnCerrar = pop_up1.findViewById( R.id.btnordnecerrar );

                BtnCerrar.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pop_up1.hide();
                    }
                } );

                Button pago = pop_up1.findViewById(R.id.pago);
                pago.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog pop_up2 = new Dialog( getContext() );
                        pop_up2.setContentView( R.layout.pop_up_ecommerce_ver_pago );
                        pop_up2.show();

                        TextView Cliente = pop_up2.findViewById( R.id.text_clientename );
                        TextView TipoPago = pop_up2.findViewById( R.id.text_tipo_pago );
                        TextView Importe = pop_up2.findViewById( R.id.text_importe );
                        TextView Envio = pop_up2.findViewById( R.id.text_costo_envio );

                        double Envioformat = Double.parseDouble( clickedData.getEnvio() );
                        NumberFormat formatter = NumberFormat.getCurrencyInstance();

                        double importeformat = Double.parseDouble( clickedData.getImporte() );
                        NumberFormat formatter2 = NumberFormat.getCurrencyInstance();

                        Cliente.setText( clickedData.getCliente() );
                        TipoPago.setText( clickedData.getTipoPago() );
                        Envio.setText( formatter.format( Envioformat ) );
                        Importe.setText( formatter2.format( importeformat ) );

                        Button close = pop_up2.findViewById(R.id.btnordnecerrar);
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pop_up2.hide();
                            }
                        });


                    }
                });
                Button Guia = pop_up1.findViewById(R.id.guia);
                Guia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog pop_up2 = new Dialog( getContext() );
                        pop_up2.setContentView( R.layout.pop_up_ecommerce_guia );
                        pop_up2.show();

                        String Envio = clickedData.getIDshipping();
                        String Token = clickedData.gettoken();

                        TextView Nombre = pop_up2.findViewById( R.id.text_guia_nombre );
                        TextView Nickname = pop_up2.findViewById( R.id.text_guia_nickname );
                        TextView Cliente = pop_up2.findViewById( R.id.text_guia_cliente );
                        Button BtnCerrarGuia = pop_up2.findViewById( R.id.btguianordnecerrar );

                        final TextView Celular = pop_up2.findViewById( R.id.text_guia_celular );
                        final TextView Direccion = pop_up2.findViewById( R.id.text_guia_direccion );
                        final TextView Fecha = pop_up2.findViewById( R.id.text_guia_fecha_creacion );

                        TextView TipoPago = pop_up2.findViewById( R.id.text_guia_tipo_pago );
                        TextView EstatoOrden = pop_up2.findViewById( R.id.text_guia_estado_orden );

                        final TextView CostoEnvio = pop_up2.findViewById( R.id.text_guia_costo_envio );

                        TextView Importe = pop_up2.findViewById( R.id.text_guia_importe );

                        final TextView NumeroGuia = pop_up2.findViewById( R.id.text_guia_numeroguia );
                        final TextView Servicio = pop_up2.findViewById( R.id.text_guia_servicio );

                        Nombre.setText( clickedData.getVendedor() );
                        Nickname.setText( clickedData.getNickname() );
                        Cliente.setText( clickedData.getCliente() );
                        Importe.setText( clickedData.getImporte() );
                        EstatoOrden.setText( clickedData.getEstatus() );
                        TipoPago.setText( clickedData.getTipoPago() );

                        try {
                            final String url = "https://api.mercadolibre.com/shipments/" + Envio + "?access_token=" + Token;

                            // prepare the Request
                            JsonObjectRequest getRequest = new JsonObjectRequest( Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {

                                            JSONObject ResultadoDireccion = null;
                                            JSONObject ResultadoEnvio = null;

                                            try {
                                                ResultadoDireccion = response.getJSONObject( "receiver_address" );
                                                ResultadoEnvio = response.getJSONObject( "shipping_option" );

                                                Direccion.setText( ResultadoDireccion.getString( "address_line" ) + " " + ResultadoDireccion.getString( "comment" ) + " " + ResultadoDireccion.getString( "zip_code" ) );

                                                CostoEnvio.setText( ResultadoEnvio.getString( "cost" ) );

                                                Celular.setText( ResultadoDireccion.getString( "receiver_phone" ) );

                                                Fecha.setText( response.getString( "date_created" ) );
                                                NumeroGuia.setText( response.getString( "tracking_number" ) );
                                                Servicio.setText( response.getString( "tracking_method" ) );


                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                            Toast toast1 =
                                                    Toast.makeText( getContext(),
                                                            error.toString(), Toast.LENGTH_LONG );

                                            toast1.show();
                                        }
                                    }
                            );

                            VolleySingleton.getInstanciaVolley( getContext() ).addToRequestQueue( getRequest );


                        } catch (Error e) {
                            Toast toast1 =
                                    Toast.makeText( getContext(),
                                            e.toString(), Toast.LENGTH_LONG );
                            toast1.show();
                        }


                        BtnCerrarGuia.setOnClickListener( new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                pop_up2.hide();
                            }
                        } );
                    }
                });
            }
        };
    }

    public void CargaDatos(JSONObject Datos) {
        try {

            tabla_ecomerce = (SortableOrdenEcommerceTable) v.findViewById( R.id.tabla_ecommerce );

            JSONObject RespuestaHistorial = null;
            JSONArray RespuestaHistorialResult = null;
            JSONObject RespuestaBuyer = null;
            JSONObject RespuestaSeller = null;
            JSONObject RespuestaShipping = null;
            JSONArray RespuestaShippingItems = null;
            JSONArray RespuestaPayments = null;

            String Estatus;
            String Comprador;
            String Vendedor;
            String Nickname;
            String Descripcion = "";
            String Cantidad = "";
            String Importe = "";
            String Envio = "";
            String TipoPago="";
            String Fecha ="";
            String IDshipping="";

            try {


                int EstatusApi = Integer.parseInt( Datos.getString( "estatus" ) );

                RespuestaHistorial = Datos.getJSONObject( "aHistorial" );

                progreso.dismiss();

                RespuestaTodo = Datos;

                RespuestaHistorialResult = RespuestaHistorial.getJSONArray( "results" );

                OrdenesModel = new String[RespuestaHistorialResult.length()][6];

                for (int x = 0; x < RespuestaHistorialResult.length(); x++) {

                    JSONObject elemento = RespuestaHistorialResult.getJSONObject( x );

                    Estatus = elemento.getString( "status" );
                    RespuestaBuyer = elemento.getJSONObject( "buyer" );
                    RespuestaSeller = elemento.getJSONObject( "seller" );

                    Vendedor =  RespuestaSeller.getString( "first_name" ) + " " + RespuestaSeller.getString( "last_name" );
                    Nickname = RespuestaSeller.getString( "nickname" );

                    Comprador = RespuestaBuyer.getString( "first_name" ) + " " + RespuestaBuyer.getString( "last_name" );
                    RespuestaShipping = elemento.getJSONObject( "shipping" );
                    RespuestaShippingItems = RespuestaShipping.getJSONArray( "shipping_items" );
                    IDshipping = RespuestaShipping.getString( "id");

                    for (int i = 0; i < RespuestaShippingItems.length(); i++) {

                        JSONObject elementoSI = RespuestaShippingItems.getJSONObject( i );

                        Descripcion = elementoSI.getString( "description" );
                        Cantidad = elementoSI.getString( "quantity" );
                    }

                    RespuestaPayments = elemento.getJSONArray( "payments" );

                    for (int z = 0; z < RespuestaPayments.length(); z++) {

                        JSONObject elementoP = RespuestaPayments.getJSONObject( z );

                        Importe = elementoP.getString( "transaction_amount" );
                        Envio = elementoP.getString( "shipping_cost" );

                        Fecha = elementoP.getString( "date_created");
                        TipoPago = elementoP.getString( "payment_type");
                    }

                    final Ecommerce_orden_Model orden = new Ecommerce_orden_Model( Comprador, Descripcion, Cantidad, Envio, Importe, Estatus,TipoPago,Fecha,IDshipping,Vendedor,Nickname,AccesToken );
                    Ordenes.add( orden );
                }
                final OrdenEcommerceAdapter ordenAdapter = new OrdenEcommerceAdapter( getContext(), Ordenes, tabla_ecomerce );
                tabla_ecomerce.setDataAdapter( ordenAdapter );


                progreso.hide();


            } catch (JSONException e) {

                Toast toast1 =
                        Toast.makeText( getContext(),
                                e.toString(), Toast.LENGTH_LONG );

                toast1.show();

                progreso.hide();

                try {
                    JSONObject jsonObj = new JSONObject("{\"Estatus\":\"0\"}");

                    RespuestaTodo = jsonObj;
                } catch (JSONException ea) {
                    e.printStackTrace();
                }
            }

        } catch (Error e) {

            Toast toast1 =
                    Toast.makeText( getContext(),
                            e.toString(), Toast.LENGTH_LONG );

            toast1.show();

            progreso.hide();

            try {
                JSONObject jsonObj = new JSONObject("{\"Estatus\":\"0\"}");

                RespuestaTodo = jsonObj;
            } catch (JSONException ee) {
                e.printStackTrace();
            }


        }

    }

    public void LoadTable() {

        try {

            tabla_ecomerce = (SortableOrdenEcommerceTable) v.findViewById( R.id.tabla_ecommerce );

            String url = getString( R.string.Url );

            final String ApiPath = url + "/api/ecommerce/inicio_app/?accesstoken=" + AccesToken + "&user_id_mercado_libre=" + UserML + "&usu_id=" + usu_id + "&esApp=1";

            // prepare the Request
            JsonObjectRequest getRequest = new JsonObjectRequest( Request.Method.GET, ApiPath, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // display response
                            JSONObject RespuestaHistorial = null;
                            JSONArray RespuestaHistorialResult = null;
                            JSONObject RespuestaBuyer = null;
                            JSONObject RespuestaSeller = null;
                            JSONObject RespuestaShipping = null;
                            JSONArray RespuestaShippingItems = null;
                            JSONArray RespuestaPayments = null;

                            String Estatus;
                            String Comprador;
                            String Descripcion = "";
                            String Cantidad = "";
                            String Importe = "";
                            String Envio = "";
                            String TipoPago="";
                            String Fecha ="";
                            String IDshipping;
                            String Vendedor="";
                            String Nickname="";

                            try {

                                int EstatusApi = Integer.parseInt( response.getString( "estatus" ) );

                                if (EstatusApi == 1) {

                                    progreso.dismiss();

                                    RespuestaHistorial = response.getJSONObject( "aHistorial" );

                                    RespuestaTodo = response;

                                    RespuestaHistorialResult = RespuestaHistorial.getJSONArray( "results" );

                                    OrdenesModel = new String[RespuestaHistorialResult.length()][6];

                                    for (int x = 0; x < RespuestaHistorialResult.length(); x++) {

                                        JSONObject elemento = RespuestaHistorialResult.getJSONObject( x );

                                        Estatus = elemento.getString( "status" );
                                        RespuestaBuyer = elemento.getJSONObject( "buyer" );

                                        RespuestaSeller = elemento.getJSONObject( "seller" );

                                        Vendedor =  RespuestaSeller.getString( "first_name" ) + " " + RespuestaSeller.getString( "last_name" );
                                        Nickname = RespuestaSeller.getString( "nickname" );

                                        Comprador = RespuestaBuyer.getString( "first_name" ) + " " + RespuestaBuyer.getString( "last_name" );
                                        RespuestaShipping = elemento.getJSONObject( "shipping" );
                                        RespuestaShippingItems = RespuestaShipping.getJSONArray( "shipping_items" );

                                        IDshipping = RespuestaShipping.getString( "id");

                                        for (int i = 0; i < RespuestaShippingItems.length(); i++) {

                                            JSONObject elementoSI = RespuestaShippingItems.getJSONObject( i );

                                            Descripcion = elementoSI.getString( "description" );
                                            Cantidad = elementoSI.getString( "quantity" );
                                        }

                                        RespuestaPayments = elemento.getJSONArray( "payments" );

                                        for (int z = 0; z < RespuestaPayments.length(); z++) {

                                            JSONObject elementoP = RespuestaPayments.getJSONObject( z );

                                            Importe = elementoP.getString( "transaction_amount" );
                                            Envio = elementoP.getString( "shipping_cost" );

                                            Fecha = elementoP.getString( "date_created");
                                            TipoPago = elementoP.getString( "payment_type");

                                        }

                                        final Ecommerce_orden_Model orden = new Ecommerce_orden_Model( Comprador, Descripcion, Cantidad, Envio, Importe, Estatus,TipoPago,Fecha,IDshipping,Vendedor,Nickname,AccesToken );
                                        Ordenes.add( orden );
                                    }
                                    ordenAdapter = new OrdenEcommerceAdapter( getContext(), Ordenes, tabla_ecomerce );
                                    tabla_ecomerce.setDataAdapter( ordenAdapter );


                                    progreso.hide();

                                } else {

                                    String Mensaje = response.getString( "resultado" );
                                    Toast toast1 =
                                            Toast.makeText( getContext(),
                                                    Mensaje, Toast.LENGTH_LONG );

                                    toast1.show();

                                    progreso.hide();

                                }


                            } catch (JSONException e) {

                                Toast toast1 =
                                        Toast.makeText( getContext(),
                                                e.toString(), Toast.LENGTH_LONG );

                                toast1.show();

                                progreso.hide();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Log.d("Error.Response", String.valueOf(error));

                            Toast toast1 =
                                    Toast.makeText( getContext(),
                                            error.toString(), Toast.LENGTH_LONG );

                            toast1.show();

                            progreso.hide();

                            try {
                                JSONObject jsonObj = new JSONObject("{\"Estatus\":\"0\"}");

                                RespuestaTodo = jsonObj;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
            );

            getRequest.setShouldCache(false);

            VolleySingleton.getInstanciaVolley( getContext() ).addToRequestQueue( getRequest );


        } catch (Error e) {

            Toast toast1 =
                    Toast.makeText( getContext(),
                            e.toString(), Toast.LENGTH_LONG );

            toast1.show();

            progreso.hide();

        }


    }

    public void LoadButtons() {
        dialog = new Dialog( getContext() );
        btn_pestania_sincronizar = v.findViewById( R.id.btn_pestania_sincronizar );
        btn_pestania_sincronizar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString( "Resultado", String.valueOf( RespuestaTodo ) );

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                Fragment_ecommerce_Sincronizar secondFragment = new Fragment_ecommerce_Sincronizar();
                secondFragment.setArguments( bundle );

                fragmentTransaction.replace( R.id.fragment_container, secondFragment );
                fragmentTransaction.commit();

            }
        } );
        btn_pestania_preguntas = v.findViewById( R.id.btn_pestania_preguntas );
        btn_pestania_preguntas.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString( "Resultado", String.valueOf( RespuestaTodo ) );

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                Fragment_ecommerce_preguntas secondFragment = new Fragment_ecommerce_preguntas();
                secondFragment.setArguments( bundle );

                fragmentTransaction.replace( R.id.fragment_container, secondFragment );
                fragmentTransaction.commit();

            }
        } );
        //btn_sincronizar = v.findViewById(R.id.btn_sincronizar);
        // btn_sincronizar.setOnClickListener(new View.OnClickListener() {
        //     @Override
        //   public void onClick(View v) {
        //       dialog.setContentView(R.layout.pop_up_sincronizar);
        //       dialog.show();
        //       btn_sincronizar_no = dialog.findViewById(R.id.btn_sincronizar_no);
        //       btn_sincronizar_si = dialog.findViewById(R.id.btn_sincronizar_si);


        //       btn_sincronizar_no.setOnClickListener(new View.OnClickListener() {
        //           @Override
        //           public void onClick(View v) {
        //               dialog.dismiss();
        //           }
        //       });
        //       btn_sincronizar_si.setOnClickListener(new View.OnClickListener() {
        //           @Override
        //           public void onClick(View v) {
        //               dialog.setContentView(R.layout.pop_up_confimacion_sincronizar);
        //               btn_aceptar_cerrar_ventana = dialog.findViewById(R.id.aceptar_cerrar_ventana1);

        //               btn_aceptar_cerrar_ventana.setOnClickListener(new View.OnClickListener() {
        //                   @Override
        //                   public void onClick(View v) {
        //                       dialog.dismiss();
        //                   }
        //               });
        //          }
        //       });


        //  }
        //});
    }

}
