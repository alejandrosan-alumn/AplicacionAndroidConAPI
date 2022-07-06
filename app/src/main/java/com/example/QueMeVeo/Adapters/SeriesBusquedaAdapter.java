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

public class SeriesBusquedaAdapter extends RecyclerView.Adapter<SeriesBusquedaAdapter.SeriesBusquedaViewHolder> {

    private List<ResultSeries> datos;
    private final SeriesBusquedaAdapter.OnItemClickListener listener;

    public void setDatos(List<ResultSeries> datos){this.datos = datos;}

    public interface OnItemClickListener{
        void OnItemClick(ResultSeries item);
    }

    public static class SeriesBusquedaViewHolder extends RecyclerView.ViewHolder{

        private ImageView cartelSeriePopular;
        private TextView nombreSeriePopular;

        public SeriesBusquedaViewHolder(View itemView){

            super (itemView);
            cartelSeriePopular = (ImageView) itemView.findViewById(R.id.imageView);
            nombreSeriePopular = (TextView) itemView.findViewById(R.id.tv_name);
        }

        public void bindSeriesBusqueda(ResultSeries j, final SeriesBusquedaAdapter.OnItemClickListener listener){

            String urlPosterSerie = "http://image.tmdb.org/t/p/w500" + j.getPosterPath();
            Picasso.get().load(urlPosterSerie).into(cartelSeriePopular);
            nombreSeriePopular.setText(j.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    listener.OnItemClick(j);
                }
            });
        }
    }
    public SeriesBusquedaAdapter(List<ResultSeries> datos, SeriesBusquedaAdapter.OnItemClickListener listener){

        this.datos = datos;
        this.listener = listener;
    }

    @Override
    public SeriesBusquedaAdapter.SeriesBusquedaViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.find_item_layout, viewGroup, false);
        SeriesBusquedaAdapter.SeriesBusquedaViewHolder seriesB = new SeriesBusquedaAdapter.SeriesBusquedaViewHolder(itemView);
        return seriesB;
    }

    @Override
    public void onBindViewHolder(SeriesBusquedaAdapter.SeriesBusquedaViewHolder viewHolder, int pos){

        ResultSeries j = datos.get(pos);
        viewHolder.bindSeriesBusqueda(j, listener);
    }

    @Override
    public int getItemCount(){

        return datos.size();
    }
}