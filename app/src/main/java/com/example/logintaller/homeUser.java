package com.example.logintaller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.logintaller.models.User;
import com.example.logintaller.services.UserService;

public class homeUser extends AppCompatActivity {

    private static final String LOG_TAG = homeUser.class.getSimpleName();
    private TextView helloUserText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);
        helloUserText = findViewById(R.id.txtHelloUser);
        setHelloUserText(getIntent());
    }

    private void setHelloUserText(Intent intent){

        int index = intent.getIntExtra(MainActivity.EXTRA_INT_INDEX_USER, -1);
        if(index !=-1){
            User user = UserService.getUsuarios().get(index);
            helloUserText.setText(getString(R.string.hello_user_prefix,user.getNombre()));
            return;
        }
        Log.d(LOG_TAG,"No se pudo saludar al usuario");
    }


    @Override
    public void onBackPressed() {
        //or finish(), the previous activity after the starActivity
        //or just put return on this method;
        moveTaskToBack(true);
    }

    public void closeSession(View view) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setTitle("¿Cerrar Sesión?");
        alertBuilder.setMessage("¿Estas seguro de cerrar sesión?");

        alertBuilder.setPositiveButton("OK", (dialog, which) -> {
            Toast.makeText(getApplicationContext(), "Se cerro la sesión",
                    Toast.LENGTH_SHORT).show();
            finish();
        });
        alertBuilder.setNegativeButton("Cancelar", (dialog, which) -> {

        });

        alertBuilder.show();
    }
}