package com.example.logintaller.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import java.util.Optional;

@Dao
public interface UsuarioDao {
    @Query("SELECT * FROM usuario")
    List<Usuario> getAll();

    @Query("SELECT * FROM usuario WHERE uid IN (:userIds)")
    List<Usuario> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM usuario WHERE nombre LIKE :nombre")
    Usuario findByName(String nombre);

    @Query("SELECT * FROM usuario WHERE UPPER(correo) = UPPER(:correo)")
    Optional<Usuario> findByCorreo(String correo);
    @Query("SELECT * FROM usuario WHERE uid = :uid")
    Optional<Usuario> findById(int uid);
    @Insert
    void insertOne(Usuario usuario);
    @Insert
    void insertAll(Usuario... usuarios);

    @Delete
    void delete(Usuario usuario);
    @Query("DELETE FROM usuario WHERE uid = :id")
    void deleteUsuarioById(int id);
    @Query("UPDATE usuario SET nombre = :newName WHERE uid = :uid")
    void updateName(String newName,int uid);

    @Query("DELETE FROM usuario")
    void deleteAll();
}
