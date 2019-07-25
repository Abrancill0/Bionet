package com.Danthop.bionet.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PagoModel implements Parcelable {
    private String id;
    private String cantidad;
    private String Nombre;

    public PagoModel(String nombre,String id, String cantidad) {
        this.Nombre = nombre;
        this.id = id;
        this.cantidad = cantidad;
    }

    protected PagoModel(Parcel in) {
        id = in.readString();
        cantidad = in.readString();
        Nombre = in.readString();
    }

    public static final Creator<PagoModel> CREATOR = new Creator<PagoModel>() {
        @Override
        public PagoModel createFromParcel(Parcel in) {
            return new PagoModel(in);
        }

        @Override
        public PagoModel[] newArray(int size) {
            return new PagoModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        this.Nombre = nombre;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(cantidad);
        dest.writeString(Nombre);
    }
}
