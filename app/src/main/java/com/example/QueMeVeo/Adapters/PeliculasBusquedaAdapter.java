package com.example.QueMeVeo.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.QueMeVeo.Datos.PeliculasDetalles;
import com.example.QueMeVeo.Datos.Result;
import com.example.practicaandroid.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PeliculasBusquedaAdapter extends RecyclerView.Adapter<PeliculasBusquedaAdapter.PeliculasBusquedaViewHolder> {

    private List<Result> datos = new ArrayList<>();
    private final PeliculasBusquedaAdapter.OnItemClickListener listener;

    public void setDatos(List<Result> datos){this.datos = datos;}

    public interface OnItemClickListener{
        void onItemClick(Result item);
    }

    public static class PeliculasBusquedaViewHolder extends RecyclerView.ViewHolder{

        private ImageView cartelPeliculaBusqueda;
        private TextView nombrePeliculaBusqueda;

        public PeliculasBusquedaViewHolder(View itemView){

            super(itemView);
            cartelPeliculaBusqueda = (ImageView)itemView.findViewById(R.id.imageView);
            nombrePeliculaBusqueda = (TextView)itemView.findViewById(R.id.tv_name);
        }

        public void bindPeliculasBusqueda(Result j, final PeliculasBusquedaAdapter.OnItemClickListener listener){

            String urlPosterPP = "http://image.tmdb.org/t/p/w500" + j.getPosterPath();
            Picasso.get().load(urlPosterPP).into(cartelPeliculaBusqueda);
            nombrePeliculaBusqueda.setText(j.getTitle());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(j);
                }
            });
        }
    }
    public PeliculasBusquedaAdapter(List<Result> datos, PeliculasBusquedaAdapter.OnItemClickListener listener){

        this.datos = datos;
        this.listener = listener;
    }

    @Override
    public PeliculasBusquedaAdapter.PeliculasBusquedaViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.find_item_layout, viewGroup, false);
        PeliculasBusquedaAdapter.PeliculasBusquedaViewHolder peliculasPopularesBH = new PeliculasBusquedaAdapter.PeliculasBusquedaViewHolder(itemView);
        return peliculasPopularesBH;
    }

    @Override
    public void onBindViewHolder(PeliculasBusquedaAdapter.PeliculasBusquedaViewHolder viewHolder, int pos){

        Result j = datos.get(pos);
        viewHolder.bindPeliculasBusqueda(j,listener);
    }

    @Override
    public int getItemCount(){

        return datos.size();
    }
}
