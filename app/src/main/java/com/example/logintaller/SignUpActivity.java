package com.example.logintaller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.logintaller.models.Usuario;
import com.example.logintaller.services.UsuarioRepository;
import com.example.logintaller.utils.HashTool;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class SignUpActivity extends AppCompatActivity {
    private static final String LOG_TAG= SignUpActivity.class.getSimpleName();
    public static final String EXTRA_BOOLEAN_LOGIN = "com.example.logintaller.extra.BOOLEAN.LOGIN";
    public static final String EXTRA_INDEX_LOGIN = "com.example.logintaller.extra.INDEX.LOGIN";

    private EditText editTextNombre;
    private EditText editTextCorreo;
    private EditText editTextPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_sign_up);

        editTextNombre = findViewById(R.id.txtUpdateName);
        editTextCorreo = findViewById(R.id.editTextEmailSignUp);
        editTextPassword = findViewById(R.id.editTextPassSignUp);
    }

    public void processSignUp(View view) {
        String nombre = editTextNombre.getText().toString().trim();
        String correo = editTextCorreo.getText().toString().trim();
        String contrasena = editTextPassword.getText().toString().trim();
        if(nombre.isEmpty()){
            showToast("El nombre esta vacio");
            return;
        }
        if(correo.isEmpty()){
            showToast("El correo esta vacio");
            return;
        }
        if(contrasena.isEmpty()){
            showToast("La contraseña esta vacia");
            return;
        }
        //check if the email already exist
        Log.d(LOG_TAG,"Paso verificaciones");

        try {
            UsuarioRepository usuarioRepository = new UsuarioRepository(getApplicationContext());


            Optional<Usuario> usuarioBD = usuarioRepository.findUsuarioByCorreo(correo);
            if (usuarioBD.isPresent()) {
                showToast("El correo ya esta registrado");
                return;
            }

            usuarioRepository.saveUsuario(new Usuario(nombre, correo, HashTool.obtenerHexSha(contrasena, HashTool.EXTRA_SALT)));

            usuarioBD = usuarioRepository.findUsuarioByCorreo(correo);
            if (!usuarioBD.isPresent()) {
                throw new IllegalAccessException();
            }

            Intent replyIntent= new Intent();
            replyIntent.putExtra(EXTRA_BOOLEAN_LOGIN,true);
            replyIntent.putExtra(EXTRA_INDEX_LOGIN,usuarioBD.get().getUid());
            setResult(RESULT_OK,replyIntent); //
            finish();
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            showToast("Ocurrio un error registrandose");
            Log.d(LOG_TAG,"Ocurrio un error buscando el correo"+e.getMessage());
            return;
        } catch (IllegalAccessException e) {
            showToast("Ocurrio un error registrandose");
            return;
        }
        finish();
    }

    private void showToast(String message){
        Toast.makeText(this, message,
                Toast.LENGTH_SHORT).show();
    }

    public void cancelSignUp(View view) {
        finish();
    }
}