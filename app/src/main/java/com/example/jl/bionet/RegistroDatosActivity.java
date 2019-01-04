package com.example.jl.bionet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;

public class RegistroDatosActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_datos);
        Spinner spinner = (Spinner) findViewById(R.id.Giro);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Giros, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

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

    public void avanzar(View view) {
        Intent intent = new Intent(RegistroDatosActivity.this, Numero_sucursal.class);
        startActivity(intent);
    }






}
