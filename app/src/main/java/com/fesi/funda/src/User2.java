package com.fesi.funda.src;


import android.widget.ImageView;

import com.squareup.picasso.RequestCreator;
import java.io.Serializable;

public class User2 implements Serializable {
    private static User2 instance = new User2();
    private String idUser;
    private String diUser;
    private String nacimientoUser;
    private String generoUser;
    private String emailUser;
    private String nameUser;
    private String photoUser;
    private String phoneUser;
    private String direccionUser;
    private ImageView img_user;

    private User2(){}

    public static User2 getInstance(){
        return instance;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getDiUser() {
        return diUser;
    }

    public void setDiUser(String diUser) {
        this.diUser = diUser;
    }

    public String getNacimientoUser() {
        return nacimientoUser;
    }

    public void setNacimientoUser(String nacimientoUser) {
        this.nacimientoUser = nacimientoUser;
    }

    public String getGeneroUser() {
        return generoUser;
    }

    public void setGeneroUser(String generoUser) {
        this.generoUser = generoUser;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getPhotoUser() {
        return photoUser;
    }

    public void setPhotoUser(String photoUser) {
        this.photoUser = photoUser;
    }

    public String getPhoneUser() {
        return phoneUser;
    }

    public void setPhoneUser(String phoneUser) {
        this.phoneUser = phoneUser;
    }

    public String getDireccionUser() {
        return direccionUser;
    }

    public void setDireccionUser(String direccionUser) {
        this.direccionUser = direccionUser;
    }

    public ImageView getImg_user() {
        return img_user;
    }

    public void setImg_user(ImageView img_user) {
        this.img_user = img_user;
    }
}