package com.Danthop.bionet;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AlertDialog;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/*  DEPENDENCIAS DE FEENICIA */
//import com.Danthop.bionet.Android.BTReceiverConnection;
import com.Danthop.bionet.BTReceiverConnection;
import com.Danthop.bionet.SaleConfiguration;
import com.Danthop.bionet.SaleUtils;
import com.Danthop.bionet.SppHandlerConnection;
import com.Danthop.bionet.core.dto.ResponseCode;q1
import com.Danthop.bionet.core.dto.SendReceiptResponse;

import com.imagpay.Settings;
import com.imagpay.spp.BTReceiver;
import com.imagpay.spp.SppHandler;
import com.pnikosis.materialishprogress.ProgressWheel;


/**
 * Created by Feenicia on 05/10/2018.
 */

/// Lector Bluetooth ///
public class Feenicia_Transaction_Bluetooth extends AppCompatActivity
{
    private Toolbar toolbar;
    private TextView tvMontoMostrar;
    private static AppCompatActivity FEENICIA_TRANSACCION;
    private static TextView textViewTransaccion;
    static double monto = 0, tip = 0;
    static Integer msi = 0;

    //VARIABLE EVM
    private SppHandler handlerEVM;
    private BTReceiver _receiver;
    private Settings settings;
    private Handler ui;

    private SaleUtils utils;

    Intent intent;

    //// JAR ////
    Button btn_connect, btn_desconectar;  /// CONECTAR
    Button btn_search_devices; /// BUSCAR DISPOSITIVOS
    TextView textView_Devices;  // DISPOSITIVOS ENCONTRADOS
    static TextView textView_resultTx; // RESULTADO DE LA TRANSACCION
    ArrayList<BluetoothDevice> adapter;
    Spinner sp_sale;
    static ProgressWheel progressBar;

    SppHandlerConnection sppHandlerConnection;
    BTReceiverConnection bTReceiverConnection;

