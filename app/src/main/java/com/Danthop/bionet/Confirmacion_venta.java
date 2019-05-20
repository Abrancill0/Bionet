package com.Danthop.bionet;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.model.ArticuloModel;
import com.Danthop.bionet.model.MovimientoModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.webviewtopdf.PdfView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.NumberFormat;

public class Confirmacion_venta extends AppCompatActivity {

    private String usu_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_up_ventas_confirmacion_venta);

        TextView Importe_Cambio = findViewById(R.id.importe_cambio);
        TextView Importe_Recibido = findViewById(R.id.importe_recibido);
        TextView Importe_Venta = findViewById(R.id.importe_venta);

        SharedPreferences sharedPref = this.getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id", "");

        Bundle bundle = getIntent().getExtras();

        double IC =  bundle.getDouble("IC");
        double IR =  bundle.getDouble("IR");
        double IV =  bundle.getDouble("IV");

        double CambioConDecimal = IC;
        double RecibidoConDecimal = IR;
        double ImporteTotalConDecimal = IV;
        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        Importe_Cambio.setText(formatter.format(CambioConDecimal));
        Importe_Recibido.setText(formatter.format(RecibidoConDecimal));
        Importe_Venta.setText(formatter.format(ImporteTotalConDecimal));

        Button cerrarPopUp = findViewById(R.id.btnSalir3);
        cerrarPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Ventas()).addToBackStack(null).commit();
            }


        });

        Button aceptar = findViewById(R.id.aceptar_cerrar_ventana);
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

               // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Ventas()).addToBackStack(null).commit();
            }
        });

    }
    private void loadTicket(MovimientoModel movimiento)
    {
        try {

            String url = getString(R.string.Url);

            String ApiPath;

            ApiPath = url + "/api/ventas/movimientos/obtener_detalle_ticket?" +
                    "usu_id=" + usu_id +
                    "&esApp=1" +
                    "&tic_id="+ movimiento.getMovimiento_tic_id()+
                    "&suc_id=" + SucursalID.get(SpinnerSucursal.getSelectedItemPosition());
            // prepare the Request
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ApiPath, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                int EstatusApi = Integer.parseInt(response.getString("estatus"));

                                if (EstatusApi == 1) {

                                    JSONObject RespuestaResultado = response.getJSONObject("resultado");

                                    JSONObject NodoASucursal = RespuestaResultado.getJSONObject("aSucursal");
                                    NombreSucursal = NodoASucursal.getString("suc_nombre");
                                    NumeroSucursal = NodoASucursal.getString("suc_numero");
                                    Direccion = NodoASucursal.getString("suc_direccion");
                                    RazonSocial = NodoASucursal.getString("suc_razon_social");
                                    RFC = NodoASucursal.getString("suc_rfc");
                                    LogoNegocio = NodoASucursal.getString("con_logo_negocio");

                                    JSONObject NodoTicket = RespuestaResultado.getJSONObject("aTicket");
                                    FechaCreacion = NodoTicket.getString("tic_fecha_hora_creo");
                                    NombreVendedor = NodoTicket.getString("tic_nombre_vendedor");
                                    NombreCliente = NodoTicket.getString("tic_nombre_cliente");
                                    Subtotal = NodoTicket.getString("tic_importe_subtotal");
                                    Total = NodoTicket.getString("tic_importe_total");
                                    JSONArray ArregloArticulos = NodoTicket.getJSONArray("aArticulos");
                                    JSONArray NodoImpuestos = RespuestaResultado.getJSONArray("aTicketImpuestos");

                                    ImpuestosTotal=0;
                                    for(int i=0;i<NodoImpuestos.length();i++)
                                    {
                                        JSONObject elemento = NodoImpuestos.getJSONObject(i);
                                        ImpuestosTotal= ImpuestosTotal+Float.parseFloat(elemento.getString("importe_impuesto"));
                                    }

                                    ListaArticulosTicket.clear();
                                    for(int i=0;i<ArregloArticulos.length();i++)
                                    {
                                        JSONObject elemento = ArregloArticulos.getJSONObject(i);
                                        String Cantidad = elemento.getString("tar_cantidad");
                                        String Nombre = elemento.getString("tar_nombre_articulo");
                                        String Precio = elemento.getString("tar_precio_articulo");
                                        String Importe = elemento.getString("tar_importe_total");

                                        ArticuloModel articulo =
                                                new ArticuloModel("",
                                                        Nombre,
                                                        "",
                                                        Precio,
                                                        "",
                                                        "",
                                                        "",
                                                        "",
                                                        Cantidad,
                                                        "",
                                                        "",
                                                        "",
                                                        Importe,
                                                        "",
                                                        "",
                                                        "");

                                        ListaArticulosTicket.add(articulo);
                                    }



                                    JSONObject NodoCuentasBioNet = RespuestaResultado.getJSONObject("aCuentasBioNet");
                                    NumeroTicket = NodoCuentasBioNet.getString("cbn_numero_ticket");

                                    NumberFormat formatter = NumberFormat.getCurrencyInstance();
                                    String cadenaArticulos="";
                                    for(int j=0; j<ListaArticulosTicket.size();j++)
                                    {
                                        double PrecioArticulo = Double.parseDouble(ListaArticulosTicket.get(j).getarticulo_Precio());
                                        double ImporteArticulo = Double.parseDouble(ListaArticulosTicket.get(j).getArticulo_importe());


                                        cadenaArticulos=cadenaArticulos+
                                                ("<tr> \n"+
                                                        "<td>"+ListaArticulosTicket.get(j).getArticulo_cantidad()+"</td>\n"+
                                                        "<td>"+ListaArticulosTicket.get(j).getarticulo_Nombre()+"</td>\n"+
                                                        "<td>"+formatter.format( PrecioArticulo )+"</td>\n"+
                                                        "<td>"+formatter.format( ImporteArticulo )+"</td>\n"+
                                                        "</tr>");
                                    }

                                    content= "<!DOCTYPE html>\n" +
                                            "<html dir=\"ltr\" lang=\"en\">\n" +
                                            "<head>\n" +
                                            "    <meta charset=\"utf-8\">\n" +
                                            "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                                            "<style type=\"text/css\" media=\"all\">\n" +
                                            "#tblTicketTemplate {\n" +
                                            "  width: 100%;\n" +
                                            "  font-size: 10px;\n" +
                                            "  font-family: \"Courier New\";\n" +
                                            "  text-transform: uppercase;\n" +
                                            "  line-height: 1.3;\n" +
                                            "  border: solid 1px #D4DADF;\n" +
                                            "}\n" +
                                            "#tblTicketTemplate thead tr td {\n" +
                                            "  padding-left: 10%;\n" +
                                            "  padding-right: 10%;\n" +
                                            "}\n" +
                                            "#tblTicketTemplate #tdLogo {\n" +
                                            "  text-align: center;\n" +
                                            "  vertical-align: middle;\n" +
                                            "}\n" +
                                            "#tblTicketTemplate #tdFiscal{\n" +
                                            "  text-align: center;\n" +
                                            "  vertical-align: middle;\n" +
                                            "  font-weight: bold;\n" +
                                            "}\n" +
                                            "#tblTicketTemplate .trDivider{\n" +
                                            "  border-bottom: dashed 1px black;\n" +
                                            "}\n" +
                                            "#tblTicketTemplate .trDivider td {\n" +
                                            "  padding-bottom: 3px;\n" +
                                            "}\n" +
                                            "#tblTicketTemplate .trDivider + tr td {\n" +
                                            "  padding-top: 3px;\n" +
                                            "}\n" +
                                            "#tblTicketTemplate tbody .trInfo{\n" +
                                            "  font-size: 10px;\n" +
                                            "  vertical-align: top;\n" +
                                            "}\n" +
                                            "#tblTicketTemplate tbody .trInfo td a{\n" +
                                            "  text-align: center;\n" +
                                            "}\n" +
                                            "#tblTicketTemplate tbody #trTituloDetalle{\n" +
                                            "  font-weight: bold;\n" +
                                            "  text-align: center;\n" +
                                            "  border-top: dashed 1px black;\n" +
                                            "  border-bottom: dashed 1px black;\n" +
                                            "}\n" +
                                            "#tblTicketTemplate tbody .trArticulo{\n" +
                                            "  font-size: 10px;\n" +
                                            "}\n" +
                                            "#tblTicketTemplate .importe{\n" +
                                            "  text-align: right;\n" +
                                            "}\n" +
                                            "#tblTicketTemplate tbody .trArticulo + tr td{\n" +
                                            "  font-size: 9px;\n" +
                                            "}\n" +
                                            "#tblTicketTemplate tbody .trTotal{\n" +
                                            "  font-size: 14px;\n" +
                                            "  font-weight: bold;\n" +
                                            "}\n" +
                                            "#tblTicketTemplate tfoot{\n" +
                                            "  text-align: center;\n" +
                                            "}\n" +
                                            "</style>\n" +
                                            "</head>\n" +
                                            "<body>\n"+
                                            "<table id=\"tblTicketTemplate\">\n" +
                                            "  <thead>\n" +
                                            "    <tr>\n" +
                                            "      <td id=\"tdLogo\" colspan=\"4\">\n" +
                                            "        <img src="+getString(R.string.Url)+LogoNegocio+" class=\"img_con_logo_ticket\" width=\"100%\" height=\"75\" />\n" +
                                            "      </td>\n" +
                                            "    </tr>\n" +
                                            "    <tr>\n" +
                                            "      <td id=\"tdFiscal\" colspan=\"4\">\n" +
                                            "        "+RazonSocial+"<br />"+RFC+"\n" +
                                            "      </td>\n" +
                                            "    </tr>\n" +
                                            "    <tr class=\"trDivider\">\n" +
                                            "      <td id=\"tdFiscal\" colspan=\"4\">\n" +
                                            "        <!--Funciones::formatDireccion($aDatos['aSucursalPrincipal']['suc_direccion'], \"suc_\")-->\n" +
                                            "      </td>\n" +
                                            "    </tr>\n" +
                                            "    <tr>\n" +
                                            "      <td id=\"tdFiscal2\" colspan=\"4\">\n" +
                                            "        <!--Funciones::formatDireccion($aDatos['aSucursal']['suc_direccion'], \"suc_\")-->\n" +
                                            "      </td>\n" +
                                            "    </tr>\n" +
                                            "    <tr class=\"trDivider\">\n" +
                                            "      <td id=\"tdFiscalTelefono\" colspan=\"4\">\n" +
                                            "        <!--Tel.:  $aDatos['aSucursal']['suc_telefono'] -->\n" +
                                            "      </td>\n" +
                                            "    </tr>\n" +
                                            "  </thead>\n" +
                                            "  <tbody>\n" +
                                            "    <tr class=\"trInfo\">\n" +
                                            "      <td id=\"tdFechaHora\" colspan=\"2\">\n" +
                                            "        <b>Fec./Hr.:</b> "+FechaCreacion+"\n" +
                                            "      </td>\n" +
                                            "      <td id=\"tdTicketNum\" colspan=\"2\">\n" +
                                            "        <b>Ticket:</b>"+NumeroTicket+"\n" +
                                            "      </td>\n" +
                                            "    </tr>\n" +
                                            "    <tr class=\"trInfo\">\n" +
                                            "      <td id=\"tdVendedor\" colspan=\"4\">\n" +
                                            "        <b>Vendedor:</b>"+NombreVendedor+"\n" +
                                            "      </td>\n" +
                                            "    </tr>\n" +
                                            "    <tr class=\"trInfo\">\n" +
                                            "      <td id=\"tdCliente\" colspan=\"4\">\n" +
                                            "        <b>Cliente:</b>"+NombreCliente+"\n" +
                                            "      </td>\n" +
                                            "    </tr>\n" +
                                            "    <tr id=\"trTituloDetalle\">\n" +
                                            "      <td>C.</td>\n" +
                                            "      <td>Articulo</td>\n" +
                                            "      <td>P.U.</td>\n" +
                                            "      <td>Importe</td>\n" +
                                            "    </tr>\n" +
                                            "    <tr id=\"tableListArticulos\">\n" +
                                            "      "+ cadenaArticulos +
                                            "      <tr>\n" +
                                            "        <td></td>\n" +
                                            "        <!--td colspan=\"2\">Descripción del articulo</td-->\n" +
                                            "        <td></td>\n" +
                                            "      </tr-->\n" +
                                            "    </tr>\n" +
                                            "    <tr class=\"trDivider\">\n" +
                                            "      <td colspan=\"4\"></td>\n" +
                                            "    </tr>\n" +
                                            "\n" +
                                            "    <tr class=\"trTotales\">\n" +
                                            "      <td colspan=\"2\">Subtotal:</td>\n" +
                                            "      <td class=\"importe\">$"+Subtotal+"</td>\n" +
                                            "      <td id=\"tdSubTotal\" class=\"importe\"></td>\n" +
                                            "    </tr>\n" +
                                            "    <tr id=\"trImpuestosTotales\" class=\"trTotales\">\n" +
                                            "      <td colspan=\"2\">Impuestos:</td>\n" +
                                            "      <td class=\"importe\">"+formatter.format( ImpuestosTotal)+"</td>\n" +
                                            "      <!--td class=\"importe\">123</td-->\n" +
                                            "    </tr>\n" +
                                            "    <tr class=\"trTotal\">\n" +
                                            "      <td colspan=\"2\">Total:</td>\n" +
                                            "      <td class=\"importe\">$"+Total+"</td>\n" +
                                            "      <td id=\"tdTotal\" class=\"importe\"></td>\n" +
                                            "    </tr>\n" +
                                            "\n" +
                                            "    <tr class=\"trInfo\">\n" +
                                            "      <td id=\"tdQR\" colspan=\"4\">\n" +
                                            "        <!--if(trim(@$aDatos['aSucursal']['con_url_encuesta']) != \"\")\n" +
                                            "          <a href=\" trim(@$aDatos['aSucursal']['con_url_encuesta']) \" target=\"_blank\">\n" +
                                            "            !! QrCode::size(200)->generate(trim($aDatos['aSucursal']['con_url_encuesta'])); !!\n" +
                                            "          </a>\n" +
                                            "          <br />\n" +
                                            "          <a href=\" trim(@$aDatos['aSucursal']['con_url_encuesta']) \" target=\"_blank\">Nos Interesa su opinión</a>\n" +
                                            "        endif-->\n" +
                                            "      </td>\n" +
                                            "    </tr>\n" +
                                            "\n" +
                                            "    <tr class=\"trInfo\">\n" +
                                            "      <td colspan=\"4\">\n" +
                                            "        <!--b>Tarjeta de Credito:</b> 0099-->\n" +
                                            "      </td>\n" +
                                            "    </tr>\n" +
                                            "    <tr class=\"trInfo\">\n" +
                                            "      <td colspan=\"4\">\n" +
                                            "        <!--b>Folio de facturación:</b><br />0001-001-09-101010-->\n" +
                                            "      </td>\n" +
                                            "    </tr>\n" +
                                            "    <tr class=\"trInfo trDivider\">\n" +
                                            "      <td colspan=\"4\">\n" +
                                            "        <!--Usted cuenta con 132 puntos del programa de lealtad-->\n" +
                                            "      </td>\n" +
                                            "    </tr>\n" +
                                            "    <!--if (trim($aDatos['aSucursal']['con_texto_politica_devolucion_ticket_digital']) != \"\")\n" +
                                            "      <tr class=\"trInfo trDivider\">\n" +
                                            "        <td colspan=\"4\">\n" +
                                            "          <small><b>*<span id=\"text_con_texto_politica_devolucion_ticket_digital\"> $aDatos['aSucursal']['con_texto_politica_devolucion_ticket_digital'] </span></b></small>\n" +
                                            "        </td>\n" +
                                            "      </tr>\n" +
                                            "    endif-->\n" +
                                            "\n" +
                                            "    <tr class=\"trInfo\">\n" +
                                            "      <td colspan=\"4\">\n" +
                                            "        <small><span id=\"text_con_texto_personalidado_ticket_digital\" class=\"h5\"><!-- $aDatos['aSucursal']['con_texto_personalidado_ticket_digital'] --></span></small>\n" +
                                            "      </td>\n" +
                                            "    </tr>\n" +
                                            "\n" +
                                            "  </tbody>\n" +
                                            "  <tfoot>\n" +
                                            "    <tr class=\"trInfo\">\n" +
                                            "      <td colspan=\"4\">\n" +
                                            "        Este ticket fue creado desde bio-Net Punto de Venta, el mejor sistema para tu negocio, para más información visita <a href=\"bionetpos.com\" target=\"_blank\">bionetpos.com</a>\n" +
                                            "      </td>\n" +
                                            "    </tr>\n" +
                                            "  </tfoot>\n" +
                                            "</table>\n"+
                                            "</body>\n"+
                                            "</html>";
                                    Dialog dialog = new Dialog(getContext());
                                    dialog.setContentView(R.layout.pop_up_ticket_web);
                                    webView = (WebView) dialog.findViewById(R.id.simpleWebView);
                                    // displaying text in WebView
                                    webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
                                    dialog.show();

                                    File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/PDFTicket/");
                                    final String fileName="Ticket.pdf";

                                    final ProgressDialog progressDialog=new ProgressDialog(getContext());
                                    progressDialog.setMessage("Espere un momento por favor");
                                    progressDialog.show();
                                    PdfView.createWebPrintJob(getActivity(), webView, directory, fileName, new PdfView.Callback() {

                                        @Override
                                        public void success(String path) {
                                            progressDialog.dismiss();
                                            PdfView.openPdfFile(getActivity(),getString(R.string.app_name),"¿Desea abrir el archivo pdf?"+fileName,path);
                                        }

                                        @Override
                                        public void failure() {
                                            progressDialog.dismiss();

                                        }
                                    });



                                }
                            } catch (JSONException e) {
                                Toast toast1 =
                                        Toast.makeText(getContext(),
                                                String.valueOf(e), Toast.LENGTH_LONG);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast toast1 =
                                    Toast.makeText(getContext(),
                                            String.valueOf(error), Toast.LENGTH_LONG);
                        }
                    }
            );
            getRequest.setShouldCache(false);

            VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(getRequest);
        } catch (Error e) {
            e.printStackTrace();
        }



    }

}
