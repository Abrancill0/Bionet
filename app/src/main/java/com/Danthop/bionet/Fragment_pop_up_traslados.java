package com.Danthop.bionet;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_pop_up_traslados extends DialogFragment {
    private String usu_id;
    private Integer id;
    private String suc_nombre;
    private ArrayList<String> SucursalID;
    private ArrayList<String> SucursalName;
    private Spinner SpinnerSucursal;
    private Spinner SpinnerSucursal2;
    private Spinner SpinnerTipoTraslado;
    private EditText observaciones;
    private EditText Producto;
    private EditText Cantidad;
    private TextView SucOrigen;
    private TextView SucDestino;
    private ImageView atras;
    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextView text4;
    private TextView articulos;
    private TextView textViewObser;
    private TextView textfecha;
    private Spinner SpinnerFecha;
    ProgressDialog progreso;
    private String SucursalOrigen;
    private String SucursalDestino;
    private String UUIDexistencias;
    private String UUIDarticulo;
    private String UUIDvariante;
    private String UUIDmodificador;
    private float CantidadSolicitada;

    public Fragment_pop_up_traslados() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pop_up_traslados, container, false);

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id","");

        SucursalID = new ArrayList<>();
        SucursalName = new ArrayList<>();

        //atras=(ImageView)v.findViewById(R.id.atras);
        text1=(TextView)v.findViewById(R.id.text1);
        text2=(TextView)v.findViewById(R.id.text2);
        SucOrigen=(TextView)v.findViewById(R.id.SucOrigen);
        SpinnerSucursal=(Spinner)v.findViewById(R.id.Sucursal_Origen);
        SucDestino=(TextView)v.findViewById(R.id.SucDestino);
        SpinnerSucursal2=(Spinner)v.findViewById(R.id.Sucursal_Destino);
        text3=(TextView)v.findViewById(R.id.text3);
        observaciones=(EditText)v.findViewById(R.id.editObservaciones);
        textfecha=(TextView)v.findViewById(R.id.textfecha);
        SpinnerFecha=(Spinner)v.findViewById(R.id.SpinnerFecha);
        //text4=(TextView)v.findViewById(R.id.text4);
        //articulos = (TextView)v.findViewById(R.id.articulos);

        SpinnerTipoTraslado = (Spinner) v.findViewById(R.id.tipos_traslados);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.traslados, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        SpinnerTipoTraslado.setAdapter(adapter);

        SpinnerTipoTraslado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                VerificarTipoTraslado();
            }
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       Button aceptar = (Button) v.findViewById(R.id.btnaceptar);
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_pop_up_table_solicitartraslado dialog = new Fragment_pop_up_table_solicitartraslado();
                dialog.setTargetFragment(Fragment_pop_up_traslados.this, 1);
                dialog.show(getFragmentManager(), "MyCustomDialog");
                onDestroyView();
            }
        });

        Button cancelarTraslado = v.findViewById(R.id.CancelarTraslado);
        cancelarTraslado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroyView();
            }
        });

        SolicitarTraslado();
        SpinnerSucursales();
        return v;
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------
    private void VerificarTipoTraslado (){
        String Opcion;
        //Opcion = SpinnerTipoTraslado.getSelectedItemId().toString();

    }

//---------------------------------------------------------------------------------------------------------------------------------------------------------------
   private void  SolicitarTraslado(){
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Procesando...");
        progreso.show();
        //SucursalOrigen = SucursalName.get(SpinnerSucursal.getSelectedItemPosition());
        //SucursalDestino = SucursalID.get(SpinnerSucursal2.getSelectedItemPosition());


        JSONObject request = new JSONObject();
        try
        {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("suc_origen",SpinnerSucursal.getSelectedItem().toString());
            request.put("suc_destino",SpinnerSucursal2.getSelectedItem().toString());
            request.put("observaciones",observaciones.getText());
            request.put( "tat_id_existencias_origen", UUIDexistencias);
            request.put( "tat_id_articulo", UUIDarticulo);
            request.put( "tat_id_variante",UUIDvariante);
            request.put( "tat_id_modificador",UUIDmodificador );
            request.put( "tat_cantidad", CantidadSolicitada);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
       JSONArray ArrayArticulos = new JSONArray();
       ArrayArticulos.put(request);

       RequestQueue requestQueue = Volley.newRequestQueue(getContext());

       JSONObject jsonBodyrequest = new JSONObject();
       String url = getString(R.string.Url);
       String ApiPath = url + "/api/inventario/store_solicitud_traslado";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_pestania_traslado()).commit();
                progreso.hide();

                String estatus = "0";
                String mensaje = "";
                try {
                    estatus = response.getString("estatus");
                    mensaje = response.getString("mensaje");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                int status = Integer.parseInt(estatus);

                if (status == 1) {
                    Toast toast1 = Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT);
                    toast1.show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progreso.hide();
                        Toast toast1 = Toast.makeText(getContext(), "Error de conexion", Toast.LENGTH_SHORT);
                        toast1.show();
                    }
                }
        );
        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------------------
   private void SpinnerSucursales() {
        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);
        String ApiPath = url + "/api/configuracion/sucursales/index_app";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject Respuesta = null;
                JSONArray RespuestaSucursales = null;
                JSONObject RespuestaUUID = null;

                try {
                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1) {
                        Respuesta = response.getJSONObject("resultado");
                        RespuestaSucursales = Respuesta.getJSONArray("aSucursales");

                        for(int x = 0; x < RespuestaSucursales.length(); x++){
                            JSONObject elemento = RespuestaSucursales.getJSONObject(x);

                            RespuestaUUID = elemento.getJSONObject("suc_id");
                            String UUID = RespuestaUUID.getString("uuid");
                            SucursalID.add(UUID);

                            suc_nombre = elemento.getString("suc_nombre");
                            SucursalName.add(suc_nombre);

                        }SpinnerSucursal.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,SucursalName));
                        SpinnerSucursal2.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,SucursalName));
                    }
                    else {
                        Toast toast1 =
                                Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);
                        toast1.show();
                    }
                } catch (JSONException e) {
                    Toast toast1 =
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);
                    toast1.show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast1 =
                                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG);
                        toast1.show();
                    }
                }
        );
        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------------------

}