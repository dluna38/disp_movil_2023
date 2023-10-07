package com.example.logintaller.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.logintaller.utils.HashTool;

@Entity
public class Usuario {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "nombre")
    private String name;

    @NonNull
    @ColumnInfo(name = "correo")
    private String correo;
    @NonNull
    @ColumnInfo(name = "contrasena")
    private String contrasena;


    public Usuario() {
    }

    public Usuario(int uid) {
        this.uid = uid;
    }

    public Usuario(String name, String correo, String contrasena) {
        this.name = name;
        this.correo = correo;
        this.contrasena = contrasena;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", correo='" + correo + '\'' +
                ", contrasena='" + contrasena + '\'' +
                '}';
    }
}
