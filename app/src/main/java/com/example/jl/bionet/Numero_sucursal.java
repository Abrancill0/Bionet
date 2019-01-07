package com.example.jl.bionet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class Numero_sucursal extends Activity {


    private static final String[] TABLA1_HEADERS = { "Nombre de la sucursal", "Teléfono", "Correo", "Dirección" };
    private static final String[] TABLA2_HEADERS = { "No.","Nombre de la sucursal", "Teléfono", "Correo", "Dirección" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.numero_sucursales);

        TableView tableView1 = (TableView) findViewById(R.id.tabla1);
        TableView tableView2 = (TableView) findViewById(R.id.tabla2);
        tableView1.setHeaderBackgroundColor(getResources().getColor(R.color.white));
        tableView1.setHeaderAdapter(new SimpleTableHeaderAdapter(this, TABLA1_HEADERS));
        tableView2.setHeaderBackgroundColor(getResources().getColor(R.color.white));
        tableView2.setHeaderAdapter(new SimpleTableHeaderAdapter(this, TABLA2_HEADERS));

        NumberPicker np = (NumberPicker) findViewById(R.id.NumeroSucursales);
        String[] nums = new String[20];
        for(int i=0; i<nums.length; i++)
            nums[i] = Integer.toString(i);

        np.setMinValue(1);
        np.setMaxValue(20);
        np.setWrapSelectorWheel(false);
        np.setDisplayedValues(nums);
        np.setValue(1);


    }

    public void eleccion_premium(View view) {
        Intent intent = new Intent(Numero_sucursal.this, EleccionPremium.class);
        startActivity(intent);
    }






}
