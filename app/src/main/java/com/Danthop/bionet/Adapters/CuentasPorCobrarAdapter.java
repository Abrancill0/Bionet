package com.Danthop.bionet.Adapters;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.Feenicia_Transaction_Bluetooth;
import com.Danthop.bionet.Fragment_Ventas;
import com.Danthop.bionet.Fragment_crear_cliente;
import com.Danthop.bionet.Fragment_editarCliente;
import com.Danthop.bionet.Numero_sucursal;
import com.Danthop.bionet.R;
import com.Danthop.bionet.Tables.SortableClienteContactos;
import com.Danthop.bionet.Tables.SortableClientesTable;
import com.Danthop.bionet.Tables.SortableCuentasPorCobrarTable;
import com.Danthop.bionet.Tables.SortableMetodosPagoTable;
import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.CompraModel;
import com.Danthop.bionet.model.ContactoModel;
import com.Danthop.bionet.model.CuentaPendienteModel;
import com.Danthop.bionet.model.PagoModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.google.gson.JsonObject;
import com.sortabletableview.recyclerview.toolkit.FilterHelper;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;


public class CuentasPorCobrarAdapter extends LongPressAwareTableDataAdapter<CuentaPendienteModel>{

    int TEXT_SIZE = 16;
    private SortableClienteContactos tabla_clientes;

    private List<CuentaPendienteModel> cuentaPendienteList;
    private List<CuentaPendienteModel> cuentaPendienteListFull;

    private ArrayList<String> sucursales_cliente;

    private SortableCuentasPorCobrarTable tabla_cuentas;




    private ProgressDialog progressDialog;

    private String usu_id;
    private String code;
    private String Cli_id;

    private Typeface s;

    private String UsuarioID;

    public CuentasPorCobrarAdapter(final Context context, final List<CuentaPendienteModel> data, final SortableCuentasPorCobrarTable tableView,
                                   ArrayList<String> SucursalesCliente,
                                   String cli_id) {
        super(context, data, tableView);

        cuentaPendienteList = data;
        cuentaPendienteListFull = new ArrayList<>(data);
        sucursales_cliente = SucursalesCliente;
        tabla_cuentas = tableView;
        Cli_id = cli_id;

        SharedPreferences sharedPref = getContext().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id", "");
        code = sharedPref.getString("sso_code","");
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Espere un momento por favor");
        progressDialog.setCanceledOnTouchOutside(false);



    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        CuentaPendienteModel cuenta = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderTicket(cuenta);
                break;
            case 1:
                renderedView = renderFecha(cuenta);
                break;
            case 2:
                renderedView = renderCargo(cuenta);
                break;
            case 3:
                renderedView = renderAbono(cuenta);
                break;
            case 4:
                renderedView = renderPendiente(cuenta);
                break;
            case 5:
                renderedView = renderButtonAbono(cuenta);
                break;
        }

        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final CuentaPendienteModel cuenta = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 1:

            default:

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

    private View renderTicket(final CuentaPendienteModel cuenta) {
        return renderString(cuenta.getTicket());
    }

    private View renderFecha(final CuentaPendienteModel cuenta) {
        if(cuenta.getTipo().equals("total"))
        {
            final TextView textView = new TextView(getContext());
            textView.setText(cuenta.getFecha());
            textView.setTextSize(TEXT_SIZE);
            textView.setPadding(5,5,5,5);
            textView.setTextColor(getResources().getColor(R.color.green));
            textView.setGravity(View.TEXT_ALIGNMENT_VIEW_START);
            textView.setGravity(View.TEXT_ALIGNMENT_TEXT_END);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return textView;
        }
        return renderString(cuenta.getFecha());
    }

    private View renderCargo(final CuentaPendienteModel cuenta) {
        if(cuenta.getTipo().equals("total"))
        {
            final TextView textView = new TextView(getContext());
            textView.setText(cuenta.getCargo());
            textView.setTextSize(TEXT_SIZE);
            textView.setPadding(5,5,5,5);
            textView.setTextColor(getResources().getColor(R.color.green));
            textView.setGravity(View.TEXT_ALIGNMENT_VIEW_START);
            textView.setGravity(View.TEXT_ALIGNMENT_TEXT_END);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return textView;
        }
        return renderString(cuenta.getCargo());
    }

