package com.example.logintaller.services;


import android.content.Context;
import android.util.Log;

import com.example.logintaller.databases.AppDatabase;
import com.example.logintaller.models.Usuario;
import com.example.logintaller.models.UsuarioDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class UsuarioRepository {
    public final String LOG_TAG = UsuarioRepository.class.getSimpleName();
    private static ExecutorService executorService=null;
    private Context context;
    public UsuarioRepository(Context context) {
        getExecutor();
        this.context=context;
    }
    private ExecutorService getExecutor(){
        if(executorService == null || executorService.isShutdown()){
            executorService = Executors.newSingleThreadExecutor();
        }
        return executorService;
    }
    private UsuarioDao getUsuarioDao(){
        return AppDatabase.getDatabase(context).usuarioDao();
    }


    public List<Usuario> getAllUsuarios(){

        try {
            Future<List<Usuario>> expectedList = getExecutor().submit(() -> getUsuarioDao().getAll());
            return expectedList.get(1, TimeUnit.SECONDS);
        }catch (ExecutionException | InterruptedException | TimeoutException e) {
            Log.d(LOG_TAG,"se espero y no se recupero"+e.getMessage());
        }
        return new ArrayList<>();
    }

    public boolean saveUsuario(Usuario usuario){
        try {
            getExecutor().submit(()->getUsuarioDao().insertOne(usuario));
            return true;
        } catch (Exception e) {
            Log.d(LOG_TAG,"Ocurrio un error guardando el usuario"+e.getMessage());
            return false;
        }
    }
    public boolean updateUsuario(String newName,int id){
        try {
            getExecutor().submit(()->getUsuarioDao().updateName(newName,id));
            return true;
        } catch (Exception e) {
            Log.d(LOG_TAG,"Ocurrio un error actualizando el usuario"+e.getMessage());
            return false;
        }
    }
    public boolean deleteUsuarioById(int id){
        try {
            getExecutor().submit(()->getUsuarioDao().deleteUsuarioById(id));
            return true;
        } catch (Exception e) {
            Log.d(LOG_TAG,"Ocurrio un error actualizando el usuario"+e.getMessage());
            return false;
        }
    }

    public Optional<Usuario> findUsuarioByCorreo(String correo) throws ExecutionException, InterruptedException, TimeoutException {
        Future<Optional<Usuario>> futUsuario = getExecutor().submit(() -> getUsuarioDao().findByCorreo(correo));
        return futUsuario.get(1,TimeUnit.SECONDS);
    }
    public Optional<Usuario> findUsuarioById(int id) throws ExecutionException, InterruptedException, TimeoutException {
        Future<Optional<Usuario>> futUsuario = getExecutor().submit(() -> getUsuarioDao().findById(id));
        return futUsuario.get(1,TimeUnit.SECONDS);
    }

    public boolean deleteUsuario(Usuario usuario){
        try {
            getExecutor().submit(()->getUsuarioDao().delete(usuario));
            return true;
        } catch (Exception e) {
            Log.d(LOG_TAG,"Ocurrio un error borrando el usuario"+e.getMessage());
            return false;
        }
    }
}
