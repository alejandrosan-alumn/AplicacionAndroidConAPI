package com.example.QueMeVeo.ui.Favoritos;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.QueMeVeo.Adapters.PeliculasFavoritasAdapter;
import com.example.QueMeVeo.Adapters.PeliculasPopularesAdapter;
import com.example.QueMeVeo.Adapters.SeriesFavoritasAdapter;
import com.example.QueMeVeo.ConexionSQLiteHelper;
import com.example.QueMeVeo.Datos.PeliculasDetalles;
import com.example.QueMeVeo.Datos.PeliculasPopulares;
import com.example.QueMeVeo.Datos.Result;
import com.example.QueMeVeo.Datos.SeriesDetalles;
import com.example.QueMeVeo.actividades.PeliculaActivity;
import com.example.QueMeVeo.actividades.SerieActivity;
import com.example.QueMeVeo.api.PeliculasService;
import com.example.QueMeVeo.api.SeriesService;
import com.example.practicaandroid.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FavoritosFragment extends Fragment {

    PeliculasDetalles peliculasFavoritas;
    RecyclerView recyclerViewPF;
    ConexionSQLiteHelper conexion;

    SeriesDetalles seriesFavoritas;
    RecyclerView recyclerViewSF;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favoritos, container, false);

        peliculasFavoritas = new PeliculasDetalles();
        recyclerViewPF = (RecyclerView) root.findViewById(R.id.recycler_view);
        recyclerViewPF.setHasFixedSize(false);
        conexion = new ConexionSQLiteHelper(getContext());

        seriesFavoritas = new SeriesDetalles();
        recyclerViewSF = (RecyclerView) root.findViewById(R.id.recycler_view2);
        recyclerViewSF.setHasFixedSize(false);

        final PeliculasFavoritasAdapter adaptadorPeliculasFavoritas = new PeliculasFavoritasAdapter(peliculasFavoritas, new PeliculasFavoritasAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PeliculasDetalles itemPelicula) {

                Intent ampliacionPeliculaFavoritaIntent = new Intent().setClass(getContext(), PeliculaActivity.class);
                ampliacionPeliculaFavoritaIntent.putExtra("id", itemPelicula.getId());
                startActivity(ampliacionPeliculaFavoritaIntent);
            }
        });
        recyclerViewPF.setAdapter(adaptadorPeliculasFavoritas);
        recyclerViewPF.setLayoutManager(new LinearLayoutManager(root.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewPF.addItemDecoration(new DividerItemDecoration(root.getContext(), DividerItemDecoration.VERTICAL));
        recyclerViewPF.setItemAnimator(new DefaultItemAnimator());

        final SeriesFavoritasAdapter adaptadorSeriesFavoritas = new SeriesFavoritasAdapter(seriesFavoritas, new SeriesFavoritasAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SeriesDetalles itemSerie) {
                Intent ampliacionSerieFavoritaIntent = new Intent().setClass(getContext(), SerieActivity.class);
                ampliacionSerieFavoritaIntent.putExtra("id", itemSerie.getId());
                startActivity(ampliacionSerieFavoritaIntent);
            }
        });
        recyclerViewSF.setAdapter(adaptadorSeriesFavoritas);
        recyclerViewSF.setLayoutManager(new LinearLayoutManager(root.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewSF.addItemDecoration(new DividerItemDecoration(root.getContext(), DividerItemDecoration.VERTICAL));
        recyclerViewSF.setItemAnimator(new DefaultItemAnimator());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PeliculasService.peliculas)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PeliculasService servicioPeliculasAPI = retrofit.create(PeliculasService.class);

        List<Integer> idPeliculasFavoritas = new ArrayList<Integer>();
        SQLiteDatabase bd = conexion.getReadableDatabase();
        Cursor cursor = bd.rawQuery("SELECT * FROM Peliculas", null);
        while(cursor.moveToNext()){
            idPeliculasFavoritas.add(cursor.getInt(0));
        }
        bd.close();
        for(int i = 0; i < idPeliculasFavoritas.size(); i++) {
            Log.d("IDPeliculas", "ID: " + idPeliculasFavoritas.get(i).toString());
            Call<PeliculasDetalles> callPeliculasFav = servicioPeliculasAPI.getPeliculasFavoritas(idPeliculasFavoritas.get(i));
            callPeliculasFav.enqueue(new Callback<PeliculasDetalles>() {
                @Override
                public void onResponse(Call<PeliculasDetalles> call, Response<PeliculasDetalles> response) {
                    switch (response.code()){

                        case 200:
                            PeliculasDetalles resultadoPFav = response.body();
                            peliculasFavoritas = resultadoPFav;
                            adaptadorPeliculasFavoritas.AddDatos(peliculasFavoritas);
                            adaptadorPeliculasFavoritas.notifyDataSetChanged();
                            break;
                        case 400:
                            Log.d("EntradaMal", "Entrada 400");
                            break;
                        default:
                            Log.d("EntradaMal", "Entrada default");
                            break;
                    }
                }

                @Override
                public void onFailure(Call<PeliculasDetalles> call, Throwable t) {

                    System.err.println("Error ocurrido, código lanzado: " + t);
                }
            });
        }

        Retrofit retrofitSeries = new Retrofit.Builder()
                .baseUrl(SeriesService.series)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SeriesService servicioSeriesAPI = retrofitSeries.create(SeriesService.class);

        ArrayList<Integer> idSeriesFavoritas = new ArrayList<Integer>();
        SQLiteDatabase bdSeries = conexion.getReadableDatabase();
        Cursor cursorSeries = bdSeries.rawQuery("SELECT * FROM Series", null);
        while(cursorSeries.moveToNext()){
            idSeriesFavoritas.add(cursorSeries.getInt(0));
        }
        bdSeries.close();
        for(int i = 0; i < idSeriesFavoritas.size(); i++) {
            Call<SeriesDetalles> callSeriesFav = servicioSeriesAPI.getSerieDetalle(idSeriesFavoritas.get(i));
            callSeriesFav.enqueue(new Callback<SeriesDetalles>() {
                @Override
                public void onResponse(Call<SeriesDetalles> call, Response<SeriesDetalles> responseSerie) {
                    switch (responseSerie.code()){

                        case 200:
                            SeriesDetalles resultadoSFav = responseSerie.body();
                            seriesFavoritas = resultadoSFav;
                            adaptadorSeriesFavoritas.AddDatos(seriesFavoritas);
                            adaptadorSeriesFavoritas.notifyDataSetChanged();
                            break;
                        case 400:
                            Log.d("EntradaMal", "Entrada 400");
                            break;
                        default:
                            Log.d("EntradaMal", "Entrada default");
                            break;
                    }
                }

                @Override
                public void onFailure(Call<SeriesDetalles> call, Throwable t) {

                    System.err.println("Error ocurrido, código lanzado: " + t);
                }
            });
        }
        return root;
    }
}