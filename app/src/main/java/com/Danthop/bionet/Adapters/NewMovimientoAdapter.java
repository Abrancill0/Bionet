package com.Danthop.bionet.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.Danthop.bionet.model.MovimientoModel;
import com.sortabletableview.recyclerview.TableDataColumnAdapterDelegator;

public class NewMovimientoAdapter extends TableDataColumnAdapterDelegator.TableDataColumnAdapter<MovimientoModel, NewMovimientoAdapter.TextViewHolder> {
    @Override
    public TextViewHolder onCreateColumnCellViewHolder(ViewGroup parent, int
            viewType) {
        TextView text = new TextView(getContext());
        return new TextViewHolder(text);
    }
    @Override
    public void onBindColumnCellViewHolder(TextViewHolder viewHolder, int rowIndex) {
        MovimientoModel flight = getRowData(rowIndex);
        viewHolder.setImageResource(flight.getMovimiento_suc_nombre());
    }
    static class TextViewHolder extends RecyclerView.ViewHolder {
        private TextView imageView;
        private TextViewHolder(TextView ImageView) {
            super(ImageView);
            imageView = ImageView;
        }
        private void setImageResource(String text) {
            imageView.setText(text);
        }
    }
}
