package com.aplication.bionet.Class;

public class HttpRequest<T> {

    public HttpRequest(){

        respuesta = null;

    }

    public void SetRespuesta(T nuevoValor){

        respuesta = nuevoValor;
    }

    public T getRespuesta(){





        return  respuesta;
    }

    private T respuesta;

}
