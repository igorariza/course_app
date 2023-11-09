package com.fesi.funda.src;

import java.io.Serializable;
import java.util.List;

public class Carreras implements Serializable {

    private String nombreCarrera;
    private String _idCarrera;
    private List<Cursos> cursos;
    private String imgCarrera;
    private String urlImg;
    private String descripcionCarrera;
    private int cantidadCursos;

    public Carreras() {
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

    public List<Cursos> getCursos() {
        return cursos;
    }

    public void setCursos(List<Cursos> cursos) {
        this.cursos = cursos;
    }

    public int getCantidadCursos() {
        return cantidadCursos;
    }

    public void setCantidadCursos(int cantidadCursos) {
        this.cantidadCursos = cantidadCursos;
    }

    public String getImgCarrera() {
        return imgCarrera;
    }

    public void setImgCarrera(String imgCarrera) {
        this.imgCarrera = imgCarrera;
    }

    public String getDescripcionCarrera() {
        return descripcionCarrera;
    }

    public void setDescripcionCarrera(String descripcionCarrera) {
        this.descripcionCarrera = descripcionCarrera;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }
}
