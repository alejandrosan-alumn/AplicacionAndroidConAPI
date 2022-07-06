package com.example.QueMeVeo.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.QueMeVeo.Datos.Result;
import com.example.practicaandroid.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PeliculasPopularesAdapter extends RecyclerView.Adapter<PeliculasPopularesAdapter.PeliculasPopularesViewHolder> {

    private List<Result> datos = new ArrayList<>();
    private final OnItemClickListener listener;

    public void setDatos(List<Result> datos){this.datos = datos;}

    public interface OnItemClickListener{
        void onItemClick(Result item);
    }

    public static class PeliculasPopularesViewHolder extends RecyclerView.ViewHolder{

        private ImageView cartelPeliculaPopular;
        private TextView nombrePeliculaPopular;
        private RatingBar valoracionPeliculaPopular;

        public PeliculasPopularesViewHolder(View itemView){

            super(itemView);
            cartelPeliculaPopular = (ImageView)itemView.findViewById(R.id.imageView);
            nombrePeliculaPopular = (TextView)itemView.findViewById(R.id.tv_name);
            valoracionPeliculaPopular = (RatingBar) itemView.findViewById(R.id.ratingBar);
        }

        public void bindPeliculasPopulares(Result j, final OnItemClickListener listener){

            String urlPosterPP = "http://image.tmdb.org/t/p/w500" + j.getPosterPath();
            Picasso.get().load(urlPosterPP).into(cartelPeliculaPopular);
            nombrePeliculaPopular.setText(j.getTitle());
            valoracionPeliculaPopular.setNumStars(5);
            valoracionPeliculaPopular.setStepSize((float) 0.5);
            valoracionPeliculaPopular.setRating(j.getVoteAverage().floatValue()/2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(j);
                }
            });
        }
    }
    public PeliculasPopularesAdapter(List<Result> datos, OnItemClickListener listener){

        this.datos = datos;
        this.listener = listener;
    }

    @Override
    public PeliculasPopularesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_movie_item_roundedcorners, viewGroup, false);
        PeliculasPopularesViewHolder peliculasPopularesVH = new PeliculasPopularesViewHolder(itemView);
        return peliculasPopularesVH;
    }

    @Override
    public void onBindViewHolder(PeliculasPopularesViewHolder viewHolder, int pos){

        Result j = datos.get(pos);
        viewHolder.bindPeliculasPopulares(j,listener);
    }

    @Override
    public int getItemCount(){

        return datos.size();
    }
}
