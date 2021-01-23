package com.fesi.funda.src;

import java.io.Serializable;
import java.util.List;

public class CarrerasInscritas implements Serializable {

    private String nombreCarrera;
    private String _idCarrera;
    private int cantidadCursos;

    public CarrerasInscritas() {
    }

    public String getNombreCarrera() {
        return nombreCarrera;
    }

    public void setNombreCarrera(String nombreCarrera) {
        this.nombreCarrera = nombreCarrera;
    }

    public String get_idCarrera() {
        return _idCarrera;
    }

    public void set_idCarrera(String _idCarrera) {
        this._idCarrera = _idCarrera;
    }

    public int getCantidadCursos() {
        return cantidadCursos;
    }

    public void setCantidadCursos(int cantidadCursos) {
        this.cantidadCursos = cantidadCursos;
    }
}
