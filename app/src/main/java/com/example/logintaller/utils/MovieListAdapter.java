package com.example.logintaller.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.logintaller.R;
import com.example.logintaller.models.apiModels.Pelicula;
import com.example.logintaller.services.TmbdService;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    public static final String LOG_TAG=MovieListAdapter.class.getSimpleName();
    private final List<Pelicula> realMovieList;
    private LayoutInflater mInflater;

    private final Context mContext;
    public MovieListAdapter(Context context,
                            List<Pelicula> realMovieList) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        this.realMovieList = realMovieList;
    }

    @NonNull
    @Override
    public MovieListAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.movielist_itemx,
                parent, false);
        return new MovieViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Pelicula mCurrent = realMovieList.get(position);

        holder.makeBind(mCurrent);
    }


    @Override
    public int getItemCount() {
        return realMovieList.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView txtTitle;
        //public final TextView txtYear;
        public final TextView txtMovieType;
        public final ImageView movieImage;
        final MovieListAdapter mAdapter;

        MovieViewHolder(View itemView, MovieListAdapter adapter) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.title);
            //txtYear = itemView.findViewById(R.id.txt_year);
            movieImage = itemView.findViewById(R.id.movieImage);
            txtMovieType = itemView.findViewById(R.id.txtMovieType);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        void makeBind(Pelicula pelicula){
            if(pelicula.media_type.equals(Pelicula.MEDIA_TYPE_MOVIE)) {
                txtTitle.setText(pelicula.title);
                txtMovieType.setText(String.format("Pelicula (%s)",pelicula.getReleaseYear()));
                txtMovieType.setBackgroundColor(mContext.getResources().getColor(R.color.movieTypeColor, mContext.getTheme()));
            }else{
                txtTitle.setText(pelicula.name);
                txtMovieType.setText(String.format("Serie (%s)",pelicula.getReleaseYear()));
                txtMovieType.setBackgroundColor(mContext.getResources().getColor(R.color.serieTypeColor, mContext.getTheme()));
            }

            //txtYear.setText(pelicula.getReleaseYear());
            if (pelicula.poster_path == null){
                Glide.with(mContext).load(AppCompatResources.getDrawable(mContext,R.drawable.image_not_available)).into(movieImage);
            }else {
                Glide.with(mContext).load(TmbdService.getGlideUrl(pelicula.poster_path)).into(movieImage);
            }
        }


        @Override
        public void onClick(View view) {
            // Get the position of the item that was clicked.
            int mPosition = getLayoutPosition();
// Use that to access the affected item in mWordList.
            Pelicula element = realMovieList.get(mPosition);
// Change the word in the mWordList.
            Log.d(LOG_TAG,element.toString());
// Notify the adapter that the data has changed so it can
// update the RecyclerView to display the data.
        }
    }
}
