package com.example.logintaller.databases;


import androidx.room.Database;

import com.example.logintaller.models.Usuario;
import com.example.logintaller.models.UsuarioDao;

@Database(entities = {Usuario.class},version = 2)
public abstract class RoomDatabaseInstance extends androidx.room.RoomDatabase {
    public abstract UsuarioDao usuarioDao();
}
