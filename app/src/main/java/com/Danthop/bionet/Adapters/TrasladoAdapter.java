package com.Danthop.bionet.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.R;
import com.Danthop.bionet.Tables.SortableInventariosTable;
import com.Danthop.bionet.model.ArticuloModel;
import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.InventarioModel;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class TrasladoAdapter extends LongPressAwareTableDataAdapter<InventarioModel> implements Filterable {
    int TEXT_SIZE = 16;
    private int CantidadTraspaso[] = new int[10000];

    private List<InventarioModel> InventarioList;
    private List<InventarioModel> InventarioListFull;

    private final List<InventarioModel> ArticulosTraslados;

    public TrasladoAdapter(final Context context, final List<InventarioModel> data, final SortableInventariosTable tableView,List<InventarioModel> articulostraslados) {
        super(context, data, tableView);
        ArticulosTraslados = articulostraslados;

        InventarioList = data;
        InventarioListFull = new ArrayList<>(data);
    }
    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        InventarioModel Invetario = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderCheck(Invetario,rowIndex);
                break;
            case 1:
                renderedView = renderarticulo(Invetario);
                break;
            case 2:
                renderedView = rendercategoria(Invetario);
                break;
            case 3:
                renderedView = rendersucursal(Invetario);
                break;
            case 4:
                renderedView = renderexistencia(Invetario);
                break;
            case 5:
                renderedView = renderCantidad(Invetario,rowIndex);
        }
        return renderedView;
    }


    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final InventarioModel Invetario = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 1:
                renderedView = renderEditableInventarioName(Invetario);
                break;
            default:
                renderedView = getDefaultCellView(rowIndex, columnIndex, parentView);
        }

        return renderedView;
    }

    private View renderEditableInventarioName(final InventarioModel Inventario) {
        final EditText editText = new EditText(getContext());
        editText.setText(Inventario.getNombre_sucursal());
        editText.setPadding(20, 10, 20, 10);
        editText.setTextSize(TEXT_SIZE);
        editText.setSingleLine();
        editText.addTextChangedListener(new TrasladoAdapter.OrdenNameUpdater(Inventario));
        return editText;
    }

    private View renderCheck(final InventarioModel Inventario,final int Indice) {

        final CheckBox check = new CheckBox(getContext());

        check.setPadding(20, 10, 20, 10);
        check.setBackgroundColor(getResources().getColor(R.color.white));
        check.setDrawingCacheBackgroundColor(getResources().getColor(R.color.white));


        final ElegantNumberButton cantidad = new ElegantNumberButton(getContext());

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (check.isChecked())
                {
                    int ElegantButtoncantidad = CantidadTraspaso[Indice];

                    final InventarioModel inventarioM = new InventarioModel(
                            "", "", Inventario.getExistencia(), "", "", "",
                            "","","","","",
                            "","", "","","",
                            "","","", "","","",
                            "","", "","","",
                            "", "",Inventario.getUUIDarticulo(),Inventario.getUUIDvariante(),Inventario.getUUIDmodificador(),Inventario.getUUIDexistencias(),String.valueOf(ElegantButtoncantidad),"");
                    ArticulosTraslados.add(inventarioM);

                }
                else
                {
                    for(int i=0;i <ArticulosTraslados.size();i++)
                    {
                        if(Inventario.getUUIDarticulo().equals(ArticulosTraslados.get(i).getUUIDarticulo()))
                        {
                            ArticulosTraslados.remove(i);
                        }

                    }
                }

            }
        });



        return check;
    }

    private View renderarticulo(final InventarioModel Inventario) {
        return renderString(Inventario.getProducto());
    }

    private View renderexistencia(final InventarioModel Inventario) {
        return renderString(Inventario.getExistencia());
    }

    private View rendercategoria(final InventarioModel Inventario) {
        return renderString(Inventario.getCategoria());
    }

    private View rendersucursal(final InventarioModel Inventario) {
        return renderString(Inventario.getNombre_sucursal());
    }

    private View renderCantidad(final InventarioModel Inventario, final int Indice) {
        final ElegantNumberButton cantidad = new ElegantNumberButton(getContext());
        //cantidad.setNumber(Inventario.getCantidad());
        cantidad.setPadding(20, 10, 20, 10);
        cantidad.setBackgroundColor(getResources().getColor(R.color.white));
        cantidad.setDrawingCacheBackgroundColor(getResources().getColor(R.color.white));

        cantidad.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {

                CantidadTraspaso[Indice] = Integer.parseInt( cantidad.getNumber() );

                if (Integer.parseInt(cantidad.getNumber()) > Integer.parseInt(Inventario.getExistencia()) )
                {
                    cantidad.setNumber(Inventario.getExistencia());

                    Toast toast1 = Toast.makeText(getContext(), "la cantidad seleccionada debe ser menor o igual a la existencia", Toast.LENGTH_SHORT);
                    toast1.show();
                }
                else
                {
                    String cat = cantidad.getNumber();
                }


            }
        });


        return cantidad;
    }


    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }

    private static class OrdenNameUpdater implements TextWatcher {

        private InventarioModel ordenToUpdate;

        public OrdenNameUpdater(InventarioModel ordenToUpdate) {
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
            ordenToUpdate.setNombre_sucursal(s.toString());
        }
    }

    @Override
    public Filter getFilter() {
        return InventarioFilter;
    }

    private Filter InventarioFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<InventarioModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(InventarioListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (InventarioModel item : InventarioListFull) {
                    if (item.getProducto().toLowerCase().contains(filterPattern)
                            || item.getCategoria().toLowerCase().contains(filterPattern)
                            || item.getNombre_sucursal().toLowerCase().contains(filterPattern)){
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
            InventarioList.clear();
            InventarioList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
