package com.Danthop.bionet;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import com.Danthop.bionet.Adapters.PreguntasAdapter;
import com.Danthop.bionet.Adapters.SincronizarAdapter;
import com.Danthop.bionet.Tables.SortablePreguntasTable;
import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.Preguntas_Model;
import com.Danthop.bionet.model.SincronizarModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.request.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.SwipeToRefreshListener;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_ecommerce_preguntas extends Fragment {


    private SortablePreguntasTable tabla_preguntas;
    private View v;
    private String usu_id;
    private Button btn_pestania_sincronizar;
    private Button btn_pestania_ordenes;
    private ArrayList<String> Articulo;
    private Spinner SpinnerArticulo;
    private String UserML;
    private String AccesToken;
    private String TokenLife;
    ProgressDialog progreso;
    private String[][] PreguntasModel;
    private String RespuestaTodo;
    private List<Preguntas_Model> Preguntas;
    private JSONObject RespuestaTodoJSON;
    private TableDataClickListener<Preguntas_Model> tablaListener;
    private Dialog pop_up1;
    private TextView Respuesta;

    public Fragment_ecommerce_preguntas() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate( R.layout.fragment_ecommerce_preguntas, container, false );

        Preguntas = new ArrayList<>();

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences( "DatosPersistentes", getActivity().MODE_PRIVATE );

        UserML = sharedPref.getString( "UserIdML", "" );
        AccesToken = sharedPref.getString( "AccessToken", "" );
        TokenLife = sharedPref.getString( "TokenLifetime", "" );
        usu_id = sharedPref.getString( "usu_id", "" );

        Bundle bundle = getArguments();

        String json = bundle.getString( "Resultado" );
        RespuestaTodo = bundle.getString( "Resultado" );

        try {
            JSONObject obj = new JSONObject( json );
            CargaDatos( obj );

        } catch (JSONException e) {
            e.printStackTrace();
        }

        LoadListenerTable();
        LoadButtons();
        LoadSpinner();

        tabla_preguntas.addDataClickListener(tablaListener);
        tabla_preguntas.setSwipeToRefreshEnabled( true );
        tabla_preguntas.setSwipeToRefreshListener( new SwipeToRefreshListener() {
            @Override
            public void onRefresh(final RefreshIndicator refreshIndicator) {
                tabla_preguntas.postDelayed( new Runnable() {
                    @Override
                    public void run() {
                        Preguntas.clear();
                        LoadTable();
                        refreshIndicator.hide();
                    }
                }, 2000 );
            }
        } );

        tabla_preguntas.setEmptyDataIndicatorView( v.findViewById( R.id.Tabla_vacia ) );

        return v;
    }

    private void LoadListenerTable(){
        tablaListener = new TableDataClickListener<Preguntas_Model>() {
            @Override
            public void onDataClicked(int rowIndex, final Preguntas_Model clickedData) {


                pop_up1=new Dialog(getContext());
                pop_up1.setContentView(R.layout.pop_up_ecommerce_contestar_pregunta );
                pop_up1.show();

                TextView ArticuloPregunta = pop_up1.findViewById(R.id.text_pregunta_articulo);
                TextView Pregunta = pop_up1.findViewById(R.id.text_pregunta);
                Respuesta = pop_up1.findViewById(R.id.text_respuesta);
                Button BntContestar = pop_up1.findViewById(R.id.btcontestar);
                Button Btncerrar = pop_up1.findViewById(R.id.btpreguntacerrar);
                Button BtnEliminar = pop_up1.findViewById(R.id.btn_eliminar_pregunta);

                ArticuloPregunta.setText( "Mensaje por " + clickedData.getTitulo() );
                Pregunta.setText( clickedData.getPreguntas());

                BntContestar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Contestar(clickedData.getidpregunta(),clickedData.gettoken(), String.valueOf( Respuesta.getText() ) );

                        pop_up1.hide();
                    }

                });

                BtnEliminar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog pop_up2 = new Dialog(getContext());
                        pop_up2.setContentView(R.layout.pop_up_ecommerce_elimina_pregunta );
                        pop_up2.show();
                        final Spinner spinner1;

                        spinner1 = pop_up2.findViewById(R.id.spinnerelimina);
                        List<String> list = new ArrayList<String>();
                        list.add("Eliminar Pregunta");
                        list.add("Eliminar y bloquear preguntas del usuario");
                        list.add("Eliminar y bloquear a usuario para que no oferte");

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_spinner_item, list);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner1.setAdapter(dataAdapter);

                        TextView Titulo= pop_up2.findViewById( R.id.text_pregunta_articulo_eliminar);
                        TextView Cliente= pop_up2.findViewById( R.id.text_clientepreguntaEliminar);
                        TextView PreguntaCliente= pop_up2.findViewById(R.id.text_preguntaeliminar);

                        UserML = clickedData.getUserML();

                        Titulo.setText("Mensaje por: " + clickedData.getTitulo());
                        Cliente.setText(clickedData.getComprador());
                        PreguntaCliente.setText(clickedData.getPreguntas());

                        Button BotonEliminarCliente = pop_up2.findViewById(R.id.btEliminapregunta);

                        Button BotonCerrarCliente = pop_up2.findViewById(R.id.btpreguntacerrar);

                        BotonEliminarCliente.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String selected = spinner1.getSelectedItem().toString();

                                if (selected.equals("Eliminar Pregunta"))
                                {
                                    progreso = new ProgressDialog(getContext());
                                    progreso.setMessage("Eliminando pregunta...");
                                    progreso.show();

                                    EliminaPregunta(clickedData.getidpregunta(),clickedData.gettoken());

                                    progreso.hide();
                                }
                                else if (selected.equals("Eliminar y bloquear preguntas del usuario"))
                                {
                                    progreso = new ProgressDialog(getContext());
                                    progreso.setMessage("Procesando...");
                                    progreso.show();

                                    EliminaPregunta(clickedData.getidpregunta(),clickedData.gettoken());

                                    EliminaBloquearPregunta(UserML,clickedData.getIDComprador(),clickedData.gettoken());

                                    progreso.hide();

                                }
                                else if (selected.equals("Eliminar y bloquear a usuario para que no oferte"))
                                {
                                    progreso = new ProgressDialog(getContext());
                                    progreso.setMessage("Procesando...");
                                    progreso.show();

                                    EliminaPregunta(clickedData.getidpregunta(),clickedData.gettoken());

                                    EliminaPreguntaBloquearUsuario(UserML,clickedData.getIDComprador(),clickedData.gettoken());

                                    progreso.hide();

                                }

                                pop_up2.hide();
                            }

                        });

                        BotonCerrarCliente.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                pop_up2.hide();
                            }

                        });

                    }
                });

                Btncerrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pop_up1.hide();
                    }

                });
            }
        };
    }


    public void CargaDatos(JSONObject Datos) {
        try {
            progreso = new ProgressDialog( getContext() );
            progreso.setMessage( "Cargando..." );
            progreso.show();

            tabla_preguntas = (SortablePreguntasTable) v.findViewById( R.id.tabla_preguntas );

            JSONArray RespuestaDatos = null;
            JSONArray RespuestaDatos2 = null;
            JSONObject RespuestaObjeto = null;
            JSONObject RespuestaObjeto2 = null;
            JSONObject Respuestapreguntas = null;
            JSONObject Respuestapreguntas2 = null;
            JSONArray RespuestaQuestions = null;
            JSONArray RespuestaQuestions2 = null;
            JSONObject Respuestaespecificaciones = null;
            JSONArray RespuestaComprador = null;

            String Titulo;
            String preguntas;
            String idcomprador;
            String id_pregunta = "";
            String comprador = "";
            int numeroregistro = 0;

            RespuestaDatos = Datos.getJSONArray( "aDatos" );

            RespuestaDatos2 = Datos.getJSONArray( "aDatos" );

            for (int z = 0; z < RespuestaDatos2.length(); z++) {

                RespuestaObjeto2 = RespuestaDatos2.getJSONObject( z );

                Respuestapreguntas2 = RespuestaObjeto2.getJSONObject( "preguntas" );

                int preguntitas = Respuestapreguntas2.getInt( "total" );

                numeroregistro += Respuestapreguntas2.getInt( "total" );

            }

            PreguntasModel = new String[numeroregistro][7];

            for (int x = 0; x < RespuestaDatos.length(); x++) {

                RespuestaObjeto = RespuestaDatos.getJSONObject( x );

                Respuestapreguntas = RespuestaObjeto.getJSONObject( "preguntas" );

                Respuestaespecificaciones = RespuestaObjeto.getJSONObject( "especificaciones" );

                Titulo = Respuestaespecificaciones.getString( "title" );

                RespuestaQuestions = Respuestapreguntas.getJSONArray( "questions" );

                for (int i = 0; i < RespuestaQuestions.length(); i++) {

                    JSONObject elemento = RespuestaQuestions.getJSONObject( i );

                    preguntas = elemento.getString( "text" );

                    id_pregunta = elemento.getString( "id" );

                    idcomprador = elemento.getJSONObject( "from" ).getString( "id" );

                    RespuestaComprador = Datos.getJSONArray( "aUsuariosQuePregunta" );

                    String Status = elemento.getString( "status" );

                    if(Status.equals("UNANSWERED")){

                        for (int a = 0; a < RespuestaComprador.length(); a++) {
                            JSONObject elemento2 = RespuestaComprador.getJSONObject( a );

                            String keyidcomp = elemento2.getString( "id_comprador" );

                            String Valor1 = String.valueOf( idcomprador );
                            String Valor2 = String.valueOf( keyidcomp );

                            if (Valor1.equals( Valor2 )) {

                                comprador = elemento2.getString( "nickname" );

                                break;
                            }

                        }

                        PreguntasModel[i][0] = preguntas;
                        PreguntasModel[i][1] = comprador;
                        PreguntasModel[i][2] = Titulo;
                        PreguntasModel[i][3] = id_pregunta;
                        PreguntasModel[i][4] = AccesToken;
                        PreguntasModel[i][5] = UserML;
                        PreguntasModel[i][6] = idcomprador;

                        final Preguntas_Model pregunta = new Preguntas_Model( preguntas, comprador, Titulo, id_pregunta, AccesToken,UserML,idcomprador );
                        Preguntas.add( pregunta );
                    }
                }


            }

            final PreguntasAdapter preguntasAdapter = new PreguntasAdapter( getContext(), Preguntas, tabla_preguntas );
            tabla_preguntas.setDataAdapter( preguntasAdapter );

            progreso.hide();

        } catch (JSONException e) {
            e.printStackTrace();

            Toast toast1 =
                    Toast.makeText( getContext(),
                            String.valueOf( e ), Toast.LENGTH_LONG );

            progreso.hide();

            try {
                JSONObject jsonObj = new JSONObject("{\"Estatus\":\"0\"}");

                RespuestaTodoJSON = jsonObj;
            } catch (JSONException ea) {
                e.printStackTrace();
            }

        }


    }

    public void LoadTable() {

        progreso = new ProgressDialog( getContext() );
        progreso.setMessage( "Cargando..." );
        progreso.show();

        tabla_preguntas = (SortablePreguntasTable) v.findViewById( R.id.tabla_preguntas );

        final String url = "http://187.189.192.150:8010/api/ecommerce/inicio_app?accesstoken=" + AccesToken + "&user_id=" + UserML + "&usu_id=" + usu_id + "&esApp=1";

        JsonObjectRequest getRequest = new JsonObjectRequest( Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        JSONArray RespuestaDatos = null;
                        JSONArray RespuestaDatos2 = null;
                        JSONObject RespuestaObjeto = null;
                        JSONObject RespuestaObjeto2 = null;
                        JSONObject Respuestapreguntas = null;
                        JSONObject Respuestapreguntas2 = null;
                        JSONArray RespuestaQuestions = null;
                        JSONArray RespuestaQuestions2 = null;
                        JSONObject Respuestaespecificaciones = null;
                        JSONArray RespuestaComprador = null;

                        String Titulo;
                        String preguntas;
                        String idcomprador;
                        String id_pregunta = "";
                        String comprador = "";
                        int numeroregistro = 0;

                        try {

                            RespuestaTodoJSON = response;

                            int EstatusApi = Integer.parseInt( response.getString( "estatus" ) );

                            if (EstatusApi == 1) {

                                RespuestaDatos = response.getJSONArray( "aDatos" );

                                RespuestaDatos2 = response.getJSONArray( "aDatos" );

                                for (int z = 0; z < RespuestaDatos2.length(); z++) {

                                    RespuestaObjeto2 = RespuestaDatos2.getJSONObject( z );

                                    Respuestapreguntas2 = RespuestaObjeto2.getJSONObject( "preguntas" );

                                    int preguntitas = Respuestapreguntas2.getInt( "total" );

                                    numeroregistro += Respuestapreguntas2.getInt( "total" );

                                }

                                PreguntasModel = new String[numeroregistro][7];

                                for (int x = 0; x < RespuestaDatos.length(); x++) {

                                    RespuestaObjeto = RespuestaDatos.getJSONObject( x );

                                    Respuestapreguntas = RespuestaObjeto.getJSONObject( "preguntas" );

                                    Respuestaespecificaciones = RespuestaObjeto.getJSONObject( "especificaciones" );

                                    Titulo = Respuestaespecificaciones.getString( "title" );

                                    RespuestaQuestions = Respuestapreguntas.getJSONArray( "questions" );

                                    for (int i = 0; i < RespuestaQuestions.length(); i++) {

                                        JSONObject elemento = RespuestaQuestions.getJSONObject( i );

                                        preguntas = elemento.getString( "text" );

                                        id_pregunta = elemento.getString( "id" );

                                        idcomprador = elemento.getJSONObject( "from" ).getString( "id" );

                                        RespuestaComprador = response.getJSONArray( "aUsuariosQuePregunta" );

                                        String Status = elemento.getString( "status" );

                                        if(Status.equals("UNANSWERED"))
                                       {
                                           for (int a = 0; a < RespuestaComprador.length(); a++) {
                                               JSONObject elemento2 = RespuestaComprador.getJSONObject( a );

                                               String keyidcomp = elemento2.getString( "id_comprador" );

                                               String Valor1 = String.valueOf( idcomprador );
                                               String Valor2 = String.valueOf( keyidcomp );

                                               if (Valor1.equals( Valor2 )) {

                                                   comprador = elemento2.getString( "nickname" );

                                                   break;
                                               }

                                           }

                                           PreguntasModel[i][0] = preguntas;
                                           PreguntasModel[i][1] = comprador;
                                           PreguntasModel[i][2] = Titulo;
                                           PreguntasModel[i][3] = id_pregunta;
                                           PreguntasModel[i][4] = AccesToken;
                                           PreguntasModel[i][5] = UserML;
                                           PreguntasModel[i][6] = idcomprador;

                                           final Preguntas_Model pregunta = new Preguntas_Model( preguntas, comprador, Titulo, id_pregunta, AccesToken,UserML,idcomprador );
                                           Preguntas.add( pregunta );
                                       }

                                    }

                                }

                            }

                            final PreguntasAdapter preguntasAdapter = new PreguntasAdapter( getContext(), Preguntas, tabla_preguntas );
                            tabla_preguntas.setDataAdapter( preguntasAdapter );


                            progreso.hide();

                        } catch (JSONException e) {
                            e.printStackTrace();

                            Toast toast1 =
                                    Toast.makeText( getContext(),
                                            String.valueOf( e ), Toast.LENGTH_LONG );

                            progreso.hide();

                        }


                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d( "Error.Response", String.valueOf( error ) );

                        Toast toast1 =
                                Toast.makeText( getContext(),
                                        String.valueOf( error ), Toast.LENGTH_LONG );

                        progreso.hide();
                    }
                }
        );

        getRequest.setShouldCache( false );

        VolleySingleton.getInstanciaVolley( getContext() ).addToRequestQueue( getRequest );

    }

    public void LoadButtons() {
        btn_pestania_ordenes = v.findViewById( R.id.btn_pestania_ordenes );
        btn_pestania_ordenes.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();

                if (RespuestaTodoJSON != null) {
                    bundle.putString( "Resultado", String.valueOf( RespuestaTodoJSON ) );

                    RespuestaTodoJSON = null;
                } else {
                    bundle.putString( "Resultado", String.valueOf( RespuestaTodo ) );
                }

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                Fragment_ecomerce secondFragment = new Fragment_ecomerce();
                secondFragment.setArguments( bundle );

                fragmentTransaction.replace( R.id.fragment_container, secondFragment );
                fragmentTransaction.commit();

            }
        } );
        btn_pestania_sincronizar = v.findViewById( R.id.btn_pestania_sincronizar );
        btn_pestania_sincronizar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();

                if (RespuestaTodoJSON != null) {
                    bundle.putString( "Resultado", String.valueOf( RespuestaTodoJSON ) );

                    RespuestaTodoJSON = null;
                } else {
                    bundle.putString( "Resultado", String.valueOf( RespuestaTodo ) );
                }

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                Fragment_ecommerce_Sincronizar secondFragment = new Fragment_ecommerce_Sincronizar();
                secondFragment.setArguments( bundle );

                fragmentTransaction.replace( R.id.fragment_container, secondFragment );
                fragmentTransaction.commit();

            }
        } );
    }

    public void LoadSpinner() {
        Articulo = new ArrayList<>();

        SpinnerArticulo = v.findViewById( R.id.Combo_articulo );

        Articulo.add( "Artículo 1" );
        Articulo.add( "Artículo 2" );
        Articulo.add( "Artículo 3" );

        SpinnerArticulo.setAdapter( new ArrayAdapter<String>( getContext(), android.R.layout.simple_spinner_item, Articulo ) );

    }


    private void Contestar(String itemid,String token,String Respuesta)
    {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Respondiendo pregunta...");
        progreso.show();

        JSONObject request = new JSONObject();
        try
        {
            request.put("question_id", itemid);
            request.put("text", Respuesta);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        String ApiPath = "https://api.mercadolibre.com/answers?access_token=" + token;

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {


                Toast toast1 =
                        Toast.makeText(getContext(),
                                String.valueOf("Pregunta contestada correctamente"), Toast.LENGTH_LONG);

                toast1.show();

                progreso.hide();
            }

        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast toast1 =
                                Toast.makeText(getContext(),
                                        "Error de conexion", Toast.LENGTH_SHORT);

                        toast1.show();

                        progreso.hide();

                    }
                }
        );

        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);

    }

    private void EliminaPregunta(String itemid,String token)
    {
        try {
            final String url = "https://api.mercadolibre.com/questions/"+ itemid + "?access_token="+ token;

            // prepare the Request
            JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.DELETE, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            Toast toast1 =
                                    Toast.makeText(getContext(),
                                            String.valueOf("Pregunta eliminada correctamente"), Toast.LENGTH_LONG);
                            toast1.show();

                            progreso.hide();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast toast1 =
                                    Toast.makeText(getContext(),
                                            error.toString(), Toast.LENGTH_LONG);

                            toast1.show();

                            progreso.hide();
                        }
                    }
            );

            VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(getRequest);


        } catch (Error e) {
            Toast toast1 =
                    Toast.makeText(getContext(),
                            e.toString(), Toast.LENGTH_LONG);
            toast1.show();

            progreso.hide();

        }


    }


    private void EliminaBloquearPregunta(String UsuMLID,String UsuID,String token)
    {
        try {

            JSONObject request = new JSONObject();
            try
            {
                request.put("user_id", UsuID);

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            String ApiPath = "https://api.mercadolibre.com/users/"+ UsuMLID + "/questions_blacklist?access_token="+ token;

            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
            {
                @Override
                public void onResponse(JSONObject response) {

                    Toast toast1 =
                            Toast.makeText(getContext(),
                                    String.valueOf("Usuario bloqueado correctamente"), Toast.LENGTH_LONG);

                    toast1.show();

                    progreso.hide();

                }

            },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast toast1 =
                                    Toast.makeText(getContext(),
                                            "Error de conexion", Toast.LENGTH_SHORT);

                            toast1.show();

                            progreso.hide();

                        }
                    }
            );

            VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);


        } catch (Error e) {
            Toast toast1 =
                    Toast.makeText(getContext(),
                            e.toString(), Toast.LENGTH_LONG);
            toast1.show();

            progreso.hide();

        }


    }


    private void EliminaPreguntaBloquearUsuario(String UsuMLID,String UsuID,String token)
    {
        try {

            JSONObject request = new JSONObject();
            try
            {
                request.put("user_id", UsuID);

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            String ApiPath = "https://api.mercadolibre.com/users/"+ UsuMLID + "/order_blacklist?access_token="+ token;

            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
            {
                @Override
                public void onResponse(JSONObject response) {

                    Toast toast1 =
                            Toast.makeText(getContext(),
                                    String.valueOf("Usuario bloqueado correctamente"), Toast.LENGTH_LONG);

                    toast1.show();

                    progreso.hide();

                }

            },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast toast1 =
                                    Toast.makeText(getContext(),
                                            "Error de conexion", Toast.LENGTH_SHORT);

                            toast1.show();

                            progreso.hide();
                        }
                    }
            );

            VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);


        } catch (Error e) {
            Toast toast1 =
                    Toast.makeText(getContext(),
                            e.toString(), Toast.LENGTH_LONG);
            toast1.show();

            progreso.hide();

        }


    }


}
