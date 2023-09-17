package com.example.logintaller;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.logintaller.models.User;
import com.example.logintaller.services.UserService;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String EXTRA_INT_INDEX_USER = "com.example.logintaller.extra.INT.INDEX.USER";
    private EditText editTextEmail;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail = findViewById(R.id.editTextEmailLogIn);
        editTextPassword = findViewById(R.id.editTextPasswordLogIn);
    }

    public void startSignUp(View view) {
        Log.d(LOG_TAG, "ir a registro");
        Intent intent = new Intent(this, SignUpActivity.class);
        mStartForResult.launch(intent);
    }

    public void processLogIn(View view) {
        String correo = editTextEmail.getText().toString().trim();
        String contrasena = editTextPassword.getText().toString().trim();

        if (correo.isEmpty()) {
            showToast("El correo esta vacio");
            return;
        }
        if (contrasena.isEmpty()) {
            showToast("El contrasena esta vacio");
            return;
        }
        try {
            User user = UserService.getUsuarios().stream().filter(userBD -> userBD.getEmail().equals(correo)).findFirst().orElseThrow(NoSuchFieldError::new);

            if(user.getPassword().equals(contrasena)){
                Log.d(LOG_TAG,"Credenciales correctas");
                switchToUserScreen(UserService.getUsuarios().indexOf(user));
            }
            else {
                editTextPassword.getText().clear();
                showToast("Credenciales incorrectas");
            }

        } catch (NoSuchFieldError e) {
            showToast("No hay usuario registrados con ese correo");
            editTextEmail.getText().clear();
        }

    }

    private void switchToUserScreen(int indexUser){
        Intent intent = new Intent(this, homeUser.class);
        intent.putExtra(EXTRA_INT_INDEX_USER,indexUser);
        startActivity(intent);
    }
    private final ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Log.d(LOG_TAG, "SE RECIBE EL INTENT");

                    Intent intent = result.getData();
                    boolean goodForLogIn = intent.getBooleanExtra(SignUpActivity.EXTRA_BOOLEAN_LOGIN, false);
                    int indexUser =  intent.getIntExtra(SignUpActivity.EXTRA_INDEX_LOGIN,-1);

                    if(indexUser == -1) Log.wtf(LOG_TAG,"NO devolvio el index");

                    if (goodForLogIn){
                        Log.d(LOG_TAG, "iniciar Sesion");
                        switchToUserScreen(indexUser);
                    }
                    else Log.d(LOG_TAG, "Dejar en inicio");
                    Log.d(LOG_TAG, "USUARIOS: " + UserService.getUsuarios());
                }
    });


    private void showToast(String message){
        Toast.makeText(this, message,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        Log.d(LOG_TAG,"Main en pausa");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(LOG_TAG,"Main en stop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(LOG_TAG,"Main a destruir");
        super.onDestroy();
    }
}