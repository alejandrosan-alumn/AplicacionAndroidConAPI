package com.example.QueMeVeo.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.QueMeVeo.Datos.Cast;
import com.example.practicaandroid.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ActoresPeliculasAdapter extends RecyclerView.Adapter<ActoresPeliculasAdapter.ActoresPeliculaViewHolder> {

    private List<Cast> datos;
    private final ActoresPeliculasAdapter.OnItemClickListener listener;

    public void setDatos(List<Cast> datos){this.datos = datos;}

    public interface OnItemClickListener{
        void onItemClick(Cast item);
    }

    public static class ActoresPeliculaViewHolder extends RecyclerView.ViewHolder{

        private ImageView fotoActor;
        private TextView nombreActor;

        public ActoresPeliculaViewHolder(View itemView){

            super(itemView);
            fotoActor = (ImageView) itemView.findViewById(R.id.imageView);
            nombreActor = (TextView) itemView.findViewById(R.id.tv_name);
        }
        public void bindActoresPelicula(Cast j, final ActoresPeliculasAdapter.OnItemClickListener listener){

            String urlFoto = "http://image.tmdb.org/t/p/w500" + j.getProfilePath();
            Picasso.get().load(urlFoto).into(fotoActor);
            nombreActor.setText(j.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(j);
                }
            });
        }
    }
    public ActoresPeliculasAdapter(List<Cast> datos, OnItemClickListener listener){

        this.datos = datos;
        this.listener = listener;
    }

    @Override
    public ActoresPeliculaViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_cast_item, viewGroup, false);
        ActoresPeliculaViewHolder ActoresPeliculaVH = new ActoresPeliculaViewHolder(itemView);
        return ActoresPeliculaVH;
    }

    @Override
    public void onBindViewHolder(ActoresPeliculaViewHolder viewHolder, int pos){

        Cast j = datos.get(pos);
        viewHolder.bindActoresPelicula(j,listener);
    }
    @Override
    public int getItemCount(){

        return datos.size();
    }
}
