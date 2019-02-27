package com.Danthop.bionet;


import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_ecomerce extends Fragment {
    private TableView tabla_ecomerce;
    private View v;
    private String usu_id;
    private Button btn_pestania_sincronizar;
    private Button btn_pestania_preguntas;
    private Button btn_sincronizar;
    private Dialog dialog;
    private Button btn_sincronizar_no;
    private Button btn_sincronizar_si;
    private Button btn_aceptar_cerrar_ventana;
    private static final int REQUEST_CODE = 999;
    private String UserML;
    private String AccesToken;
    private String TokenLife;

    private String[][] OrdenesModel;


    public Fragment_ecomerce() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_ecomerce,container, false);

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences( "DatosPersistentes", getActivity().MODE_PRIVATE );

         UserML = sharedPref.getString( "UserIdML", "" );
         AccesToken = sharedPref.getString( "AccessToken", "" );
         TokenLife = sharedPref.getString( "TokenLifetime", "" );
         usu_id = sharedPref.getString("usu_id","");

        LoadTable();
        LoadButtons();

        return v;
    }


    public void LoadTable(){
        tabla_ecomerce = (TableView) v.findViewById(R.id.tabla_ecommerce);
        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(),  "Cliente", "Articulo", "Cantidad", "Envio", "Importe","Estatus");
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));


        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(6);
        tableColumnWeightModel.setColumnWeight(0, 5);
        tableColumnWeightModel.setColumnWeight(1, 3);
        tableColumnWeightModel.setColumnWeight(2, 2);
        tableColumnWeightModel.setColumnWeight(3, 2);
        tableColumnWeightModel.setColumnWeight(4, 2);
        tableColumnWeightModel.setColumnWeight(5, 2);

        tabla_ecomerce.setHeaderAdapter(simpleHeader);
        tabla_ecomerce.setColumnModel(tableColumnWeightModel);


            final String url = "http://187.189.192.150:8010/api/ecomerce/inicio_app/?accesstoken=" + AccesToken  + "&user_id=" + UserML;

            // prepare the Request
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response) {
                            // display response
                            JSONObject RespuestaHistorial = null;
                            JSONArray RespuestaHistorialResult = null;
                            JSONObject RespuestaBuyer = null;
                            JSONObject RespuestaShipping = null;
                            JSONArray RespuestaShippingItems = null;
                            JSONArray RespuestaPayments = null;

                            String Estatus;
                            String Comprador;
                            String Descripcion ="";
                            String Cantidad="";
                            String Importe ="";
                            String Envio="";

                            try {

                                int EstatusApi = Integer.parseInt( response.getString("estatus") );

                                if (EstatusApi == 1) {

                                    RespuestaHistorial = response.getJSONObject("aHistorial");

                                    RespuestaHistorialResult = RespuestaHistorial.getJSONArray("results");

                                    OrdenesModel = new String[RespuestaHistorialResult.length()][6];

                                    for(int x = 0; x < RespuestaHistorialResult.length(); x++){

                                        JSONObject elemento = RespuestaHistorialResult.getJSONObject(x);

                                        Estatus = elemento.getString("status");
                                        RespuestaBuyer = elemento.getJSONObject("buyer");
                                        Comprador = RespuestaBuyer.getString("first_name") + " " + RespuestaBuyer.getString("last_name") ;
                                        RespuestaShipping = elemento.getJSONObject("shipping");
                                        RespuestaShippingItems= RespuestaShipping.getJSONArray("shipping_items");

                                        for(int i = 0; i < RespuestaShippingItems.length(); i++){

                                            JSONObject elementoSI = RespuestaShippingItems.getJSONObject(i);

                                            Descripcion = elementoSI.getString( "description" );
                                            Cantidad = elementoSI.getString( "quantity" );
                                        }

                                        RespuestaPayments = elemento.getJSONArray("payments");

                                        for(int z = 0; z < RespuestaPayments.length(); z++) {

                                            JSONObject elementoP = RespuestaPayments.getJSONObject(z);

                                            Importe = elementoP.getString( "transaction_amount" );
                                            Envio = elementoP.getString( "shipping_cost" );
                                        }

                                        OrdenesModel[x][0] = Comprador;
                                        OrdenesModel[x][1] = Descripcion;
                                        OrdenesModel[x][2] = Cantidad;
                                        OrdenesModel[x][3] = Envio;
                                        OrdenesModel[x][4] = Importe;
                                        OrdenesModel[x][5] = Estatus;
                                    }

                                    tabla_ecomerce.setDataAdapter(new SimpleTableDataAdapter(getContext(), OrdenesModel));


                                }
                                else
                                {

                                    String Mensaje = response.getString("resultado");
                                    Toast toast1 =
                                            Toast.makeText(getContext(),
                                                    Mensaje, Toast.LENGTH_LONG);

                                    toast1.show();

                                }


                            } catch (JSONException e) {

                                Toast toast1 =
                                        Toast.makeText(getContext(),
                                                e.toString(), Toast.LENGTH_LONG);

                                toast1.show();
                            }


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

    public void LoadButtons(){
        dialog=new Dialog(getContext());
        btn_pestania_sincronizar = v.findViewById(R.id.btn_pestania_sincronizar);
        btn_pestania_sincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_ecommerce_Sincronizar()).commit();
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