    private View renderAbono(final CuentaPendienteModel cuenta) {
        if(cuenta.getTipo().equals("total"))
        {
            final TextView textView = new TextView(getContext());
            textView.setText(cuenta.getAbono());
            textView.setTextSize(TEXT_SIZE);
            textView.setPadding(5,5,5,5);
            textView.setTextColor(getResources().getColor(R.color.green));
            textView.setGravity(View.TEXT_ALIGNMENT_VIEW_START);
            textView.setGravity(View.TEXT_ALIGNMENT_TEXT_END);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return textView;
        }
        return renderString(cuenta.getAbono());
    }

    private View renderPendiente(final CuentaPendienteModel cuenta) {
        if(cuenta.getTipo().equals("total"))
        {
            final TextView textView = new TextView(getContext());
            textView.setText(cuenta.getPendiente());
            textView.setTextSize(TEXT_SIZE);
            textView.setPadding(20, 10, 20, 10);
            textView.setTextColor(getResources().getColor(R.color.green));
            textView.setGravity(View.TEXT_ALIGNMENT_VIEW_START);
            textView.setGravity(View.TEXT_ALIGNMENT_TEXT_END);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return textView;
        }
        return renderString(cuenta.getPendiente());
    }

    private View renderButtonAbono(final CuentaPendienteModel cuenta)
    {
        if(cuenta.getTipo().equals("cargo"))
        {
            final Button abonar = new Button(getContext());
            abonar.setText("Abonar");
            abonar.setTextColor(getResources().getColor(R.color.white));
            abonar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            abonar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadAbonar(cuenta);
                }
            });
            return abonar;
        }
        else
        {
            final TextView nada = new TextView(getContext());
            nada.setText("");
            return nada;
        }
    }

    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setTextSize(TEXT_SIZE);
        textView.setPadding(20, 10, 20, 10);
        textView.setGravity(View.TEXT_ALIGNMENT_VIEW_START);
        textView.setGravity(View.TEXT_ALIGNMENT_TEXT_END);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        return textView;
    }

    private static class ClienteNameUpdater implements TextWatcher {

        private ClienteModel clienteToUpdate;

        public ClienteNameUpdater(ClienteModel clienteToUpdate) {

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

        }
    }

    @Override
    public Filter getFilter() {
        return CuentaFilter;
    }

    private Filter CuentaFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<CuentaPendienteModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(cuentaPendienteListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (CuentaPendienteModel item : cuentaPendienteListFull) {
                    if (item.getTicket().toLowerCase().contains(filterPattern)
                            || item.getFecha().toLowerCase().contains(filterPattern)
                            || item.getCargo().toLowerCase().contains(filterPattern)
                            || item.getAbono().toLowerCase().contains(filterPattern)
                            || item.getPendiente().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            cuentaPendienteList.clear();
            cuentaPendienteList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    private void LoadAbonar(CuentaPendienteModel cuenta)
    {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.pop_up_clientes_ver_cliente_abonar);
        dialog.show();
        Button btn_importe = dialog.findViewById(R.id.btn_importe);
        Button btn_datos = dialog.findViewById(R.id.btn_datos_adicionales);
        View layout_importe = dialog.findViewById(R.id.layout_importe);
        View layout_datos = dialog.findViewById(R.id.layout_datos_adicionales);
        ArrayList<String> SucursalName = new ArrayList<>();
        ArrayList<String> SucursalID =new ArrayList<>();
        List<PagoModel> ListaDePagosDisponibles=new ArrayList<>();
        List<PagoModel> ListaDePagos_a_utilizar=new ArrayList<>();
        Spinner SpinnerSucursal = (Spinner) dialog.findViewById(R.id.sucursal);
        SortableMetodosPagoTable tabla_metodos_pago = dialog.findViewById(R.id.tabla_seleccionar_metodo_pago);
        tabla_metodos_pago.setEmptyDataIndicatorView(dialog.findViewById(R.id.Tabla_vacia));

        EditText BancoEmisor = dialog.findViewById(R.id.text_banco_emisor);
        EditText CuentaOrdentante = dialog.findViewById(R.id.text_cuenta_ordenante);
        EditText CuentaBeneficiario = dialog.findViewById(R.id.text_cuenta_beneficiario);
        EditText CadenaPago = dialog.findViewById(R.id.text_cadena_pago);


        TextView importe_recibido = dialog.findViewById(R.id.text_view_recibido);
        TextView importe_faltante = dialog.findViewById(R.id.text_view_faltante);

        double pendiente_Currency = Double.parseDouble( cuenta.getPendiente() );
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        TextView importe_pendiente = dialog.findViewById(R.id.importe_pendiente);
        importe_pendiente.setText(formatter.format( pendiente_Currency ));

        LoadSucursales(SucursalName,SucursalID,SpinnerSucursal);

        btn_importe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_importe.setBackgroundColor(getResources().getColor(R.color.fondo_azul));
                btn_datos.setBackgroundResource(R.drawable.pestanas_desplegables);
                layout_importe.setVisibility(View.VISIBLE);
                layout_datos.setVisibility(View.GONE);
            }
        });
        btn_datos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_datos.setBackgroundColor(getResources().getColor(R.color.fondo_azul));
                btn_importe.setBackgroundResource(R.drawable.pestanas_desplegables);
                layout_importe.setVisibility(View.GONE);
                layout_datos.setVisibility(View.VISIBLE);
            }
        });

        SpinnerSucursal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
               LoadMetodosPago(ListaDePagosDisponibles,ListaDePagos_a_utilizar,SucursalID,SpinnerSucursal,tabla_metodos_pago,
                       importe_faltante,importe_recibido,
                       cuenta);

            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        TextView realizarPago = dialog.findViewById(R.id.realizar_Pago);
        realizarPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!ListaDePagos_a_utilizar.isEmpty())
                {
                    double totalsumaimportes = 0;
                    double TarjetaCredito = 0;
                    double TarjetaDebito = 0;
                    double PagosEfectivo = 0;


                    for (int i = 0; i < ListaDePagos_a_utilizar.size(); i++) {

                        String tipo_pago = ListaDePagos_a_utilizar.get(i).getNombre();
                        String cantPago = ListaDePagos_a_utilizar.get(i).getCantidad();

                        if (tipo_pago.equals("Tarjeta de crédito"))
                        {
                            TarjetaCredito = Double.parseDouble(cantPago);
                        }

                        if (tipo_pago.equals("Tarjeta débito"))
                        {
                            TarjetaDebito = Double.parseDouble(cantPago);
                        }

                        if(tipo_pago.equals("Efectivo")){

                            PagosEfectivo +=Double.parseDouble(cantPago);

                        }

                        totalsumaimportes += Double.parseDouble(cantPago);

                    }

                    //Validar montos antes de pasar de pantallas
                    String TotalText = cuenta.getPendiente();

                    double TotalFormat = 0;
                    String cleanString = TotalText.replaceAll("\\D", "");
                    try {
                        TotalFormat = Double.parseDouble(cleanString);
                        TotalFormat = TotalFormat / 100;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    double totalvalida = totalsumaimportes-PagosEfectivo;

                    Toast toast2 =
                            Toast.makeText(getContext(),
                                    totalvalida + " " + totalsumaimportes + " " + PagosEfectivo,
                                    Toast.LENGTH_LONG);
                    //toast2.show();


                    dialog.dismiss();

                    double valorTarjetas = 0;

                    valorTarjetas = TarjetaCredito + TarjetaDebito;

                    if (valorTarjetas > 0)
                    {

                        Intent myIntent = new Intent(getContext(), Feenicia_Transaction_Bluetooth.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putDouble("TC",TarjetaCredito);
                        mBundle.putDouble("TD",TarjetaDebito);
                        mBundle.putString( "Ticket",cuenta.getTicket_id());
                        mBundle.putString("Sucursal",SucursalID.get(SpinnerSucursal.getSelectedItemPosition()));
                        mBundle.putInt( "03meses",2);
                        mBundle.putInt( "06meses",2);
                        mBundle.putInt( "09meses",2);
                        mBundle.putInt( "12meses",2);


                        mBundle.putInt("Tamano",ListaDePagos_a_utilizar.size());

                        for (int i = 0; i < ListaDePagos_a_utilizar.size(); i++) {
                            mBundle.putInt("fpa_id"+i, Integer.parseInt( ListaDePagos_a_utilizar.get(i).getId()));
                            mBundle.putString("valor"+i,ListaDePagos_a_utilizar.get(i).getCantidad());
                        }

                        myIntent.putExtras(mBundle);


                        getContext().startActivity(myIntent);
                    }
                    else
                    {


                        dialog.setContentView(R.layout.pop_up_ventas_confirmacion_venta);
                        dialog.show();
                        progressDialog.show();

                        Button cerrarPopUp = dialog.findViewById(R.id.btnSalir3);
                        cerrarPopUp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        TextView importe_venta = dialog.findViewById(R.id.importe_venta);
                        TextView importe_recibido = dialog.findViewById(R.id.importe_recibido);
                        TextView importe_cambio = dialog.findViewById(R.id.importe_cambio);
                        FinalizarTicket(importe_cambio, importe_recibido, importe_venta,ListaDePagos_a_utilizar,cuenta,
                                SucursalID,SpinnerSucursal,
                                BancoEmisor,
                                CuentaOrdentante,
                                CuentaBeneficiario,
                                CadenaPago);


                        Button aceptar = dialog.findViewById(R.id.aceptar_cerrar_ventana);
                        aceptar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                }


            }
        });

    }

    private void LoadSucursales(ArrayList<String> SucursalName, ArrayList<String> SucursalID, Spinner SpinnerSucursal)
    {
        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("code",code);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = getContext().getString(R.string.Url);

        String ApiPath = url + "/api/configuracion/sucursales/index_app";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject Respuesta = null;
                JSONArray RespuestaNodoSucursales = null;
                JSONObject RespuestaNodoID = null;

                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");
                    if (status == 1) {
                        progressDialog.dismiss();

                        Respuesta = response.getJSONObject("resultado");

                        RespuestaNodoSucursales = Respuesta.getJSONArray("aSucursales");

                        for (int x = 0; x < RespuestaNodoSucursales.length(); x++) {
                            JSONObject jsonObject1 = RespuestaNodoSucursales.getJSONObject(x);
                            String sucursal = jsonObject1.getString("suc_nombre");
                            for(int f=0; f<sucursales_cliente.size();f++)
                            {
                                if(sucursal.equals(sucursales_cliente.get(f)))
                                {
                                    SucursalName.add(sucursal);
                                    RespuestaNodoID = jsonObject1.getJSONObject("suc_id");
                                    String id = RespuestaNodoID.getString("uuid");
                                    SucursalID.add(id);
                                }
                            }
                        }
                        SpinnerSucursal.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, SucursalName));

                    } else {
                        progressDialog.dismiss();
                        Toast toast1 =
                                Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);

                        toast1.show();


                    }

                } catch (JSONException e) {
                    progressDialog.dismiss();

                    Toast toast1 =
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);

                    toast1.show();


                }

            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast toast1 =
                                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG);

                        toast1.show();


                    }
                }
        );
        postRequest.setShouldCache(false);
        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);
    }


    private void LoadMetodosPago(List<PagoModel> ListaDePagosDisponibles,
                                 List<PagoModel> ListaDePagos_a_utilizar,
                                 ArrayList<String> SucursalID,
                                 Spinner SpinnerSucursal,
                                 SortableMetodosPagoTable tabla_metodos_pago,
                                 TextView textViewFaltante,
                                 TextView textViewRecibo,
                                 CuentaPendienteModel cuenta)
    {

        ListaDePagosDisponibles.clear();
        ListaDePagos_a_utilizar.clear();
        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("tic_id_sucursal", SucursalID.get(SpinnerSucursal.getSelectedItemPosition()));
            request.put("code", code);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = getContext().getString(R.string.Url);

        String ApiPath = url + "/api/ventas/tickets/select-formas-pago";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray Respuesta = null;
                JSONObject RespuestaNodoTicket = null;
                JSONObject TicketID = null;
                JSONArray NodoTicketArticulos = null;

                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1) {

                        Respuesta = response.getJSONArray("resultado");

                        for (int x = 0; x < Respuesta.length(); x++) {
                            JSONObject elemento = Respuesta.getJSONObject(x);
                            String id = elemento.getString("fpa_id");
                            String nombre = elemento.getString("fpa_nombre");


                            final PagoModel pago = new PagoModel(
                                    nombre,
                                    id,
                                    ""
                            );
                            ListaDePagosDisponibles.add(pago);
                        }
                        MetodoPagoAdapter metodo = new MetodoPagoAdapter(getContext(), ListaDePagosDisponibles,tabla_metodos_pago,
                                ListaDePagos_a_utilizar,textViewRecibo,textViewFaltante,
                                cuenta.getPendiente());
                        tabla_metodos_pago.setDataAdapter(metodo);
                        metodo.notifyDataSetChanged();

                    } else {
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
        postRequest.setShouldCache(false);
        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);
    }

    private void FinalizarTicket(final TextView importeCambio,
                                 final TextView importeRecibido,
                                 final TextView importeVenta,
                                 List<PagoModel> ListaDePagos_a_utilizar,
                                 CuentaPendienteModel cuenta,
                                 ArrayList<String> SucursalID,
                                 Spinner SpinnerSucursal,
                                 EditText BancoEmisor,
                                 EditText CuentaOrdenante,
                                 EditText CuentaBeneficiario,
                                 EditText CadenaPago
                                 ) {

        JSONArray arreglo = new JSONArray();
        try {
            for (int i = 0; i < ListaDePagos_a_utilizar.size(); i++) {
                JSONObject list1 = new JSONObject();
                list1.put("fpa_id", ListaDePagos_a_utilizar.get(i).getId());
                list1.put("valor", ListaDePagos_a_utilizar.get(i).getCantidad());
                arreglo.put(list1);
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        JSONObject arreglo2 = new JSONObject();
        try {
            arreglo2.put("do_banco_emisor", BancoEmisor);
            arreglo2.put("do_cuenta_ordenado", CuentaOrdenante);
            arreglo2.put("do_cuenta_beneficiario", CuentaBeneficiario);
            arreglo2.put("do_cadena_pago", CadenaPago);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }


        JSONObject request = new JSONObject();

        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("cxc_id",cuenta.getCxC_ID());
            request.put("cxc_id_ticket", cuenta.getTicket_id());
            request.put("tpa_importe_forma_pago", arreglo);
            request.put("id_sucursal",SucursalID.get(SpinnerSucursal.getSelectedItemPosition()));
            request.put("tpa_datos_opcionales",arreglo2);
            request.put("importe_pendiente",cuenta.getPendiente());
            request.put("code", code);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = getContext().getString(R.string.Url);

        String ApiPath = url + "/api/clientes/abonar-parcialidades";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1) {

                        JSONObject Respuesta = response.getJSONObject("resultado");
                        JSONObject RespuestaNodoTicket = Respuesta.getJSONObject("aPago");

                        JSONObject Nodotic_importe_total = RespuestaNodoTicket.getJSONObject("tpa_importe_ticket");
                        JSONObject Nodotic_importe_recibido = RespuestaNodoTicket.getJSONObject("tpa_importe_recibido");
                        JSONObject Nodotic_importe_cambio = RespuestaNodoTicket.getJSONObject("tpa_importe_cambio");
                        String tic_importe_total = Nodotic_importe_total.getString("value");
                        String tic_importe_recibido = Nodotic_importe_recibido.getString("value");
                        String tic_importe_cambio = Nodotic_importe_cambio.getString("value");


                        double CambioConDecimal = Double.parseDouble(tic_importe_cambio);
                        double RecibidoConDecimal = Double.parseDouble(tic_importe_recibido);
                        double ImporteTotalConDecimal = Double.parseDouble(tic_importe_total);
                        NumberFormat formatter = NumberFormat.getCurrencyInstance();

                        importeCambio.setText(formatter.format(CambioConDecimal));
                        importeRecibido.setText(formatter.format(RecibidoConDecimal));
                        importeVenta.setText(formatter.format(ImporteTotalConDecimal));

                        LoadCuentasPorCobrar();



                        progressDialog.dismiss();
                    } else {
                        progressDialog.dismiss();
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
        postRequest.setShouldCache(false);
        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);
    }

    private void LoadCuentasPorCobrar()
    {
        cuentaPendienteList.clear();
        progressDialog.show();
        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("cli_id", Cli_id);
            request.put("code",code);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = getContext().getString(R.string.Url);

        String ApiPath = url + "/api/clientes/select-cliente";

        //clienteAdapter.clear();

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject Respuesta = null;
                JSONObject RespuestaNodoDireccion = null;
                JSONArray ArregloCuentas = null;

                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1) {
                        Respuesta = response.getJSONObject("resultado");

                        ArregloCuentas = Respuesta.getJSONArray("aCxC");
                        int abono=0;
                        String pendiente="";
                        for(int i = 0; i<ArregloCuentas.length();i++)
                        {
                            JSONObject elemento = ArregloCuentas.getJSONObject(i);
                            JSONObject cxcIDNodo = elemento.getJSONObject("cxc_id");
                            String cxcID = cxcIDNodo.getString("uuid");
                            String Ticket = elemento.getString("cxc_numero_ticket");
                            JSONObject nodoIDTicket = elemento.getJSONObject("cxc_id_ticket");
                            String Ticket_id = nodoIDTicket.getString("uuid");
                            String fecha = elemento.getString("cxc_fecha_hora_creo");
                            String Cargo = elemento.getString("cxc_impore_ticket");
                            String Abono = elemento.getString("cxc_impore_pago");
                            int AbonoEnInt = Integer.parseInt(Abono);
                            abono= abono+AbonoEnInt;
                            String tipo = elemento.getString("cxc_tipo");
                            if(tipo.equals("cargo"))
                            {
                                pendiente = elemento.getString("cxc_impore_pendiente");
                                Abono="";
                            }
                            CuentaPendienteModel cuenta = new CuentaPendienteModel(cxcID,Ticket,Ticket_id,fecha,Cargo,Abono,pendiente,tipo);
                            cuentaPendienteList.add(cuenta);
                            if((i+1)<ArregloCuentas.length())
                            {
                                JSONObject elementoSiguiente = ArregloCuentas.getJSONObject(i+1);
                                if(cuenta.getTipo().equals("abono")&&elementoSiguiente.getString("cxc_tipo").equals("cargo"))
                                {
                                    String abonoEnString = String.valueOf(abono);
                                    CuentaPendienteModel cuentaTotal = new CuentaPendienteModel(cxcID,Ticket,Ticket_id,"Total",Cargo,abonoEnString,pendiente,"total");
                                    cuentaPendienteList.add(cuentaTotal);
                                    abono=0;
                                }
                            }
                            else
                            {
                                String abonoEnString = String.valueOf(abono);
                                CuentaPendienteModel cuentaTotal = new CuentaPendienteModel(cxcID,Ticket,Ticket_id,"Total",Cargo,abonoEnString,pendiente,"total");
                                cuentaPendienteList.add(cuentaTotal);
                                abono=0;
                            }
                        }

                        CuentasPorCobrarAdapter cuentasAdapter = new CuentasPorCobrarAdapter(getContext(),cuentaPendienteList,tabla_cuentas,sucursales_cliente,Cli_id);
                        tabla_cuentas.setDataAdapter(cuentasAdapter);
                        ViewCompat.setNestedScrollingEnabled(tabla_cuentas, true);
                        cuentasAdapter.notifyDataSetChanged();

                        progressDialog.dismiss();
                    } else {
                        progressDialog.dismiss();
                        Toast toast1 =
                                Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);
                        toast1.show();
                    }

                } catch (JSONException e) {
                    progressDialog.dismiss();
                    Toast toast1 =
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);
                    toast1.show();
                }
            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast toast1 =
                                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG);
                        toast1.show();
                    }
                }
        );
        postRequest.setShouldCache(false);
        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);
    }




}