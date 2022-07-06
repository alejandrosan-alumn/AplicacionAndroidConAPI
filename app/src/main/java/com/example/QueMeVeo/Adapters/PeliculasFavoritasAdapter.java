package com.example.QueMeVeo.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.QueMeVeo.Datos.PeliculasDetalles;
import com.example.practicaandroid.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PeliculasFavoritasAdapter extends RecyclerView.Adapter<PeliculasFavoritasAdapter.PeliculasFavoritasViewHolder> {

    private List<PeliculasDetalles> peliculasFavoritas = new ArrayList<>();
    private final PeliculasFavoritasAdapter.OnItemClickListener listener;

    public void AddDatos(PeliculasDetalles peliculaFav ){
        peliculasFavoritas.add(peliculaFav);
        this.notifyDataSetChanged();
    }
    public interface OnItemClickListener{
        void onItemClick(PeliculasDetalles item);
    }
    public static class PeliculasFavoritasViewHolder extends RecyclerView.ViewHolder{

        private ImageView posterPeliculaFavorita;
        private TextView nombrePeliculaFavorita;
        private RatingBar ratingPeliculaFavorita;

        public PeliculasFavoritasViewHolder(View itemView){

            super (itemView);
            posterPeliculaFavorita = (ImageView) itemView.findViewById(R.id.imageView);
            nombrePeliculaFavorita = (TextView) itemView.findViewById(R.id.tv_name);
            ratingPeliculaFavorita = (RatingBar) itemView.findViewById(R.id.ratingBar);
        }
        public void bindPeliculasFavorito(PeliculasDetalles j, final OnItemClickListener listener){

            String urlPosterF = "http://image.tmdb.org/t/p/w500" + j.getPosterPath();
            Picasso.get().load(urlPosterF).into(posterPeliculaFavorita);
            nombrePeliculaFavorita.setText(j.getTitle());
            ratingPeliculaFavorita.setNumStars(5);
            ratingPeliculaFavorita.setStepSize((float) 0.5);
            //ratingPeliculaFavorita.setRating(j.getVoteAverage().floatValue()/2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(j);
                }
            });
        }
    }

    public PeliculasFavoritasAdapter(PeliculasDetalles datos, OnItemClickListener listener){
        this.AddDatos(datos);
        this.listener = listener;
    }
    @Override
    public PeliculasFavoritasViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_movie_item_roundedcorners, viewGroup, false);
        PeliculasFavoritasViewHolder peliculasFavoritasVH = new PeliculasFavoritasAdapter.PeliculasFavoritasViewHolder(itemView);
        return peliculasFavoritasVH;
    }
    @Override
    public void onBindViewHolder(PeliculasFavoritasAdapter.PeliculasFavoritasViewHolder viewHolder, int pos){

        PeliculasDetalles j = this.peliculasFavoritas.get(pos);
        viewHolder.bindPeliculasFavorito(j,listener);
    }

    @Override
    public int getItemCount(){

        return peliculasFavoritas.size();
    }
}
