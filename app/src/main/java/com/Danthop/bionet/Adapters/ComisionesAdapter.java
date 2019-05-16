package com.Danthop.bionet.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.Danthop.bionet.R;
import com.Danthop.bionet.Tables.SortableCorteCajaTable;
import com.Danthop.bionet.model.CorteCajaModel;

import java.text.NumberFormat;
import java.util.List;

import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class ComisionesAdapter extends LongPressAwareTableDataAdapter<CorteCajaModel> {
    int TEXT_SIZE = 12;



    public ComisionesAdapter(final Context context, final List<CorteCajaModel> data, final SortableCorteCajaTable tableView) {
        super(context, data, tableView);


    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        CorteCajaModel corte = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = rendernombrevendedor(corte);
                break;
            case 1:
                renderedView = rendermontoacomulado(corte);
                break;
            case 2:
                renderedView = rendermontopagado(corte);
                break;
            case 3:
                renderedView = rendermontopendiente(corte);
                break;
            case 4:
                renderedView = renderButton(corte);
                break;
        }
        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final CorteCajaModel Historico = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 1:
                renderedView = renderEditableInventarioName(Historico);
                break;
            default:
                renderedView = getDefaultCellView(rowIndex, columnIndex, parentView);
        }

        return renderedView;
    }

    private View renderEditableInventarioName(final CorteCajaModel corte) {
        final EditText editText = new EditText(getContext());
        editText.setPadding(20, 10, 20, 10);
        editText.setTextSize(TEXT_SIZE);
        editText.setSingleLine();
        editText.addTextChangedListener(new ComisionesAdapter.OrdenNameUpdater(corte));
        return editText;
    }

    private View rendernombrevendedor(final CorteCajaModel corte) {
        return renderString(corte.getNombrevendedor());
    }


    private View rendermontoacomulado(final CorteCajaModel corte) {
        double Precio = Double.parseDouble( String.valueOf( corte.getMontoacomulado() ) );

        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        final TextView textView = new TextView( getContext() );
        textView.setText( formatter.format( Precio ) );
        textView.setPadding( 20, 10, 20, 10 );
        textView.setTextSize( TEXT_SIZE );

        return textView;
    }

    private View rendermontopagado(final CorteCajaModel corte) {
        double Precio = Double.parseDouble( String.valueOf( corte.getMontopagado() ) );

        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        final TextView textView = new TextView( getContext() );
        textView.setText( formatter.format( Precio ) );
        textView.setPadding( 20, 10, 20, 10 );
        textView.setTextSize( TEXT_SIZE );

        return textView;
    }

    private View rendermontopendiente(final CorteCajaModel corte) {
        double Precio = Double.parseDouble( String.valueOf( corte.getMontopendiente() ) );

        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        final TextView textView = new TextView( getContext() );
        textView.setText( formatter.format( Precio ) );
        textView.setPadding( 20, 10, 20, 10 );
        textView.setTextSize( TEXT_SIZE );

        return textView;
    }

    private View renderButton(final CorteCajaModel corte) {
        return renderButtonPagar(corte);
    }

    private View renderButtonPagar(final CorteCajaModel corte) {

        final Button buttonPagar = new Button(getContext());

        buttonPagar.setPadding(20, 10, 20, 10);
        buttonPagar.setText( "PAGAR" );
        buttonPagar.setBackgroundColor(getResources().getColor( R.color.colorPrimaryDark));
        buttonPagar.setTextColor( getResources().getColor( R.color.white ) );
        //button.setDrawingCacheBackgroundColor(getResources().getColor(R.color.white));

        buttonPagar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Dialog pagar_comison;
                pagar_comison=new Dialog(getContext());
                pagar_comison.setContentView(R.layout.pop_up_pagar_comision );
                pagar_comison.show();

                TextView comision_a_pagar = pagar_comison.findViewById(R.id.comision_a_pagar);
                TextView text_importe_valor = pagar_comison.findViewById(R.id.text_importe_valor);
                TextView text_pagar_valor = pagar_comison.findViewById(R.id.text_pagar_valor);
                Button  realizar_Pago = pagar_comison.findViewById(R.id.realizar_Pago);

                Button cerrar_pago = pagar_comison.findViewById(R.id.cerrar_pago);
                cerrar_pago.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pagar_comison.hide();
                    }
                });

                Button btnSalir3 = pagar_comison.findViewById(R.id.btnSalir3);
                btnSalir3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pagar_comison.hide();
                    }
                });

                realizar_Pago = pagar_comison.findViewById(R.id.realizar_Pago);
                realizar_Pago.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                    }
                });



            }
        });


        return buttonPagar;
    }



    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }

    private static class OrdenNameUpdater implements TextWatcher {

        private CorteCajaModel ordenToUpdate;

        public OrdenNameUpdater(CorteCajaModel ordenToUpdate) {
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
        public void afterTextChanged(Editable s) { ordenToUpdate.setusuario(s.toString());
        }

    }
}

