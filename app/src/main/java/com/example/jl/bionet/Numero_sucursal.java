package com.example.jl.bionet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class Numero_sucursal extends Activity {

    TableView tableView1 = (TableView) findViewById(R.id.tabla1);
    private static final String[] TABLE_HEADERS = { "Nombre de la sucursal", "Teléfono", "Correo", "Dirección" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.numero_sucursales);
        tableView1.setHeaderAdapter(new SimpleTableHeaderAdapter(this, TABLE_HEADERS));



    }






}
