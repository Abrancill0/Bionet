package com.Danthop.bionet.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.Numero_sucursal;
import com.Danthop.bionet.R;
import com.Danthop.bionet.Tables.SortableClientesTable;
import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class ClienteAdapter extends LongPressAwareTableDataAdapter<ClienteModel> {

    int TEXT_SIZE = 12;

    private String[][] clienteModel;

    private SortableClientesTable tabla_clientes;

    private List<ClienteModel> clientes;

    private String nombre;
    private String UUID;
    private String telefono;
    private String correo_electronico;
    private String calle;

    private String UsuarioID;

    public ClienteAdapter(final Context context, final List<ClienteModel> data, final SortableClientesTable tableView) {
        super(context, data, tableView);

        if (tabla_clientes ==null ){
            tabla_clientes = tableView;
        }

    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        ClienteModel cliente = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderClienteName(cliente);
                break;
            case 1:
                renderedView = renderClienteCorreo(cliente);
                break;
            case 2:
                renderedView = renderClienteTelefono(cliente);
                break;
            case 3:
                renderedView = renderVerFicha(cliente);
                break;
            case 4:
                renderedView = renderEditar(cliente);
                break;
            case 5:
                renderedView = renderEliminar(cliente);
                break;
        }

        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final ClienteModel cliente = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 1:
                renderedView = renderEditableClienteName(cliente);
                break;
            default:
                renderedView = getDefaultCellView(rowIndex, columnIndex, parentView);
        }

        return renderedView;
    }

    private View renderEditableClienteName(final ClienteModel cliente) {
        final EditText editText = new EditText(getContext());
        editText.setText(cliente.getCliente_Nombre());
        editText.setPadding(20, 10, 20, 10);
        editText.setTextSize(TEXT_SIZE);
        editText.setSingleLine();
        editText.addTextChangedListener(new ClienteNameUpdater(cliente));
        return editText;
    }

    private View renderClienteName(final ClienteModel cliente) {
        return renderString(cliente.getCliente_Nombre());
    }

    private View renderClienteCorreo(final ClienteModel cliente) {
        return renderString(cliente.getCliente_Correo());
    }

    private View renderClienteTelefono(final ClienteModel cliente) {
        return renderString(cliente.getCliente_Telefono());
    }

    private View renderClienteUltimaVisita(final ClienteModel cliente) {
        return renderString(cliente.getCliente_Ultima_Visita());
    }

    private View renderEliminar(final ClienteModel cliente) {
        return ButtonEliminar(cliente);
    }

    private View renderVerFicha(final ClienteModel cliente) {
        return ButtonVerFicha(cliente);
    }

    private View renderEditar(final ClienteModel cliente) {
        return ButtonEditar(cliente);
    }

    private View ButtonVerFicha(final ClienteModel cliente){
        final Button ver = new Button(getContext());
        ver.setText("Ver");
        ver.setPadding(10, 10, 10, 10);
        ver.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Dialog ver_cliente_dialog;
                ver_cliente_dialog=new Dialog(getContext());
                ver_cliente_dialog.setContentView(R.layout.pop_up_ficha_cliente);
                ver_cliente_dialog.show();
                TextView NameCliente = ver_cliente_dialog.findViewById(R.id.cliente_nombre);
                TextView CorreoCliente = ver_cliente_dialog.findViewById(R.id.email_cliente);
                TextView TelefonoCliente = ver_cliente_dialog.findViewById(R.id.telefono_cliente);

                NameCliente.setText(cliente.getCliente_Nombre());
                CorreoCliente.setText(cliente.getCliente_Correo());
                TelefonoCliente.setText(cliente.getCliente_Telefono());
            }
        });
        return ver;
    }

    private View ButtonEditar(final ClienteModel cliente){
        final Button editar = new Button(getContext());
        editar.setText("Editar");
        editar.setPadding(10, 10, 10, 10);
        editar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                /* sdaddsdad >>>___----___---___--__--_-__---_----_-__----_---_---------_-------------------___--*/
                /*  sdagsdggsssdddsdsd   -->---<>_>__--->>_-<-<<<<-----_-<-<-<-_<-_<--<-xascsvsdvsdv-sd-v-sdv-sd */
            }
        });
        return editar;
    }

    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }

    private View ButtonEliminar(final ClienteModel cliente){
        final Button eliminar = new Button(getContext());

        eliminar.setText("Eliminar");
        eliminar.setPadding(20, 10, 20, 10);
        eliminar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                final Dialog pop_up_confirmacion_eliminar =new Dialog(getContext());
                pop_up_confirmacion_eliminar.setContentView(R.layout.pop_up_confirmar_eliminar_cliente);
                pop_up_confirmacion_eliminar.show();
                Button aceptar = pop_up_confirmacion_eliminar.findViewById(R.id.AceptarEliminar);
                Button cancelar = pop_up_confirmacion_eliminar.findViewById(R.id.CancelarEliminar);

                aceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        JSONObject request = new JSONObject();
                        try
                        {
                            request.put("usu_id", cliente.getCliente_usu_id());
                            request.put("cli_id", cliente.getCliente_UUID());
                            request.put("esApp", "1");

                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }

                        String url = "http://187.189.192.150:8010/";

                        String ApiPath = url + "api/clientes/delete";

                        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response) {

                                JSONObject Respuesta = null;

                                try {

                                    int status = Integer.parseInt(response.getString("estatus"));
                                    String Mensaje = response.getString("mensaje");

                                    if (status == 1)
                                    {

                                        Toast toast1 =
                                                Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);

                                        toast1.show();

                                        Muestra_clientes(cliente.getCliente_usu_id());

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

                        pop_up_confirmacion_eliminar.dismiss();


                    }
                });

                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pop_up_confirmacion_eliminar.dismiss();
                    }
                });


            }
        });
        return eliminar;
    }

    private static class ClienteNameUpdater implements TextWatcher {

        private ClienteModel clienteToUpdate;

        public ClienteNameUpdater(ClienteModel clienteToUpdate) {
            this.clienteToUpdate = clienteToUpdate;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // no used
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // not used
        }

        @Override
        public void afterTextChanged(Editable s) {
            clienteToUpdate.setCliente_Nombre(s.toString());
        }
    }


    private void Muestra_clientes(String usu_id)
    {

        UsuarioID = usu_id;

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

        String url = "http://187.189.192.150:8010";

        String ApiPath = url + "/api/clientes/index_app";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject Respuesta = null;
                JSONObject RespuestaNodoDireccion= null;
                JSONObject ElementoUsuario=null;
                JSONObject ElementoCorreo=null;
                JSONArray RespuestaNodoClientes= null;

                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");
                    //     Respuesta = response.getJSONObject("resultado");
                    if (status == 1)
                    {

                        Respuesta = response.getJSONObject("resultado");

                        RespuestaNodoClientes = Respuesta.getJSONArray("aClientes");

                        clientes = new ArrayList<>();

                        clienteModel = new String[RespuestaNodoClientes.length()][5];

                        for(int x = 0; x < RespuestaNodoClientes.length(); x++){
                            JSONObject elemento = RespuestaNodoClientes.getJSONObject(x);

                            ElementoUsuario =  elemento.getJSONObject("cli_id");

                            UUID = ElementoUsuario.getString( "uuid");
                            nombre = elemento.getString("cli_nombre");
                            correo_electronico = elemento.getString("cli_correo_electronico");
                            telefono = elemento.getString("cli_telefono");
                            RespuestaNodoDireccion = elemento.getJSONObject("cli_direccion");
                            calle = RespuestaNodoDireccion.getString("cli_calle");
                            final ClienteModel cliente = new ClienteModel(UUID,nombre, correo_electronico, telefono,"",UsuarioID);
                            clientes.add(cliente);
                        }

                        final ClienteAdapter clienteAdapter = new ClienteAdapter(getContext(), clientes, tabla_clientes);
                        tabla_clientes.setDataAdapter(clienteAdapter);

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