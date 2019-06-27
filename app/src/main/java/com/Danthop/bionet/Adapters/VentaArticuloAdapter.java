package com.Danthop.bionet.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.R;
import com.Danthop.bionet.Tables.SortableVentaArticulos;
import com.Danthop.bionet.Views.ButtonCantidad;
import com.Danthop.bionet.model.ArticuloApartadoModel;
import com.Danthop.bionet.model.ArticuloModel;
import com.Danthop.bionet.model.Impuestos;
import com.Danthop.bionet.model.OrdenEspecialArticuloModel;
import com.Danthop.bionet.model.TicketModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class VentaArticuloAdapter extends LongPressAwareTableDataAdapter<ArticuloModel> {

    int TEXT_SIZE = 12;
    private SortableVentaArticulos tabla_venta_articulos;
    private List<ArticuloModel> Articulos;
    private static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();
    private String Usu_id;
    private TicketModel TicketVenta;

    private TextView total;
    private TextView descuento;
    private TextView impuesto;
    private TextView subtotal;
    private CarouselView carouselView;
    private ArrayList<String> Imagenes;

    private List<Impuestos> ImpuestosDeArticuloApartado;
    private List<ArticuloApartadoModel> ListaDeArticulosApartados;

    private List<Impuestos> ImpuestosDeArticuloOrdenado;
    private List<OrdenEspecialArticuloModel> ListaDeArticulosOrdenados;

    private ArrayList<String> ArticulosConExistencias = new ArrayList<>();

    private Button OrdenarButton;
    private Button ApartarButton;
    private Button FinalizarButton;

    private RecyclerView RecyclerImpuesto;
    private RecyclerView.LayoutManager mLayoutManager;
    private NumberFormat formatter;
    private ProgressDialog progressDialog;

    private LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);


    public VentaArticuloAdapter(final Context context, final List<ArticuloModel> data, final SortableVentaArticulos tableView, TicketModel Ticket, String usu_id,
                                TextView Total, TextView Descuento, TextView Impuesto, TextView Subtotal,
                                CarouselView Carousel, ArrayList<String> imagenes,
                                List<Impuestos> impuestosDeArticuloApartado,
                                List<ArticuloApartadoModel> listaDeArticulosApartados,
                                List<Impuestos> impuestosDeArticuloOrdenado,
                                List<OrdenEspecialArticuloModel> listaDeArticulosOrdenados,
                                Button ordenarButton,
                                Button apartarButton,
                                Button finalizarButton,
                                RecyclerView reciclerImpuesto) {
        super(context, data, tableView);
        Articulos = data;
        tabla_venta_articulos = tableView;
        Usu_id = usu_id;
        TicketVenta=Ticket;
        total=Total;
        descuento=Descuento;
        impuesto=Impuesto;
        subtotal=Subtotal;
        carouselView = Carousel;
        Imagenes = imagenes;
        ImpuestosDeArticuloApartado= impuestosDeArticuloApartado;
        ListaDeArticulosApartados= listaDeArticulosApartados;
        ImpuestosDeArticuloOrdenado = impuestosDeArticuloOrdenado;
        ListaDeArticulosOrdenados = listaDeArticulosOrdenados;
        OrdenarButton = ordenarButton;
        ApartarButton = apartarButton;
        FinalizarButton = finalizarButton;
        RecyclerImpuesto = reciclerImpuesto;
        formatter = NumberFormat.getCurrencyInstance();
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Espere un momento por favor");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        ArticuloModel Articulo = getRowData(rowIndex);
        View renderedView = null;

        /*View childView = parentView.getChildAt(rowIndex);
        if(Articulo.getArticulo_articulo_exi_id().equals(""))
        {
            childView.setBackgroundColor(getResources().getColor(R.color.amarillo));
        }
        else
        {
            childView.setBackgroundColor(getResources().getColor(R.color.amarillo));
        }*/

        switch (columnIndex) {
            case 0:

                renderedView = renderNombre(Articulo);
                break;

            case 1:

                renderedView = renderSKU(Articulo);
                break;

            case 2:

                renderedView = renderCantidad(Articulo);
                break;

            case 3:

                renderedView = renderUM(Articulo);
                break;

            case 4:

                renderedView = renderPrecio(Articulo);
                break;

            case 5:

                renderedView = renderDescuento(Articulo);
                break;

            case 6:
                renderedView = renderImporte(Articulo);
                break;

            case 7:

                renderedView = renderEliminar(Articulo);
                break;

        }

        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final ArticuloModel Articulo = getRowData(rowIndex);
        View renderedView = null;

        return renderedView;
    }

    private View renderEditableClienteName(final ArticuloModel Articulo) {
        final TextView v = new TextView(getContext());
        v.setText(Articulo.getarticulo_Nombre());
        return v;
    }

    private View renderEliminar(final ArticuloModel articulo)
    {
        final Button delete = new Button(getContext());
        delete.setPadding(5,2,5,2);
        delete.setBackgroundColor(getResources().getColor(R.color.white));
        delete.setDrawingCacheBackgroundColor(getResources().getColor(R.color.white));
        delete.setBackgroundResource(R.drawable.ic_delete);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(5, 20, 5, 5);
        delete.setLayoutParams(params);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EliminarArticulo(articulo.getArticulo_tarID());
            }
        });
        return delete;

    }

    private View renderNombre(final ArticuloModel articulo) {
        return renderString(articulo.getarticulo_Nombre());
    }

    private View renderSKU(final ArticuloModel articulo) {
        return renderString(articulo.getArticulo_sku());

    }
    private View renderCantidad(final ArticuloModel articulo) {
        final ButtonCantidad cantidad = new ButtonCantidad(getContext());
        cantidad.setNumber(articulo.getArticulo_cantidad());
        cantidad.setPadding(5,2,5,2);

        cantidad.setOnSumarCantidad(new ButtonCantidad.OnCustomEventListener() {
            @Override
            public void onEvent() {
                progressDialog.show();
                modificaCantidad(articulo,cantidad.getCantidad());
            }
        });

        cantidad.setOnRestarCantidad(new ButtonCantidad.OnCustomEventListener() {
            @Override
            public void onEvent() {
                progressDialog.show();
                modificaCantidad(articulo,cantidad.getCantidad());
            }
        });

        cantidad.setOnCambiarAMano(new ButtonCantidad.OnCustomEventListener() {
            @Override
            public void onEvent() {
                progressDialog.show();
                modificaCantidad(articulo,cantidad.getCantidad());
            }
        });

        return cantidad;
    }

    private View renderUM(final ArticuloModel articulo) {
        return renderString(articulo.getGetArticulo_UM());
    }

    private View renderDescuento(final ArticuloModel articulo) {
        double Descuento = Double.parseDouble( articulo.getArticulo_descuento() );


        final TextView textView = new TextView( getContext() );
        textView.setText( formatter.format( Descuento ) );
        textView.setPadding(5,2,5,2);
        textView.setTextSize( TEXT_SIZE );
        textView.setGravity(View.TEXT_ALIGNMENT_TEXT_END);
        textView.setGravity(View.TEXT_ALIGNMENT_VIEW_END);

        return textView;
    }
    private View renderImporte(final ArticuloModel articulo) {
        double Importe = Double.parseDouble( articulo.getArticulo_importe() );

        formatter = NumberFormat.getCurrencyInstance();

        final TextView textView = new TextView( getContext() );
        textView.setText( formatter.format( Importe ) );
        textView.setPadding(5,2,5,2);
        textView.setGravity(View.TEXT_ALIGNMENT_TEXT_END);
        textView.setGravity(View.TEXT_ALIGNMENT_VIEW_END);
        textView.setTextSize( TEXT_SIZE );

        return textView;
    }

   /* private View renderPromocion(final ArticuloModel articulo) {
        return renderString(articulo.getarticulo_Nombre());
    }*/


    private View renderPrecio(final ArticuloModel articulo) {
        double Precio = Double.parseDouble( articulo.getarticulo_Precio() );

        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        final TextView textView = new TextView( getContext() );
        textView.setText( formatter.format( Precio ) );
        textView.setTextSize( TEXT_SIZE );
        textView.setGravity(View.TEXT_ALIGNMENT_TEXT_END);
        textView.setGravity(View.TEXT_ALIGNMENT_VIEW_END);
        return textView;

    }

    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(5,2,5,2);
        textView.setTextSize(TEXT_SIZE);
        textView.setGravity(View.TEXT_ALIGNMENT_TEXT_END);
        textView.setGravity(View.TEXT_ALIGNMENT_VIEW_END);
        return textView;
    }

    private static class OrdenNameUpdater implements TextWatcher {

        private ArticuloModel ordenToUpdate;

        public OrdenNameUpdater(ArticuloModel ordenToUpdate) {
            this.ordenToUpdate = ordenToUpdate;
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
            ordenToUpdate.getarticulo_Nombre();
        }
    }

    private void modificaCantidad(ArticuloModel articulo, String cantidad) {
        Articulos.clear();
        ImpuestosDeArticuloApartado.clear();
        ListaDeArticulosApartados.clear();
        ListaDeArticulosOrdenados.clear();

        int CantidadEnEntero = Integer.parseInt(cantidad);

        if (CantidadEnEntero != 0) {
            JSONObject request = new JSONObject();
            try {
                request.put("usu_id", Usu_id);
                request.put("esApp", "1");
                request.put("tar_id", articulo.getArticulo_tarID());
                request.put("cantidad", cantidad);

            } catch (Exception e) {
                e.printStackTrace();
            }


            String ApiPath = getContext().getString(R.string.Url) + "/api/ventas/tickets/update-cantidad-articulo";

            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath, request, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    JSONObject Respuesta = null;
                    JSONObject RespuestaNodoTicket = null;

                    try {

                        int status = Integer.parseInt(response.getString("estatus"));
                        String Mensaje = response.getString("mensaje");

                        if (status == 1) {
                            Respuesta = response.getJSONObject("resultado");
                            RespuestaNodoTicket = Respuesta.getJSONObject("aTicket");

                            float Subtotal = 0;
                            float ImpuestoTotal = 0;
                            JSONArray tic_impuestos = RespuestaNodoTicket.getJSONArray("tic_impuestos");
                            List<Impuestos> ImpuestosList = new ArrayList<>();
                            for (int f = 0; f < tic_impuestos.length(); f++) {
                                JSONObject nodo_impuestos = tic_impuestos.getJSONObject(f);
                                String ValorImpuesto = nodo_impuestos.getString("valor");
                                double ValorEnPesos = Double.parseDouble((ValorImpuesto));
                                if (ValorEnPesos < 0) {
                                    ValorEnPesos = ValorEnPesos * -1;
                                }
                                ValorImpuesto = String.valueOf(formatter.format(ValorEnPesos));
                                Impuestos impuesto = new Impuestos("", ValorImpuesto);
                                impuesto.setNombreImpuesto(nodo_impuestos.getString("nombre"));
                                ImpuestosList.add(impuesto);
                            }

                            ImpuestoAdapter mAdapter = new ImpuestoAdapter(ImpuestosList);
                            RecyclerImpuesto.setHasFixedSize(true);
                            mLayoutManager = new LinearLayoutManager(getContext());
                            RecyclerImpuesto.setLayoutManager(mLayoutManager);
                            RecyclerImpuesto.setAdapter(mAdapter);

                            float DescuentoTotal = Float.parseFloat(RespuestaNodoTicket.getString("tic_importe_descuentos"));
                            float PrecioTotal = Float.parseFloat(RespuestaNodoTicket.getString("tic_importe_total"));


                            JSONArray NodoArticuloTicket = Respuesta.getJSONArray("aDetalleTicket");
                            for (int j = 0; j < NodoArticuloTicket.length(); j++) {
                                JSONObject nodo = NodoArticuloTicket.getJSONObject(j);

                                float numero_de_productos = Float.parseFloat((nodo.getString("tar_cantidad")));

                                float PrecioSubTotalProducto = 0;


                                //Sumar Subtotal
                                float SubTot = Float.parseFloat(nodo.getString("art_precio_bruto"));
                                PrecioSubTotalProducto = PrecioSubTotalProducto + SubTot;
                                PrecioSubTotalProducto = PrecioSubTotalProducto * numero_de_productos;

                                Subtotal = Subtotal + PrecioSubTotalProducto;

                                String AplicaApartados = nodo.getString("art_aplica_apartados");
                                String TieneExistencia = nodo.getString("art_id_existencia");

                                if (AplicaApartados.equals("true")) {
                                    String ArticuloCantidad = nodo.getString("tar_cantidad");
                                    String NombreArticuloApartado = nodo.getString("tar_nombre_articulo");
                                    JSONObject NodoIDApartado = nodo.getJSONObject("tar_id_articulo");
                                    String ArticuloIDApartado = NodoIDApartado.getString("uuid");
                                    JSONObject NodoIDApartadoVariante = nodo.getJSONObject("tar_id_variante");
                                    String ArticuloIDApartadoVariante = NodoIDApartadoVariante.getString("uuid");
                                    String ArticuloIDApartadoModificador = nodo.getString("tar_id_modificador");
                                    String ArticuloApartadoImportePagado = nodo.getString("tar_importe_pagado");
                                    String ArticuloIDApartadoExistencia = nodo.getString("tar_id_existencia");
                                    String ArticuloApartadoAplicaDevolucion = nodo.getString("tar_aplica_para_devolucion");
                                    String ArticuloApartadoImporteDescuento = nodo.getString("tar_importe_descuento");
                                    String ArticuloApartadoTotal = nodo.getString("tar_importe_total");
                                    JSONObject nodoImpuestos = nodo.getJSONObject("tar_impuestos");
                                    Iterator keys = nodoImpuestos.keys();
                                    while (keys.hasNext()) {
                                        Object key = keys.next();
                                        String value = nodoImpuestos.getString((String) key);
                                        Impuestos Impuesto = new Impuestos((String) key, value);
                                        ImpuestosDeArticuloApartado.add(Impuesto);
                                    }
                                    String ArticuloApartadoPorcentajeDescuento = nodo.getString("tar_porcentaje_descuento");
                                    String ArticuloApartadoPrecio = nodo.getString("tar_precio_articulo");


                                    float importePagado = Float.parseFloat((ArticuloApartadoImportePagado));
                                    float importeTotal = Float.parseFloat((ArticuloApartadoTotal));
                                    float importeRestante = importeTotal - importePagado;

                                    ArticuloApartadoModel ArticuloApartado = new ArticuloApartadoModel(
                                            ArticuloCantidad,
                                            ArticuloIDApartado,
                                            ArticuloIDApartadoVariante,
                                            ArticuloIDApartadoModificador,
                                            ArticuloApartadoImportePagado,
                                            String.valueOf(importeRestante),
                                            NombreArticuloApartado,
                                            ArticuloIDApartadoExistencia,
                                            ArticuloApartadoAplicaDevolucion,
                                            ArticuloApartadoImporteDescuento,
                                            ArticuloApartadoTotal,
                                            ImpuestosDeArticuloApartado,
                                            ArticuloApartadoPorcentajeDescuento,
                                            ArticuloApartadoPrecio);
                                    ListaDeArticulosApartados.add(ArticuloApartado);
                                }

                                //ORDENES-------------------------------------------------------
                                String AplicaOrdenes = nodo.getString("art_aplica_ordenes_especiales");
                                if (AplicaOrdenes.equals("true") && (TieneExistencia.equals(""))) {
                                    String ArticuloCantidad = nodo.getString("tar_cantidad");
                                    String NombreArticuloApartado = nodo.getString("tar_nombre_articulo");
                                    JSONObject NodoIDApartado = nodo.getJSONObject("tar_id_articulo");
                                    String ArticuloIDApartado = NodoIDApartado.getString("uuid");
                                    JSONObject NodoIDApartadoVariante = nodo.getJSONObject("tar_id_variante");
                                    String ArticuloIDApartadoVariante = NodoIDApartadoVariante.getString("uuid");
                                    String ArticuloIDApartadoModificador = nodo.getString("tar_id_modificador");
                                    String ArticuloApartadoImportePagado = nodo.getString("tar_importe_pagado");
                                    String ArticuloIDApartadoExistencia = nodo.getString("art_id_existencia");
                                    String ArticuloApartadoAplicaDevolucion = nodo.getString("tar_aplica_para_devolucion");
                                    String ArticuloApartadoImporteDescuento = nodo.getString("tar_importe_descuento");
                                    String ArticuloApartadoTotal = nodo.getString("tar_importe_total");
                                    JSONObject nodoImpuestos = nodo.getJSONObject("tar_impuestos");
                                    Iterator keys = nodoImpuestos.keys();
                                    while (keys.hasNext()) {
                                        Object key = keys.next();
                                        String value = nodoImpuestos.getString((String) key);
                                        Impuestos Impuesto = new Impuestos((String) key, value);
                                        ImpuestosDeArticuloOrdenado.add(Impuesto);
                                    }
                                    String ArticuloApartadoPorcentajeDescuento = nodo.getString("tar_porcentaje_descuento");
                                    String ArticuloApartadoPrecio = nodo.getString("tar_precio_articulo");


                                    float importePagado = Float.parseFloat((ArticuloApartadoImportePagado));
                                    float importeTotal = Float.parseFloat((ArticuloApartadoTotal));
                                    float importeRestante = importeTotal - importePagado;

                                    OrdenEspecialArticuloModel ArticuloOrdenado = new OrdenEspecialArticuloModel(
                                            ArticuloCantidad,
                                            ArticuloIDApartado,
                                            ArticuloIDApartadoVariante,
                                            ArticuloIDApartadoModificador,
                                            ArticuloApartadoImportePagado,
                                            String.valueOf(importeRestante),
                                            NombreArticuloApartado,
                                            ArticuloApartadoAplicaDevolucion,
                                            ArticuloApartadoImporteDescuento,
                                            ArticuloApartadoTotal,
                                            ImpuestosDeArticuloOrdenado,
                                            ArticuloApartadoPorcentajeDescuento,
                                            ArticuloApartadoPrecio);
                                    ListaDeArticulosOrdenados.add(ArticuloOrdenado);
                                } else {

                                }
                            }


                            //Se modifican los datos del ticket de venta
                            TicketVenta.setTic_importe_descuentos(String.valueOf(DescuentoTotal));
                            TicketVenta.setTic_importe_total(String.valueOf(PrecioTotal));
                            TicketVenta.setTic_impuestos(String.valueOf(ImpuestoTotal));


                            NumberFormat formatter = NumberFormat.getCurrencyInstance();

                            double Price = Double.parseDouble(TicketVenta.getTic_importe_total());
                            total.setText(formatter.format(Price));

                            double descu = Double.parseDouble(TicketVenta.getTic_importe_descuentos());
                            descuento.setText(formatter.format(descu));

                            double sub = Double.parseDouble(String.valueOf(Subtotal));
                            subtotal.setText(formatter.format(sub));

                            JSONArray NodoTicketArticulos = Respuesta.getJSONArray("aDetalleTicket");
                            for (int x = 0; x < NodoTicketArticulos.length(); x++) {
                                JSONObject elemento = NodoTicketArticulos.getJSONObject(x);
                                String NombreArticulo = elemento.getString("tar_nombre_articulo");
                                JSONObject NodoTarID = elemento.getJSONObject("tar_id");
                                String tar_id = NodoTarID.getString("uuid");

                                String SKUArticulo = elemento.getString("art_sku");
                                String cantidad = elemento.getString("tar_cantidad");
                                String precio = elemento.getString("tar_precio_articulo");
                                String descuento = elemento.getString("art_importe_descuento");
                                String importe = elemento.getString("tar_importe_total");
                                String existencia = elemento.getString("art_id_existencia");
                                String unidad_medida = elemento.getString("art_um_simbologia");

                                JSONArray RespuestaImagenes = elemento.getJSONArray("art_imagenes");
                                for (int z = 0; z < RespuestaImagenes.length(); z++) {
                                    String RutaImagen = getContext().getString(R.string.Url) + RespuestaImagenes.getString(z);
                                    Imagenes.add(RutaImagen);
                                }
                                final ArticuloModel articulo = new ArticuloModel("",
                                        NombreArticulo,
                                        "",
                                        precio,
                                        "",
                                        "",
                                        SKUArticulo,
                                        "",
                                        cantidad,
                                        tar_id,
                                        descuento,
                                        "",
                                        importe, "", "", existencia
                                );
                                articulo.setGetArticulo_UM(unidad_medida);
                                Articulos.add(articulo);

                            }

                            final VentaArticuloAdapter articuloAdapter = new VentaArticuloAdapter(getContext(), Articulos, tabla_venta_articulos, TicketVenta, Usu_id,
                                    total, descuento, impuesto, subtotal,
                                    carouselView, Imagenes, ImpuestosDeArticuloApartado, ListaDeArticulosApartados, ImpuestosDeArticuloOrdenado, ListaDeArticulosOrdenados, OrdenarButton, ApartarButton, FinalizarButton,
                                    RecyclerImpuesto);
                            articuloAdapter.notifyDataSetChanged();
                            tabla_venta_articulos.setDataAdapter(articuloAdapter);
                            LoadImages();
                            progressDialog.dismiss();

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
        }else
        {
            EliminarArticulo(articulo.getArticulo_tarID());
        }
    }

    private void EliminarArticulo(String taride)
    {
        progressDialog.show();
        ArticulosConExistencias.clear();
        ImpuestosDeArticuloApartado.clear();
        ListaDeArticulosApartados.clear();
        ListaDeArticulosOrdenados.clear();
        Articulos.clear();
        Imagenes.clear();
        JSONObject request = new JSONObject();
        try
        {
            request.put("usu_id", Usu_id);
            request.put("esApp", "1");
            request.put("tar_id",taride);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


        String ApiPath = getContext().getString(R.string.Url)+"/api/ventas/tickets/destroy-articulo";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject Respuesta = null;
                JSONObject RespuestaNodoTicket= null;
                JSONObject TicketID=null;
                JSONArray NodoTicketArticulos=null;

                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1)
                    {
                        Respuesta = response.getJSONObject("resultado");
                        RespuestaNodoTicket = Respuesta.getJSONObject("aTicket");
                        Imagenes.clear();

                        float ImpuestoTotal = 0;
                        float Subtotal = 0;
                        JSONArray tic_impuestos = RespuestaNodoTicket.getJSONArray("tic_impuestos");
                        List<Impuestos> ImpuestosList = new ArrayList<>();
                        for (int f = 0; f < tic_impuestos.length(); f++) {
                            JSONObject nodo_impuestos = tic_impuestos.getJSONObject(f);
                            String ValorImpuesto = nodo_impuestos.getString("valor");
                            double ValorEnPesos = Double.parseDouble(ValorImpuesto);
                            if(ValorEnPesos<0)
                            {
                                ValorEnPesos=ValorEnPesos*-1;
                            }
                            ValorImpuesto= String.valueOf(formatter.format(ValorEnPesos));
                            Impuestos impuesto = new Impuestos("",ValorImpuesto);
                            impuesto.setNombreImpuesto(nodo_impuestos.getString("nombre"));
                            ImpuestosList.add(impuesto);
                        }

                        ImpuestoAdapter mAdapter = new ImpuestoAdapter(ImpuestosList);
                        RecyclerImpuesto.setHasFixedSize(true);
                        mLayoutManager = new LinearLayoutManager(getContext());
                        RecyclerImpuesto.setLayoutManager(mLayoutManager);
                        RecyclerImpuesto.setAdapter(mAdapter);

                        float DescuentoTotal = Float.parseFloat(RespuestaNodoTicket.getString("tic_importe_descuentos"));
                        float PrecioTotal = Float.parseFloat(RespuestaNodoTicket.getString("tic_importe_total"));


                        JSONArray NodoArticuloTicket = Respuesta.getJSONArray("aDetalleTicket");
                        for(int j = 0; j < NodoArticuloTicket.length(); j++) {
                            JSONObject nodo = NodoArticuloTicket.getJSONObject(j);

                            float numero_de_productos = Float.parseFloat((nodo.getString("tar_cantidad")));

                            float PrecioSubTotalProducto=0;


                            //Sumar Subtotal
                            float SubTot = Float.parseFloat(nodo.getString("art_precio_bruto"));
                            PrecioSubTotalProducto = PrecioSubTotalProducto + SubTot;
                            PrecioSubTotalProducto = PrecioSubTotalProducto * numero_de_productos;

                            Subtotal = Subtotal + PrecioSubTotalProducto;

                            String AplicaApartados = nodo.getString("art_aplica_apartados");
                            String TieneExistencia = nodo.getString("art_id_existencia");

                            if (AplicaApartados.equals("true")) {
                                String ArticuloCantidad = nodo.getString("tar_cantidad");
                                String NombreArticuloApartado = nodo.getString("tar_nombre_articulo");
                                JSONObject NodoIDApartado = nodo.getJSONObject("tar_id_articulo");
                                String ArticuloIDApartado = NodoIDApartado.getString("uuid");
                                JSONObject NodoIDApartadoVariante = nodo.getJSONObject("tar_id_variante");
                                String ArticuloIDApartadoVariante = NodoIDApartadoVariante.getString("uuid");
                                String ArticuloIDApartadoModificador = nodo.getString("tar_id_modificador");
                                String ArticuloApartadoImportePagado = nodo.getString("tar_importe_pagado");
                                String ArticuloIDApartadoExistencia = nodo.getString("tar_id_existencia");
                                String ArticuloApartadoAplicaDevolucion = nodo.getString("tar_aplica_para_devolucion");
                                String ArticuloApartadoImporteDescuento = nodo.getString("tar_importe_descuento");
                                String ArticuloApartadoTotal = nodo.getString("tar_importe_total");
                                JSONObject nodoImpuestos = nodo.getJSONObject("tar_impuestos");
                                Iterator keys = nodoImpuestos.keys();
                                while (keys.hasNext()) {
                                    Object key = keys.next();
                                    String value = nodoImpuestos.getString((String) key);
                                    Impuestos Impuesto = new Impuestos((String) key, value);
                                    ImpuestosDeArticuloApartado.add(Impuesto);
                                }
                                String ArticuloApartadoPorcentajeDescuento = nodo.getString("tar_porcentaje_descuento");
                                String ArticuloApartadoPrecio = nodo.getString("tar_precio_articulo");


                                float importePagado = Float.parseFloat((ArticuloApartadoImportePagado));
                                float importeTotal = Float.parseFloat((ArticuloApartadoTotal));
                                float importeRestante = importeTotal - importePagado;

                                ArticuloApartadoModel ArticuloApartado = new ArticuloApartadoModel(
                                        ArticuloCantidad,
                                        ArticuloIDApartado,
                                        ArticuloIDApartadoVariante,
                                        ArticuloIDApartadoModificador,
                                        ArticuloApartadoImportePagado,
                                        String.valueOf(importeRestante),
                                        NombreArticuloApartado,
                                        ArticuloIDApartadoExistencia,
                                        ArticuloApartadoAplicaDevolucion,
                                        ArticuloApartadoImporteDescuento,
                                        ArticuloApartadoTotal,
                                        ImpuestosDeArticuloApartado,
                                        ArticuloApartadoPorcentajeDescuento,
                                        ArticuloApartadoPrecio);
                                ListaDeArticulosApartados.add(ArticuloApartado);
                            }

                            //ORDENES-------------------------------------------------------
                            String AplicaOrdenes = nodo.getString("art_aplica_ordenes_especiales");
                            if (AplicaOrdenes.equals("true")&&(TieneExistencia.equals(""))) {
                                String ArticuloCantidad = nodo.getString("tar_cantidad");
                                String NombreArticuloApartado = nodo.getString("tar_nombre_articulo");
                                JSONObject NodoIDApartado = nodo.getJSONObject("tar_id_articulo");
                                String ArticuloIDApartado = NodoIDApartado.getString("uuid");
                                JSONObject NodoIDApartadoVariante = nodo.getJSONObject("tar_id_variante");
                                String ArticuloIDApartadoVariante = NodoIDApartadoVariante.getString("uuid");
                                String ArticuloIDApartadoModificador = nodo.getString("tar_id_modificador");
                                String ArticuloApartadoImportePagado = nodo.getString("tar_importe_pagado");
                                String ArticuloIDApartadoExistencia = nodo.getString("art_id_existencia");
                                String ArticuloApartadoAplicaDevolucion = nodo.getString("tar_aplica_para_devolucion");
                                String ArticuloApartadoImporteDescuento = nodo.getString("tar_importe_descuento");
                                String ArticuloApartadoTotal = nodo.getString("tar_importe_total");
                                JSONObject nodoImpuestos = nodo.getJSONObject("tar_impuestos");
                                Iterator keys = nodoImpuestos.keys();
                                while (keys.hasNext()) {
                                    Object key = keys.next();
                                    String value = nodoImpuestos.getString((String) key);
                                    Impuestos Impuesto = new Impuestos((String) key, value);
                                    ImpuestosDeArticuloOrdenado.add(Impuesto);
                                }
                                String ArticuloApartadoPorcentajeDescuento = nodo.getString("tar_porcentaje_descuento");
                                String ArticuloApartadoPrecio = nodo.getString("tar_precio_articulo");


                                float importePagado = Float.parseFloat((ArticuloApartadoImportePagado));
                                float importeTotal = Float.parseFloat((ArticuloApartadoTotal));
                                float importeRestante = importeTotal - importePagado;

                                OrdenEspecialArticuloModel ArticuloOrdenado = new OrdenEspecialArticuloModel(
                                        ArticuloCantidad,
                                        ArticuloIDApartado,
                                        ArticuloIDApartadoVariante,
                                        ArticuloIDApartadoModificador,
                                        ArticuloApartadoImportePagado,
                                        String.valueOf(importeRestante),
                                        NombreArticuloApartado,
                                        ArticuloApartadoAplicaDevolucion,
                                        ArticuloApartadoImporteDescuento,
                                        ArticuloApartadoTotal,
                                        ImpuestosDeArticuloOrdenado,
                                        ArticuloApartadoPorcentajeDescuento,
                                        ArticuloApartadoPrecio);
                                ListaDeArticulosOrdenados.add(ArticuloOrdenado);
                            }
                            else {

                            }
                        }





                        //Se modifican los datos del ticket de venta
                        TicketVenta.setTic_importe_descuentos(String.valueOf(DescuentoTotal));
                        TicketVenta.setTic_importe_total(String.valueOf(PrecioTotal));
                        TicketVenta.setTic_impuestos(String.valueOf(ImpuestoTotal));



                        NumberFormat formatter = NumberFormat.getCurrencyInstance();

                        double Price = Double.parseDouble( TicketVenta.getTic_importe_total() );
                        total.setText( formatter.format( Price ) );

                        double descu = Double.parseDouble( TicketVenta.getTic_importe_descuentos() );
                        descuento.setText( formatter.format( descu ) );

                        double sub = Double.parseDouble(String.valueOf(Subtotal));
                        subtotal.setText( formatter.format( sub ) );


                        NodoTicketArticulos = Respuesta.getJSONArray("aDetalleTicket");
                        for (int x = 0; x < NodoTicketArticulos.length(); x++)
                        {
                            JSONObject elemento = NodoTicketArticulos.getJSONObject(x);
                            String NombreArticulo = elemento.getString("tar_nombre_articulo");
                            JSONObject NodoTarID = elemento.getJSONObject("tar_id");
                            String tar_id = NodoTarID.getString("uuid");

                            String SKUArticulo = elemento.getString("art_sku");
                            String cantidad = elemento.getString("tar_cantidad");
                            String precio = elemento.getString("tar_precio_articulo");
                            String descuento = elemento.getString("art_importe_descuento");
                            String importe = elemento.getString("tar_importe_total");
                            String existencia = elemento.getString("art_id_existencia");
                            String unidad_medida = elemento.getString("art_um_simbologia");

                            JSONArray RespuestaImagenes = elemento.getJSONArray("art_imagenes");
                            for (int z = 0; z < RespuestaImagenes.length(); z++) {
                                String RutaImagen = getContext().getString(R.string.Url) + RespuestaImagenes.getString(z);
                                Imagenes.add(RutaImagen);
                            }
                            final ArticuloModel articulo = new ArticuloModel("",
                                    NombreArticulo,
                                    "",
                                    precio,
                                    "",
                                    "",
                                    SKUArticulo,
                                    "",
                                    cantidad,
                                    tar_id,
                                    descuento,
                                    "",
                                    importe,"","",existencia
                            );
                            articulo.setGetArticulo_UM(unidad_medida);
                            Articulos.add(articulo);

                        }

                        final VentaArticuloAdapter articuloAdapter = new VentaArticuloAdapter(getContext(), Articulos, tabla_venta_articulos, TicketVenta, Usu_id,
                                total, descuento, impuesto, subtotal,
                                carouselView, Imagenes, ImpuestosDeArticuloApartado,ListaDeArticulosApartados,ImpuestosDeArticuloOrdenado,ListaDeArticulosOrdenados,
                                OrdenarButton,ApartarButton,FinalizarButton,
                                RecyclerImpuesto);
                        articuloAdapter.notifyDataSetChanged();
                        tabla_venta_articulos.setDataAdapter(articuloAdapter);
                        LoadImages();

                        if(ListaDeArticulosOrdenados.isEmpty())
                        {
                            OrdenarButton.setVisibility(View.INVISIBLE);
                        }
                        else
                        {
                            OrdenarButton.setVisibility(View.VISIBLE);
                        }

                        if(ListaDeArticulosApartados.isEmpty())
                        {
                            ApartarButton.setVisibility(View.INVISIBLE);
                        }
                        else
                        {
                            ApartarButton.setVisibility(View.VISIBLE);
                        }

                        for(int k=0; k<Articulos.size();k++)
                        {
                            if(Articulos.get(k).getArticulo_articulo_exi_id().equals("")){

                            }
                            else
                            {
                                ArticulosConExistencias.add(Articulos.get(k).getarticulo_Nombre());
                            }
                        }

                        if(ArticulosConExistencias.isEmpty())
                        {
                            FinalizarButton.setVisibility(View.INVISIBLE);
                        }
                        else
                        {
                            FinalizarButton.setVisibility(View.VISIBLE);
                        }

                        progressDialog.dismiss();

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
        postRequest.setShouldCache(false);

        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);
        LoadImages();
    }

    public void LoadImages() {

        if(Imagenes.isEmpty())
        {
            carouselView.setVisibility(View.INVISIBLE);
        }else{
            carouselView.setVisibility(View.VISIBLE);
            ViewListener viewListener = new ViewListener() {

                @Override
                public View setViewForPosition(int position) {
                    View customView = getLayoutInflater().inflate(R.layout.image_view, null);
                    ImageView myImageView = customView.findViewById(R.id.imageViewParaCarousel);
                    Picasso.with(getContext()).load(Imagenes.get(position)).into(myImageView);

                    return customView;
                }
            };

            carouselView.setViewListener(viewListener);
            carouselView.setPageCount(Imagenes.size());
        }


    }
}
