package com.Danthop.bionet.model;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.Danthop.bionet.R;

import java.util.ArrayList;

public class NotificacionAdapter extends RecyclerView.Adapter<NotificacionAdapter.NotificacionViewHolder> {
    private ArrayList<Caja_notificacion> mNotificacionList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class NotificacionViewHolder extends RecyclerView.ViewHolder{

        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public CheckBox Box;

        public NotificacionViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.Fecha_notificacion);
            mTextView2 = itemView.findViewById(R.id.Hora_notificacion);
            mTextView3 = itemView.findViewById(R.id.Text_notificacion);
            Box = itemView.findViewById(R.id.checkBox);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }

                }
            });

            Box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Box.isChecked())

                        if (listener != null){
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION){
                                listener.onDeleteClick(position);
                            }
                        }
                }
            });


        }
    }

    public  NotificacionAdapter(ArrayList<Caja_notificacion> notificacionList){
        mNotificacionList = notificacionList;
    }

    @Override
    public NotificacionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.caja_notificacion, viewGroup, false);
        NotificacionViewHolder nth = new NotificacionViewHolder(v, mListener);
        return nth;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificacionViewHolder notificacionViewHolder, int i) {
        Caja_notificacion currentItem = mNotificacionList.get(i);

        notificacionViewHolder.mTextView1.setText(currentItem.getFecha());
        notificacionViewHolder.mTextView2.setText(currentItem.getHora());
        notificacionViewHolder.mTextView3.setText(currentItem.getTextNotificacion());

    }

    @Override
    public int getItemCount() {
        return mNotificacionList.size();
    }


}
