package com.fesi.funda.src;

import java.io.Serializable;

public class Clases implements Serializable {

    private String nombreClase;
    private String _idClase;

    public Clases() {
    }

    public String getNombreClase() {
        return nombreClase;
    }

    public void setNombreClase(String nombreClase) {
        this.nombreClase = nombreClase;
    }

    public String get_idClase() {
        return _idClase;
    }

    public void set_idClase(String _idClase) {
        this._idClase = _idClase;
    }
}
