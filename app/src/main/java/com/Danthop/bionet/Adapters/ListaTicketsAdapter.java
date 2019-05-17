package com.Danthop.bionet.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.Danthop.bionet.R;
import com.Danthop.bionet.Tables.SortableCorteCajaTable;
import com.Danthop.bionet.model.CorteCajaModel;
import com.Danthop.bionet.model.InventarioModel;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import java.text.NumberFormat;
import java.util.List;

import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class ListaTicketsAdapter extends LongPressAwareTableDataAdapter<CorteCajaModel> {
    int TEXT_SIZE = 12;
    private final List<CorteCajaModel> TicketsFactura;

    public ListaTicketsAdapter(final Context context, final List<CorteCajaModel> data, final SortableCorteCajaTable tableView, List<CorteCajaModel> ticketsfactura) {
        super(context, data, tableView);
        TicketsFactura = ticketsfactura;
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        CorteCajaModel corte = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {

            case 0:
                renderedView = renderCheck(corte,rowIndex);
                break;

            case 1:
                renderedView = rendernumTickets(corte);
                break;
            case 2:
                renderedView = rendertotal(corte);
                break;
            case 3:
                renderedView = renderefectivo(corte);
                break;
            case 4:
                 renderedView = rendermonederoelectronico(corte);
                break;
            case 5:
                 renderedView = renderdineroelectronico(corte);
                break;
            case 6:
                 renderedView = rendervalesdespensa(corte);
                break;
            case 7:
                renderedView = renderfecha(corte);
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
        editText.addTextChangedListener(new ListaTicketsAdapter.OrdenNameUpdater(corte));
        return editText;
    }

    private View renderCheck(final CorteCajaModel corte, final int Indice) {

        final CheckBox check = new CheckBox(getContext());

        check.setPadding(20, 10, 20, 10);
        check.setBackgroundColor(getResources().getColor( R.color.white));
        check.setDrawingCacheBackgroundColor(getResources().getColor(R.color.white));

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (check.isChecked())
                {

                    final CorteCajaModel corteM = new CorteCajaModel (
                            corte.getid_venta(),
                            corte.gettotal(),
                            "",
                            "",
                            "",
                            "",0.0,0.0,0.0,0.0,
                            corte.getfecha(),"","",0.0,
                            corte.getefectivo(),corte.getMonederoElectronico(), corte.getDineroElectronico(),corte.getValesDespensa(),
                            "",0.0,0.0,0.0);
                    TicketsFactura.add(corteM);

                }
                else
                {
                    for(int i=0;i <TicketsFactura.size();i++)
                    {
                        if(corte.getid_venta().equals(TicketsFactura.get(i).getid_venta()))
                        {
                            TicketsFactura.remove(i);
                        }

                    }

                }

            }
        });

        return check;
    }

    private View rendernumTickets(final CorteCajaModel corte) {
        return renderString(corte.getid_venta());
    }

    private View rendertotal(final CorteCajaModel corte) {
        double Precio = Double.parseDouble( corte.gettotal() );

        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        final TextView textView = new TextView( getContext() );
        textView.setText( formatter.format( Precio ) );
        textView.setPadding( 20, 10, 20, 10 );
        textView.setTextSize( TEXT_SIZE );

        return textView;
    }

    private View renderefectivo(final CorteCajaModel corte) {
        double Precio =corte.getefectivo01();

        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        final TextView textView = new TextView( getContext() );
        textView.setText( formatter.format( Precio ) );
        textView.setPadding( 20, 10, 20, 10 );
        textView.setTextSize( TEXT_SIZE );

        return textView;
    }

    private View rendermonederoelectronico(final CorteCajaModel corte) {
        double Precio =corte.getMonederoElectronico05();

        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        final TextView textView = new TextView( getContext() );
        textView.setText( formatter.format( Precio ) );
        textView.setPadding( 20, 10, 20, 10 );
        textView.setTextSize( TEXT_SIZE );

        return textView;
    }

    private View renderdineroelectronico(final CorteCajaModel corte) {
        double Precio =corte.getDineroElectronico06();

        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        final TextView textView = new TextView( getContext() );
        textView.setText( formatter.format( Precio ) );
        textView.setPadding( 20, 10, 20, 10 );
        textView.setTextSize( TEXT_SIZE );

        return textView;
    }

    private View rendervalesdespensa(final CorteCajaModel corte) {
        double Precio =corte.getValesDespensa08();

        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        final TextView textView = new TextView( getContext() );
        textView.setText( formatter.format( Precio ) );
        textView.setPadding( 10, 10, 10, 10 );
        textView.setTextSize( TEXT_SIZE );

        return textView;
    }

    private View renderfecha(final CorteCajaModel corte) {
        return renderString(corte.getfecha());
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
    }}