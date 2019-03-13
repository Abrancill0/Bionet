package com.Danthop.bionet.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.R;
import com.Danthop.bionet.Tables.SortableOrdenEcommerceTable;
import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.Ecommerce_orden_Model;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.request.JsonObjectRequest;
import com.google.android.gms.common.api.Api;

import java.text.NumberFormat;
import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;

import com.Danthop.bionet.Tables.SortableOrdenEcommerceTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class OrdenEcommerceAdapter extends LongPressAwareTableDataAdapter<Ecommerce_orden_Model> {

    int TEXT_SIZE = 12;
    public Dialog pop_up1;
    private static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();

    public OrdenEcommerceAdapter(final Context context, final List<Ecommerce_orden_Model> data, final SortableOrdenEcommerceTable tableView) {
        super( context, data, tableView );
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        Ecommerce_orden_Model orden = getRowData( rowIndex );
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderClienteName( orden );
                break;
            case 1:
                renderedView = renderOrdenArticulo( orden );
                break;
            case 2:
                renderedView = renderOrdenCantidad( orden );
                break;
            case 3:
                renderedView = renderOrdenEnvio( orden );
                break;
            case 4:
                renderedView = renderOrdenImporte( orden );
                break;
            case 5:
                renderedView = renderOrdenEstatus( orden );
                break;
            case 6:
                renderedView = renderButton1( orden );
                break;
            case 7:
                renderedView = renderButton2( orden );
                break;
        }

        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final Ecommerce_orden_Model orden = getRowData( rowIndex );
        View renderedView = null;

        switch (columnIndex) {
            case 1:
                renderedView = renderEditableClienteName( orden );
                break;
            default:
                renderedView = getDefaultCellView( rowIndex, columnIndex, parentView );
        }

        return renderedView;
    }

    private View renderEditableClienteName(final Ecommerce_orden_Model orden) {
        final EditText editText = new EditText( getContext() );
        editText.setText( orden.getCliente() );
        editText.setPadding( 20, 10, 20, 10 );
        editText.setTextSize( TEXT_SIZE );
        editText.setSingleLine();
        editText.addTextChangedListener( new OrdenNameUpdater( orden ) );
        return editText;
    }

    private View renderClienteName(final Ecommerce_orden_Model orden) {
        return renderString( orden.getCliente() );
    }

    private View renderOrdenArticulo(final Ecommerce_orden_Model orden) {
        return renderString( orden.getArticulo() );
    }

    private View renderOrdenCantidad(final Ecommerce_orden_Model orden) {
        return renderString( orden.getCantidad() );
    }

    private View renderOrdenEnvio(final Ecommerce_orden_Model orden) {

        double Envio = Double.parseDouble( orden.getEnvio() );

        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        final TextView textView = new TextView( getContext() );
        textView.setText( formatter.format( Envio ) );
        textView.setPadding( 20, 10, 20, 10 );
        textView.setTextSize( TEXT_SIZE );

        return textView;

    }

    private View renderOrdenImporte(final Ecommerce_orden_Model orden) {
        double Importe = Double.parseDouble( orden.getImporte() );

        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        final TextView textView = new TextView( getContext() );
        textView.setText( formatter.format( Importe ) );
        textView.setPadding( 20, 10, 20, 10 );
        textView.setTextSize( TEXT_SIZE );

        return textView;
    }

    private View renderOrdenEstatus(final Ecommerce_orden_Model orden) {
        return renderString( orden.getEstatus() );
    }

    private View renderButton1(final Ecommerce_orden_Model orden) {
        return ButtonUno( orden );
    }

    private View renderButton2(final Ecommerce_orden_Model orden) {


        return ButtonDos( orden );
    }

    private View renderString(final String value) {
        final TextView textView = new TextView( getContext() );
        textView.setText( value );
        textView.setPadding( 20, 10, 20, 10 );
        textView.setTextSize( TEXT_SIZE );
        return textView;
    }

    private View ButtonUno(final Ecommerce_orden_Model orden) {
        final Button btn = new Button( getContext() );
        btn.setText( "Ver Ficha" );
        btn.setPadding( 20, 10, 20, 10 );
        btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pop_up1 = new Dialog( getContext() );
                pop_up1.setContentView( R.layout.pop_up_ecommerce_detalle_orden );
                pop_up1.show();

                TextView Orden = pop_up1.findViewById( R.id.text_ordenname );
                TextView Cliente = pop_up1.findViewById( R.id.text_clientename );
                TextView TipoPago = pop_up1.findViewById( R.id.text_tipo_pago );
                TextView Envio = pop_up1.findViewById( R.id.text_costo_envio );
                TextView Importe = pop_up1.findViewById( R.id.text_importe );
                TextView OrdenEstado = pop_up1.findViewById( R.id.text_estado );
                TextView Fecha = pop_up1.findViewById( R.id.text_fecha );

                double Envioformat = Double.parseDouble( orden.getEnvio() );
                NumberFormat formatter = NumberFormat.getCurrencyInstance();

                double importeformat = Double.parseDouble( orden.getImporte() );
                NumberFormat formatter2 = NumberFormat.getCurrencyInstance();

                Orden.setText( orden.getArticulo() );
                Cliente.setText( orden.getCliente() );
                TipoPago.setText( orden.getTipoPago() );
                Envio.setText( formatter.format( Envioformat ) );
                Importe.setText( formatter2.format( importeformat ) );
                OrdenEstado.setText( orden.getEstatus() );
                Fecha.setText( orden.getFecha() );

                Button BtnCerrar = pop_up1.findViewById( R.id.btnordnecerrar );

                BtnCerrar.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pop_up1.hide();
                    }

                } );

            }
        } );
        return btn;
    }

    private View ButtonDos(final Ecommerce_orden_Model orden) {
        final Button btn = new Button( getContext() );
        btn.setText( "Guia" );
        btn.setPadding( 20, 10, 20, 10 );
        btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pop_up1 = new Dialog( getContext() );
                pop_up1.setContentView( R.layout.pop_up_ecommerce_guia );
                pop_up1.show();

                String Envio = orden.getIDshipping();
                String Token = orden.gettoken();

                TextView Nombre = pop_up1.findViewById( R.id.text_guia_nombre );
                TextView Nickname = pop_up1.findViewById( R.id.text_guia_nickname );
                TextView Cliente = pop_up1.findViewById( R.id.text_guia_cliente );
                Button BtnCerrarGuia = pop_up1.findViewById( R.id.btguianordnecerrar );

                final TextView Celular = pop_up1.findViewById( R.id.text_guia_celular );

                final TextView Direccion = pop_up1.findViewById( R.id.text_guia_direccion );
                final TextView Fecha = pop_up1.findViewById( R.id.text_guia_fecha_creacion );

                TextView TipoPago = pop_up1.findViewById( R.id.text_guia_tipo_pago );
                TextView EstatoOrden = pop_up1.findViewById( R.id.text_guia_estado_orden );

                final TextView CostoEnvio = pop_up1.findViewById( R.id.text_guia_costo_envio );

                TextView Importe = pop_up1.findViewById( R.id.text_guia_importe );

                final TextView NumeroGuia = pop_up1.findViewById( R.id.text_guia_numeroguia );
                final TextView Servicio = pop_up1.findViewById( R.id.text_guia_servicio );

                Nombre.setText( orden.getVendedor() );
                Nickname.setText( orden.getNickname() );
                Cliente.setText( orden.getCliente() );
                Importe.setText( orden.getImporte() );
                EstatoOrden.setText( orden.getEstatus() );
                TipoPago.setText( orden.getTipoPago() );

                try {
                    final String url = "https://api.mercadolibre.com/shipments/" + Envio + "?access_token=" + Token;

                    // prepare the Request
                    JsonObjectRequest getRequest = new JsonObjectRequest( Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
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

                        pop_up1.hide();
                    }
                } );


            }
        } );
        return btn;
    }

    private void VerGuia(String Token, String ship) {

        //Nombre Persona que vende
        //Nickname de persona que vende

        //Direccion solo texto
        //Cliente OK
        //Celular OK
        //Direccio OK

        //Fecha creacion OK
        //Tipo de pago
        //Estado de la orden
        //Costo de envio OK
        //importe de producto OK
        //numero de guia OK
        //servicio OK


        //date_created
        //tracking_number
        //tracking_method

        //shipping_option object
        //  cost
        //  list_cost
        //


        //receiver_address   object
        //  address_line
        //  comment
        //  zip_code
        //  receiver_name
        //  receiver_phone
        //city   object
        //  name


    }

    private static class OrdenNameUpdater implements TextWatcher {

        private Ecommerce_orden_Model ordenToUpdate;

        public OrdenNameUpdater(Ecommerce_orden_Model ordenToUpdate) {
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
            ordenToUpdate.setCliente( s.toString() );
        }
    }
}


