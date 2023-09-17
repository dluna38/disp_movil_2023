package com.example.logintaller.services;

import com.example.logintaller.models.User;

import java.util.ArrayList;
import java.util.List;

public abstract class UserService {
    private static List<User> usuarios;


    public static boolean saveUser(User usuario){
        getUsuarios().add(usuario);
        return true;
    }

    public static List<User> getUsuarios() {
        if(usuarios == null){
            usuarios = new ArrayList<>();
        }
        return usuarios;
    }

}
