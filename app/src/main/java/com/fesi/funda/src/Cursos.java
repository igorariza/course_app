package com.fesi.funda.src;

import java.io.Serializable;
import java.util.List;

public class Cursos implements Serializable {

    private String nombreCurso;
    private String _idCurso;
    private List<Clases> clases;
    private String imagenCurso;

    public Cursos() {
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public String get_idCurso() {
        return _idCurso;
    }

    public void set_idCurso(String _idCurso) {
        this._idCurso = _idCurso;
    }

    public List<Clases> getClases() {
        return clases;
    }

    public void setClases(List<Clases> clases) {
        this.clases = clases;
    }

    public String getImagenCurso() {
        return imagenCurso;
    }

    public void setImagenCurso(String imagenCurso) {
        this.imagenCurso = imagenCurso;
    }
}
