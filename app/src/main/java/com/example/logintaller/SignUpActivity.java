package com.example.logintaller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.logintaller.models.User;
import com.example.logintaller.services.UserService;

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
        setContentView(R.layout.activity_sign_up);

        editTextNombre = findViewById(R.id.editTextNameSignUp);
        editTextCorreo = findViewById(R.id.editTextEmailSignUp);
        editTextPassword = findViewById(R.id.editTextPassSignUp);
    }

    public void processSignUp(View view) {
        String nombre = editTextNombre.getText().toString();
        String correo = editTextCorreo.getText().toString();
        String contrasena = editTextPassword.getText().toString();
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

        User newUser = new User(nombre.trim(),correo.trim(),contrasena.trim());
        UserService.saveUser(newUser);

        Intent replyIntent= new Intent();
        replyIntent.putExtra(EXTRA_BOOLEAN_LOGIN,true);
        replyIntent.putExtra(EXTRA_INDEX_LOGIN,UserService.getUsuarios().size()-1);
        setResult(RESULT_OK,replyIntent); //
        finish();
    }

    private void showToast(String message){
        Toast.makeText(this, message,
                Toast.LENGTH_SHORT).show();
    }
}