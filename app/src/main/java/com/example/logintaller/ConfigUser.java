package com.example.logintaller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.logintaller.models.Usuario;
import com.example.logintaller.services.UsuarioRepository;

public class ConfigUser extends AppCompatActivity {
    TextView txtInputName;
    private final String TAG = ConfigUser.class.getSimpleName();
    public static final String UPDATE_MADE_RESULT = "UPDATE_MADE_RESULT";
    public static final String DELETE_MADE_RESULT = "DELETE_MADE_RESULT";
    private int IdUser;
    private boolean updateMade=false;
    private boolean deleteMade=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_config_user);

        Intent fromHome = getIntent();
        txtInputName = findViewById(R.id.txtUpdateName);
        txtInputName.setText(fromHome.getStringExtra("nombre"));
        IdUser = fromHome.getIntExtra(MainActivity.EXTRA_INT_INDEX_USER,-1);
    }

    public void cancelarConfig(View view) {
        finishActivity();
    }

    public void makeUpdate(View view) {
        String nombre = txtInputName.getText().toString().trim();
        if(nombre.isEmpty()){
            showToast("Nombre vacio");
            return;
        }
        if(IdUser != -1){
            UsuarioRepository usuarioRepository = new UsuarioRepository(this);
            Log.d(TAG, String.format("makeUpdate: %s - %s", nombre,IdUser));
            if(usuarioRepository.updateUsuario(nombre,IdUser)){
                showToast("Usuario actualizado");
                updateMade=true;
            }else {
                showToast("No se pudo actualizar");
            }
            return;
        }
        showToast("Ocurrio un error, vuelve a iniciar sesión");
    }

    private void finishActivity(){
        Intent replyIntent = new Intent();
        replyIntent.putExtra(UPDATE_MADE_RESULT,updateMade);
        replyIntent.putExtra(DELETE_MADE_RESULT,deleteMade);
        setResult(RESULT_OK,replyIntent);
        finish();
    }
    private void showToast(String message){
        Toast.makeText(this, message,
                Toast.LENGTH_SHORT).show();
    }

    public void borrarCuenta(View view) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setTitle("Borrrar Cuenta");
        alertBuilder.setMessage("¿Estas seguro de borrar tu cuenta?");

        alertBuilder.setPositiveButton("SI", (dialog, which) -> {
            if(!deleteAccount()){
                showToast("No se pudo borrar la cuenta");
                return;
            }
            deleteMade=true;
            showToast("Cuenta borrada");
            finishActivity();
        });
        alertBuilder.setNegativeButton("NO", (dialog, which) -> {
        });
        alertBuilder.show();
    }

    private boolean deleteAccount(){
        if(IdUser == -1) return false;
        UsuarioRepository usuarioRepository = new UsuarioRepository(this);
        return usuarioRepository.deleteUsuarioById(IdUser);
    }
}