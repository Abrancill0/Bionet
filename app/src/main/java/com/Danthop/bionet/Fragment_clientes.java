package com.Danthop.bionet;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.Danthop.bionet.R;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;


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
public class Fragment_clientes extends Fragment {
    private String usu_id;
    private String[][] clienteModel;
    private TableView tabla_clientes;
    private ProgressDialog progreso;



    public Fragment_clientes() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_clientes,container, false);

        tabla_clientes = (TableView) v.findViewById(R.id.tabla_clientes);
        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(), "ID", "Nombre", "Correo Eléctronico", "Teléfono", "Últ. Visita", "Consumo Promedio","ID Referencia");
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);

        usu_id = sharedPref.getString("usu_id","");


        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(7);
        tableColumnWeightModel.setColumnWeight(0, 1);
        tableColumnWeightModel.setColumnWeight(1, 3);
        tableColumnWeightModel.setColumnWeight(2, 3);
        tableColumnWeightModel.setColumnWeight(3, 3);
        tableColumnWeightModel.setColumnWeight(4, 3);
        tableColumnWeightModel.setColumnWeight(5, 3);
        tableColumnWeightModel.setColumnWeight(6, 3);





        tabla_clientes.setHeaderAdapter(simpleHeader);
        tabla_clientes.setColumnModel(tableColumnWeightModel);
      //  Muestra_sucursales();

        Button btn_crear_cliente = (Button) v.findViewById(R.id.btn_crear_cliente);
        btn_crear_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_crear_cliente()).commit();
            }
        });

        return v;


    }

    private void Muestra_sucursales()
    {
        JSONObject request = new JSONObject();
        try
        {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/clientes/index_app";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject Respuesta = null;
                JSONObject RespuestaNodoDireccion= null;
                JSONObject ElementoTelefono=null;
                JSONObject ElementoCorreo=null;
                JSONArray RespuestaNodoSucursal= null;

                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1)
                    {
                        String nombre;
                        String telefono;
                        String correo_electronico;
                        String calle;

                        Respuesta = response.getJSONObject("resultado");

                        RespuestaNodoSucursal = Respuesta.getJSONArray("aClientes");

                        clienteModel = new String[RespuestaNodoSucursal.length()][6];

                        for(int x = 0; x < RespuestaNodoSucursal.length(); x++){
                            JSONObject elemento = RespuestaNodoSucursal.getJSONObject(x);

                            nombre = elemento.getString("cli_nombre");
                            ElementoCorreo = elemento.getJSONObject("cli_correo_electronico");
                            correo_electronico = ElementoCorreo.getJSONArray("values").getString( 0);

                            ElementoTelefono = elemento.getJSONObject("cli_telefono");
                            telefono = ElementoTelefono.getString("value");




                            RespuestaNodoDireccion = elemento.getJSONObject("suc_direccion");
                            calle = RespuestaNodoDireccion.getString("suc_calle");

                            //sucursalModel[x][0] = String.valueOf(x);
                            clienteModel[x][0] =nombre;
                            clienteModel[x][1] =telefono;
                            clienteModel[x][2] =correo_electronico;
                            clienteModel[x][3] =calle;

                        }

                        tabla_clientes.setDataAdapter(new SimpleTableDataAdapter(getContext(), clienteModel));


                    }
                    else
                    {
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
                new Response.ErrorListener()
                {
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

}
