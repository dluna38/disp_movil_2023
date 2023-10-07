package com.example.logintaller;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.logintaller.models.Usuario;
import com.example.logintaller.models.apiModels.Pelicula;
import com.example.logintaller.models.apiModels.SearchResponse;
import com.example.logintaller.services.TmbdService;
import com.example.logintaller.services.UsuarioRepository;
import com.example.logintaller.utils.MovieListAdapter;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeUser extends AppCompatActivity {

    private static final String LOG_TAG = HomeUser.class.getSimpleName();
    private TextView helloUserText;
    private TextView labelPagination;
    private SearchView txtSearch;
    private ProgressBar loadingBar;
    private Button btnAnterior;
    private Button btnSiguiente;

    private Usuario usuario=null;
    private RecyclerView mRecyclerView;

    private String currentSearch;
    private int currentPage;
    ArrayList<Pelicula> realMovieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_home_user);


        helloUserText = findViewById(R.id.txtHelloUser);
        labelPagination= findViewById(R.id.labelPagination);
        labelPagination.setVisibility(View.INVISIBLE);
        loadingBar = findViewById(R.id.loadingMedia);
        btnAnterior = findViewById(R.id.btnAnterior);
        btnAnterior.setVisibility(View.INVISIBLE);
        btnSiguiente = findViewById(R.id.btnSiguiente);
        btnSiguiente.setVisibility(View.INVISIBLE);
        txtSearch = findViewById(R.id.txtSearch);
        txtSearch.setSubmitButtonEnabled(true);
        txtSearch.setOnQueryTextListener(queryListener);

        setHelloUserText(getIntent());
        realMovieList = new ArrayList<>();


        // Get a handle to the RecyclerView.
        mRecyclerView = findViewById(R.id.recycleViewHome);
// Create an adapter and supply the data to be displayed.
// Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(new MovieListAdapter(this, realMovieList));
// Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void setHelloUserText(Intent intent){
        try {
            int id = intent.getIntExtra(MainActivity.EXTRA_INT_INDEX_USER, -1);
            if(id !=-1) {
                UsuarioRepository usuarioRepository = new UsuarioRepository(getApplicationContext());
                Optional<Usuario> usuarioDB = usuarioRepository.findUsuarioById(id);
                if (usuarioDB.isPresent()) {
                    usuario = usuarioDB.get();
                    helloUserText.setText(getString(R.string.hello_user_prefix, usuario.getName()));
                    return;
                }
            }
        } catch (Exception e) {
            Log.d(LOG_TAG,"No se pudo saludar al usuario: "+e.getMessage());
        }
        helloUserText.setText(getString(R.string.hello_user_prefix,"desconocido"));
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

    SearchView.OnQueryTextListener queryListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            Log.d(LOG_TAG,s);
            if(s.isEmpty() || s.length()<3){
                return false;
            }
            txtSearch.clearFocus();
            currentSearch=s.trim();
            makeSearch(currentSearch,1);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            return false;
        }
    };

    public void makeSearch(String query,int page){
        loadingBar.setVisibility(View.VISIBLE);
        TmbdService service = new TmbdService();
        Call<SearchResponse> callListaPeli = service.searchMedias(query,page);

        callListaPeli.enqueue(new Callback<SearchResponse>(){
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if(response.body() != null) {
                    SearchResponse respuesta = response.body();
                    respuesta.results = respuesta.results.stream().filter((movie) ->
                            Objects.equals(movie.media_type, Pelicula.MEDIA_TYPE_MOVIE) ||
                                    Objects.equals(movie.media_type, Pelicula.MEDIA_TYPE_SERIE)).collect(Collectors.toList());

                    currentPage=respuesta.page;
                    showBotones(respuesta.totalPages,respuesta.page);
                    realMovieList.clear();
                    Log.d(LOG_TAG, "onResponse: "+respuesta.results.size());
                    realMovieList.addAll(respuesta.results);
                    mRecyclerView.smoothScrollToPosition(0);
                    mRecyclerView.getAdapter().notifyDataSetChanged();
                    loadingBar.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Log.d(LOG_TAG,"mal: "+t.getMessage());
                loadingBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void showBotones(int totalPages,int actualPage){
        btnSiguiente.setVisibility(View.VISIBLE);
        btnAnterior.setVisibility(View.VISIBLE);
        labelPagination.setText(getString(R.string.pages_prefix,actualPage,totalPages));
        labelPagination.setVisibility(View.VISIBLE);
        if(totalPages==1){
            btnAnterior.setEnabled(false);
            btnSiguiente.setEnabled(false);
        } else if(actualPage==1) {
            btnAnterior.setEnabled(false);
            btnSiguiente.setEnabled(true);
        }else if (actualPage==totalPages){
            btnSiguiente.setEnabled(false);
            btnAnterior.setEnabled(true);
        }else{
            btnAnterior.setEnabled(true);
            btnSiguiente.setEnabled(true);
        }
    }

    public void showPopup(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.user_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(menuListener);
        popup.show();
    }

    private final PopupMenu.OnMenuItemClickListener menuListener = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            if(menuItem.getItemId() == R.id.close_session_user){
                Log.d(LOG_TAG,"click cerrar sesion");
                closeSession(null);
                return true;
            }else if (menuItem.getItemId() == R.id.config_user){
                Log.d(LOG_TAG,"click configuración");
                switchToConfig();
                return true;
            }

            return false;
        }
    };

    private void switchToConfig(){
        Intent intent = new Intent(this, ConfigUser.class);
        intent.putExtra(MainActivity.EXTRA_INT_INDEX_USER,usuario.getUid());
        intent.putExtra("nombre",usuario.getName());
        activityConfigLauncher.launch(intent);
    }


    private final ActivityResultLauncher<Intent> activityConfigLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Log.d(LOG_TAG, "SE RECIBE EL INTENT");

                    Intent intent = result.getData();
                    if(intent == null) return;
                    boolean updateText = intent.getBooleanExtra(ConfigUser.UPDATE_MADE_RESULT, false);
                    boolean deleteMade = intent.getBooleanExtra(ConfigUser.DELETE_MADE_RESULT, false);
                    if(deleteMade){
                        finish();
                    }
                    if(updateText){
                        Log.d(LOG_TAG, "SE INTENTA ACTUALIZAR TEXTO");
                        setHelloUserText(getIntent());
                    }else{
                        Log.d(LOG_TAG, "NO HUBO ACTUALIZACiÓN");
                    }
                }
            });

    public void nextPageSearch(View view) {
        makeSearch(currentSearch,++currentPage);
    }

    public void previousPageSearch(View view) {
        makeSearch(currentSearch,--currentPage);
    }
}