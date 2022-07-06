package com.example.QueMeVeo.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.QueMeVeo.Datos.ResultSeries;
import com.example.practicaandroid.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.SeriesViewHolder> {

    private List<ResultSeries> datos;
    private final SeriesAdapter.OnItemClickListener listener;

    public void setDatos(List<ResultSeries> datos){this.datos = datos;}

    public interface OnItemClickListener{
        void OnItemClick(ResultSeries item);
    }

    public static class SeriesViewHolder extends RecyclerView.ViewHolder{

        private ImageView cartelSeriePopular;
        private TextView nombreSeriePopular;
        private RatingBar valoracionSeriePopular;

        public SeriesViewHolder(View itemView){

            super (itemView);
            cartelSeriePopular = (ImageView) itemView.findViewById(R.id.imageView);
            nombreSeriePopular = (TextView) itemView.findViewById(R.id.tv_name);
            valoracionSeriePopular = (RatingBar) itemView.findViewById(R.id.ratingBar);
        }

        public void bindSeries(ResultSeries j, final SeriesAdapter.OnItemClickListener listener){

            String urlPosterSerie = "http://image.tmdb.org/t/p/w500" + j.getPosterPath();
            Picasso.get().load(urlPosterSerie).into(cartelSeriePopular);
            nombreSeriePopular.setText(j.getName());
            valoracionSeriePopular.setNumStars(5);
            valoracionSeriePopular.setStepSize((float) 0.5);
            valoracionSeriePopular.setRating(j.getVoteAverage().floatValue()/2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    listener.OnItemClick(j);
                }
            });
        }
    }
    public SeriesAdapter(List<ResultSeries> datos, SeriesAdapter.OnItemClickListener listener){

        this.datos = datos;
        this.listener = listener;
    }

    @Override
    public SeriesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_movie_item_roundedcorners, viewGroup, false);
        SeriesViewHolder seriesVH = new SeriesViewHolder(itemView);
        return seriesVH;
    }

    @Override
    public void onBindViewHolder(SeriesViewHolder viewHolder, int pos){

        ResultSeries j = datos.get(pos);
        viewHolder.bindSeries(j, listener);
    }

    @Override
    public int getItemCount(){

        return datos.size();
    }
}