    SaleConfiguration sale = new SaleConfiguration();



    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagos);
        FEENICIA_TRANSACCION = this;

        intent = getIntent();
        tvMontoMostrar=(TextView)findViewById(R.id.tvMontoMostrar);
        textViewTransaccion = (TextView) findViewById(R.id.textView_procesando_transaccion);
        textView_resultTx = (TextView) findViewById(R.id.textView_resultTx);

        /// Jar ////
        btn_search_devices = (Button) findViewById(R.id.btn_search_devices);
        btn_connect = (Button) findViewById(R.id.btn_conectar);
        btn_desconectar = (Button) findViewById(R.id.btn_desconectar);
        textView_Devices = (TextView)findViewById(R.id.textView_Devices);
        textView_Devices.setText("");
        sp_sale = (Spinner) findViewById(R.id.sp_sales);
        progressBar = (ProgressWheel) findViewById(R.id.progressBar);

        askForContactPermission();

        utils = new SaleUtils();
        monto = 300.00;
        monto = utils.roundTwoDecimals(monto);

        tip = 1.00;
        tip = utils.roundTwoDecimals(tip);

        msi = 3;


        //DIBUJAMOS EL MONTO
        tvMontoMostrar.setText( "$ " + monto + " MN");


       // toolbar = (Toolbar) findViewById(R.id.app_bar);
       // setSupportActionBar(toolbar);
       // getSupportActionBar().setDisplayShowHomeEnabled(true);


        // PERMISO DE BLUETOOTH
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.BLUETOOTH_ADMIN}, 1);
            return;

        }
        else
        {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (!mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.enable();
            }
        }

        ///// LIBRERIA FEENICIA_BT //////////
        // 01.- Inicializar Configuraciones Terminal
        sppHandlerConnection = new SppHandlerConnection();
        handlerEVM = sppHandlerConnection.initialize(handlerEVM, settings, ui, getApplicationContext());

        // 02.- Inicializar Conexión BT
        bTReceiverConnection = new BTReceiverConnection();
        bTReceiverConnection.initializeBT(this, "FNZA");

        _receiver = bTReceiverConnection.get_receiver();
        adapter = bTReceiverConnection.getAdapter();

        ////////////////////////////////////

        sale.config(sppHandlerConnection); // Inicializar
        sale.sale_retail_without_tip(monto); // DEFAULT Monto (SIN MSI y SIN PROPINA


        // EVENTOS DE TIPO DE VENTA
        sp_sale.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                //Toast.makeText(CargaTransaccionBlueTooth.this, "Opción: " + position, Toast.LENGTH_SHORT).show();
                if(position == 0){ // RETAIL - SIN PROPINA
                    sale.sale_retail_without_tip(monto);
                }
                else if(position == 1){ // RETAIL - CON PROPINA
                    sale.sale_retail_with_tip(monto,tip);
                }
                else if(position == 2){ // MSI - SIN PROPINA
                    sale.sale_msi_without_tip(monto,msi);
                }
                else if(position == 3){ // MSI - CON PROPINA
                    sale.sale_msi_with_tip(monto,tip,msi);
                }

                Log.i("SppHandlerConnection", "MONTO: " + String.valueOf(sppHandlerConnection.getAmount()));
                Log.i("SppHandlerConnection", "TIP: " + String.valueOf(sppHandlerConnection.getTip()));
                Log.i("SppHandlerConnection", "MSI: " + String.valueOf(sppHandlerConnection.getMsi()));


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Este metodo no se utilizada
            }

        });


        /// BTN CONECTAR CON DISPOSITIVO BT FEENICIA ///
        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        if(adapter.size() < 1){
                            show_Toast(getResources().getString(R.string.textView_not_device));
                            setText(getResources().getString(R.string.text_feenicia_demo));
                            setTextTx("");
                            return;
                        }

                        Log.i("adapter", adapter.toString()); // Todos los dispositivos
                        final BluetoothDevice dev = adapter.get(0); /// Elejimos el primer device encontrado
                        Log.i("nameOfDevice", dev.getName());

                        // MENSAJE CONECTANDO
                        show_Toast(getResources().getString(R.string.textView_conectando_bluetooth));
                        Boolean resultConnection = sppHandlerConnection.connectDevice(dev);

                        if(resultConnection == true) { // SUCESS
                            setText(getResources().getString(R.string.textView_ejecutarDispositivo_bluetooth));
                            setTextTx("");
                            show_Toast(getResources().getString(R.string.text_conexion_sucess));

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btn_connect.setVisibility(View.GONE);
                                    btn_desconectar.setVisibility(View.VISIBLE);
                                    textView_Devices.setVisibility(View.GONE);
                                    textView_resultTx.setVisibility(View.GONE);
                                    btn_search_devices.setEnabled(false);
                                }
                            });


                        }
                        else if(resultConnection == false) // ERROR
                        {
                            show_Toast(getResources().getString(R.string.text_conexion_error));

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btn_connect.setVisibility(View.VISIBLE);
                                    btn_desconectar.setVisibility(View.GONE);
                                    btn_search_devices.setEnabled(true);
                                    textView_resultTx.setVisibility(View.GONE);
                                    setText(getResources().getString(R.string.text_feenicia_demo));
                                    setTextTx("");
                                }
                            });


                        }


                        _receiver.stop();

                    }
                }).start();


            }
        });

        /// BTN DESCONECTAR DISPOSITIVO///
        btn_desconectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter.isEnabled()) {
                        mBluetoothAdapter.disable();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btn_connect.setVisibility(View.VISIBLE);
                            btn_desconectar.setVisibility(View.GONE);
                            btn_search_devices.setEnabled(true);
                            textView_Devices.setText("");
                            setText(getResources().getString(R.string.text_feenicia_demo));
                            textView_resultTx.setVisibility(View.GONE);
                            setTextTx("");
                            adapter.clear(); // Limpiamos
                        }
                    });

                }catch (Exception e){
                    Log.e("Error_desconectar", e.toString());
                }


            }
        });

        // BTN PARA BUSCAR TODOS LOS DISPOSITIVOS BLUETOOTH
        btn_search_devices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (!mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.enable();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btn_connect.setVisibility(View.VISIBLE);
                        btn_desconectar.setVisibility(View.GONE);
                        setText(getResources().getString(R.string.text_feenicia_demo));
                        setTextTx("");
                    }
                });

                _receiver.start();

                runOnUiThread(new Runnable() {
                    public void run() {
                        try {

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    show_Toast(getResources().getString(R.string.text_busquedaFinalizada));
                                    Log.i("Devices_Found", adapter.toString());

                                    int size_devices = adapter.size();
                                    int x = 0;
                                    String devices = getResources().getString(R.string.text_devices_encontrados) + "\n\n";

                                    for(x = 0; x < size_devices; x++){
                                        devices = devices + x + ") " + adapter.get(x).getName().toString() + "\n";
                                    }

                                    if(adapter.size() == 0){
                                        devices = getResources().getString(R.string.text_not_devices_encontrados) + "\n\n";
                                    }

                                    textView_Devices.setText(devices);
                                    textView_Devices.setVisibility(View.VISIBLE);

                                }
                            }, 9000);
                        }catch (Exception e){
                            Log.e("Exception: ", e.toString());
                        }

                    }
                });

                runOnUiThread(new Runnable() {
                    public void run() {
                        adapter.clear();
                        textView_Devices.setText("");
                        setTextTx("");
                        textView_Devices.setVisibility(View.GONE);
                        textView_resultTx.setVisibility(View.GONE);
                        // MENSAJE BUSCANDO
                        show_Toast(getResources().getString(R.string.textView_buscando_bluetooth));
                    }
                });



            }
        });

        _receiver.registerReceiver();
    }


    public static void statusReaderCard(final String result){
        Log.i("RESULTADO_READ_CARD: " , result);

        FEENICIA_TRANSACCION.runOnUiThread(new Runnable() {
            public void run() {

                textView_resultTx.setVisibility(View.GONE);

                if(result.equals("LEYENDO TARJETA")){
                    textViewTransaccion.setText(FEENICIA_TRANSACCION.getResources().getString(R.string.msj_read_Card));
                    progressBar.setVisibility(View.VISIBLE);
                }else{
                    textViewTransaccion.setText(FEENICIA_TRANSACCION.getResources().getString(R.string.textView_ejecutarDispositivo_bluetooth));
                }

            }
        });
    }

    /* METODO EL CUAL REGRESA EL RESULTADO DE LA TX*/
    public static void resultOfTransaction(final ResponseCode response){

        try {
            Log.i("RESULT_TX: ", response.toString());

            FEENICIA_TRANSACCION.runOnUiThread(new Runnable() {
                public void run() {
                    textView_resultTx.setVisibility(View.VISIBLE);
                    String setValue = FEENICIA_TRANSACCION.getResources().getString(R.string.msj_tx_result) + "\n";
                    setValue = setValue +
                            "\nresponseCode: " + response.getResponseCode() +
                            "\ndescription: " + response.getDescriptionCode();

                    /* SI LA RESPUESTA DEL SERVICIO FUE 00 */
                    if(response.getResponseCode() != null) {

                        if (response.getTransaction() != null) {
                            setValue = setValue + "\n\n" + response.getTransaction().toStringOrder();
                        }

                        if (response.getResponseCode().equals("00")) {
                            /*** OPCIONAL ***/
                            /// ENVIAMOS EL RECIBO DE COMPRA /////
                            /****** sendReceipt(receiptId,correoDeseado) *******/
                            SppHandlerConnection.sendReceipt(response.getTransaction().getReceiptId(),"feenicia@yopmail.com");
                        }
                    }

                    setTextTx(setValue);
                    progressBar.setVisibility(View.GONE);

                }
            });

        }catch (Exception e) {
            Log.e("ERROR_TX", e.toString());
        }finally {
            textViewTransaccion.setText(FEENICIA_TRANSACCION.getResources().getString(R.string.msj_tx_finish));
        }

    }

    public static void onResultSendReceipt(SendReceiptResponse response){
        if(response != null){
            Log.i("RESPONSE_SEND_RECEIPT", response.toString());
        }

    }


    /* PINTA EN PANTALLA UN MENSAJE TOAST */
    public void show_Toast(final String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(FEENICIA_TRANSACCION, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //METODO PARA CAMBIAR LA LEYENDA DEL TEXTO QUE ESTA DEBAJO DEL PROGRESS BAR
    private void setText(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewTransaccion.setText(text);
            }
        });
    }

    //METODO PARA MOSTRAR EL RESULTADO DE LA TRANSACCION
    private static void setTextTx(final String text) {
        FEENICIA_TRANSACCION.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView_resultTx.setText(text);
            }
        });
    }


    @Override
    protected void onResume () {
        super.onResume();

    }

    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        handlerEVM.close();
        _receiver.stop();
        super.onStop();
    }

    public void onDestroy() {
        _receiver.stop();
        _receiver.unregisterReceiver();
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
        }
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(FEENICIA_TRANSACCION)
                .setTitle(getResources().getString(R.string.app_name))
                .setMessage(getResources().getString(R.string.dialog_mensaje_venta_salir))
                .setCancelable(false)
                .setNegativeButton(getResources().getString(R.string.dialog_NO), new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setPositiveButton(R.string.dialog_SI, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }


    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }


    @TargetApi(Build.VERSION_CODES.M)
    public void askForContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int ubicacionPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

            List<String> listPermissionsNeeded = new ArrayList<>();

            if (ubicacionPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }

            if (!listPermissionsNeeded.isEmpty()) {
                requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
                return;
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (!mBluetoothAdapter.isEnabled()) {
                        mBluetoothAdapter.enable();
                    }
                } else {
                    // Permission Denied
                    Toast.makeText(FEENICIA_TRANSACCION, "Debes dar premisos de Bluetooth", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}