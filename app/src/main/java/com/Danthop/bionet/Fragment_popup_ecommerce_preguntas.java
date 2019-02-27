package com.Danthop.bionet;


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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_popup_ecommerce_preguntas extends Fragment {


    private TableView tabla_preguntas;
    private View v;
    private String usu_id;
    private Button btn_pestania_sincronizar;
    private Button btn_pestania_ordenes;
    private ArrayList<String> Articulo;
    private Spinner SpinnerArticulo;

    private String UserML;
    private String AccesToken;
    private String TokenLife;

    private String[][] PreguntasModel;


    public Fragment_popup_ecommerce_preguntas() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_ecommerce_preguntas,container, false);

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences( "DatosPersistentes", getActivity().MODE_PRIVATE );

        UserML = sharedPref.getString( "UserIdML", "" );
        AccesToken = sharedPref.getString( "AccessToken", "" );
        TokenLife = sharedPref.getString( "TokenLifetime", "" );
        usu_id = sharedPref.getString("usu_id","");

        LoadTable();
        LoadButtons();
        LoadSpinner();

        return v;
    }


    public void LoadTable(){
        tabla_preguntas = (TableView) v.findViewById(R.id.tabla_preguntas);
        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(),  "Pregunta",  "Usuario", "Producto");
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);

        usu_id = sharedPref.getString("usu_id","");

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(5);
        tableColumnWeightModel.setColumnWeight(0, 5);
        tableColumnWeightModel.setColumnWeight(1, 3);
        tableColumnWeightModel.setColumnWeight(2, 3);

        tabla_preguntas.setHeaderAdapter(simpleHeader);
        tabla_preguntas.setColumnModel(tableColumnWeightModel);


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
                        JSONObject Respuestapreguntas = null;
                        JSONArray RespuestaQuestions = null;
                        JSONObject Respuestaespecificaciones =null;
                        JSONObject RespuestaComprador = null;


                        String Titulo;
                        String preguntas;
                        String idcomprador;
                        String comprador="";

                        try
                        {

                            int EstatusApi = Integer.parseInt( response.getString("estatus") );

                            if (EstatusApi == 1) {

                                RespuestaDatos= response.getJSONObject("aDatos");

                                int numeroregistro =RespuestaDatos.length();

                                PreguntasModel = new String[numeroregistro][3];

                                Iterator<String> keys = RespuestaDatos.keys();
                                while( keys.hasNext() )
                                {
                                    String key = keys.next();

                                    RespuestaObjeto = RespuestaDatos.getJSONObject(key);

                                    Respuestapreguntas = RespuestaObjeto.getJSONObject("preguntas");

                                    Respuestaespecificaciones = RespuestaObjeto.getJSONObject("especificaciones");

                                    Titulo = Respuestaespecificaciones.getString( "title" );

                                    RespuestaQuestions = Respuestapreguntas.getJSONArray("questions");

                                    for(int x = 0; x < RespuestaQuestions.length(); x++) {

                                        JSONObject elemento = RespuestaQuestions.getJSONObject(x);

                                        preguntas = elemento.getString("text");

                                        idcomprador = elemento.getJSONObject("from").getString( "id");

                                        RespuestaComprador = response.getJSONObject("aUsuariosQuePregunta");

                                        Iterator<String> keys2 = RespuestaComprador.keys();
                                        while( keys2.hasNext() )
                                        {

                                            String keyidcomp = keys2.next();

                                            String Valor1= String.valueOf(idcomprador);
                                            String Valor2= String.valueOf(keyidcomp);

                                            if(!Valor1.equals(Valor2)) {

                                                RespuestaObjeto = RespuestaComprador.getJSONObject(keyidcomp);

                                                comprador = RespuestaObjeto.getString("nickname");
                                            }

                                        }


                                        PreguntasModel[x][0] = preguntas;
                                        PreguntasModel[x][1] = comprador;
                                        PreguntasModel[x][2] = Titulo;


                                    }


                                }

                            }

                            tabla_preguntas.setDataAdapter(new SimpleTableDataAdapter(getContext(), PreguntasModel));


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

    public void LoadButtons(){
        btn_pestania_ordenes = v.findViewById(R.id.btn_pestania_ordenes);
        btn_pestania_ordenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_ecomerce()).commit();
            }
        });
        btn_pestania_sincronizar = v.findViewById(R.id.btn_pestania_sincronizar);
        btn_pestania_sincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_ecommerce_Sincronizar()).commit();
            }
        });
    }

    public void LoadSpinner(){
        Articulo=new ArrayList<>();

        SpinnerArticulo = v.findViewById(R.id.Combo_articulo);

        Articulo.add("Artículo 1");
        Articulo.add("Artículo 2");
        Articulo.add("Artículo 3");

        SpinnerArticulo.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,Articulo));

    }


}
