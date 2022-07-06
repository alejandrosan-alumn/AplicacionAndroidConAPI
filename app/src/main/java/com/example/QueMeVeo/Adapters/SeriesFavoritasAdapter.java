package com.example.QueMeVeo.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.QueMeVeo.Datos.SeriesDetalles;
import com.example.practicaandroid.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SeriesFavoritasAdapter extends RecyclerView.Adapter<SeriesFavoritasAdapter.SeriesFavoritasViewHolder>{

    private List<SeriesDetalles> seriesFavoritas = new ArrayList<>();
    private final SeriesFavoritasAdapter.OnItemClickListener listener;

    public void AddDatos(SeriesDetalles serieFav ){
        seriesFavoritas.add(serieFav);
        this.notifyDataSetChanged();
    }

    public interface OnItemClickListener{
        void onItemClick(SeriesDetalles item);
    }
    public static class SeriesFavoritasViewHolder extends RecyclerView.ViewHolder{

        private ImageView posterSerieFavorita;
        private TextView nombreSerieFavorita;
        private RatingBar ratingSerieFavorita;

        public SeriesFavoritasViewHolder(View itemView){

            super (itemView);
            posterSerieFavorita = (ImageView) itemView.findViewById(R.id.imageView);
            nombreSerieFavorita = (TextView) itemView.findViewById(R.id.tv_name);
            ratingSerieFavorita = (RatingBar) itemView.findViewById(R.id.ratingBar);
        }
        public void bindSeriesFavorito(SeriesDetalles j, final SeriesFavoritasAdapter.OnItemClickListener listener){

            String urlPosterF = "http://image.tmdb.org/t/p/w500" + j.getPosterPath();
            Picasso.get().load(urlPosterF).into(posterSerieFavorita);
            nombreSerieFavorita.setText(j.getName());
            ratingSerieFavorita.setNumStars(5);
            ratingSerieFavorita.setStepSize((float) 0.5);
            //ratingSerieFavorita.setRating(j.getVoteAverage().floatValue()/2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(j);
                }
            });
        }
    }

    public SeriesFavoritasAdapter(SeriesDetalles datos, SeriesFavoritasAdapter.OnItemClickListener listener){
        this.AddDatos(datos);
        this.listener = listener;
    }
    @Override
    public SeriesFavoritasAdapter.SeriesFavoritasViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_movie_item_roundedcorners, viewGroup, false);
        SeriesFavoritasAdapter.SeriesFavoritasViewHolder seriesFavoritasVH = new SeriesFavoritasAdapter.SeriesFavoritasViewHolder(itemView);
        return seriesFavoritasVH;
    }
    @Override
    public void onBindViewHolder(SeriesFavoritasAdapter.SeriesFavoritasViewHolder viewHolder, int pos){

        SeriesDetalles j = this.seriesFavoritas.get(pos);
        viewHolder.bindSeriesFavorito(j,listener);
    }

    @Override
    public int getItemCount(){

        return seriesFavoritas.size();
    }
}
