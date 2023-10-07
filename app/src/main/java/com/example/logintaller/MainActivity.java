package com.example.logintaller;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.example.logintaller.models.apiModels.Pelicula;
import com.example.logintaller.models.Usuario;
import com.example.logintaller.models.apiModels.SearchResponse;
import com.example.logintaller.services.TmbdApi;
import com.example.logintaller.services.TmbdService;
import com.example.logintaller.services.UsuarioRepository;
import com.example.logintaller.utils.HashTool;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String EXTRA_INT_INDEX_USER = "com.example.logintaller.extra.INT.INDEX.USER";
    private EditText editTextEmail;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_main);

        /*final View content = findViewById(android.R.id.content);
        content.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        // Check whether the initial data is ready.
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        } finally {
                            content.getViewTreeObserver().removeOnPreDrawListener(this);
                            return true;
                        }

                    }
                });*/


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

            UsuarioRepository usuarioRepository = new UsuarioRepository(getApplicationContext());
            Optional<Usuario> usuarioDB = usuarioRepository.findUsuarioByCorreo(correo);

            if(!usuarioDB.isPresent()) throw new IllegalAccessException();

            Log.d(LOG_TAG,usuarioDB.get().toString());
            if(HashTool.HashEqual(contrasena,usuarioDB.get().getContrasena())){
                Log.d(LOG_TAG,"Credenciales correctas");
                switchToUserScreen(usuarioDB.get().getUid());
            }
            else {
                editTextPassword.getText().clear();
                showToast("Credenciales incorrectas");
            }

        }catch (ExecutionException | InterruptedException | TimeoutException e) {
            showToast("Ocurrio un error iniciando sesión");
            Log.d(LOG_TAG,"Ocurrio un error buscando el correo"+e.getMessage());
        } catch (IllegalAccessException e) {
            showToast("No hay usuario registrados con ese correo");
            editTextEmail.getText().clear();
        }

    }

    private void switchToUserScreen(int indexUser){
        editTextEmail.setText("");
        editTextPassword.setText("");
        Intent intent = new Intent(this, HomeUser.class);
        intent.putExtra(EXTRA_INT_INDEX_USER,indexUser);
        startActivity(intent);
    }


    public void onClickTestBtn(View view) {
        /*UsuarioRepository usuarioRepository = new UsuarioRepository(getApplicationContext());
        Log.d(LOG_TAG,usuarioRepository.getAllUsuarios().toString());*/


        Log.d(LOG_TAG,"finish method");
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