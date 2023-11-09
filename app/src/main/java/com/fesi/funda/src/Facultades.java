package com.fesi.funda.src;

import java.io.Serializable;
import java.util.List;

public class Facultades implements Serializable {

    private String nombreFacultad;
    private List<Carreras> carreras;
    private String _idFacultad;
    private String descripcionFacultad;

    public Facultades() {
    }

    public String getNombreFacultad() {
        return nombreFacultad;
    }

    public void setNombreFacultad(String nombreFacultad) {
        this.nombreFacultad = nombreFacultad;
    }

    public List<Carreras> getCarreras() {
        return carreras;
    }

    public void setCarreras(List<Carreras> carreras) {
        this.carreras = carreras;
    }

    public String get_idFacultad() {
        return _idFacultad;
    }

    public void set_idFacultad(String _idFacultad) {
        this._idFacultad = _idFacultad;
    }

    public String getDescripcionFacultad() {
        return descripcionFacultad;
    }

    public void setDescripcionFacultad(String descripcionFacultad) {
        this.descripcionFacultad = descripcionFacultad;
    }
}
