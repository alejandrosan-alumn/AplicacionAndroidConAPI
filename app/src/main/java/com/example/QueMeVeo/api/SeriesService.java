package com.example.QueMeVeo.api;

import com.example.QueMeVeo.Datos.ActoresPelicula;
import com.example.QueMeVeo.Datos.PeliculasDetalles;
import com.example.QueMeVeo.Datos.PeliculasPopulares;
import com.example.QueMeVeo.Datos.Series;
import com.example.QueMeVeo.Datos.SeriesDetalles;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SeriesService {

    final static String series = "https://api.themoviedb.org/3/tv/";
    final static String busqueda = "https://api.themoviedb.org/3/search/";
    @GET("popular?api_key=af855856558eb7aedf787a299469ff94&language=es")
    Call<Series> getSeriesPopulares();
    @GET("top_rated?api_key=af855856558eb7aedf787a299469ff94&language=es")
    Call<Series> getSeriesValoradas();
    @GET("{id}?api_key=af855856558eb7aedf787a299469ff94&language=es")
    Call<SeriesDetalles> getSerieDetalle(@Path("id") int id);
    @GET("{id}/credits?api_key=af855856558eb7aedf787a299469ff94&language=es")
    Call<ActoresPelicula> getSerieActores(@Path("id") int id);
    @GET("tv?filters&api_key=af855856558eb7aedf787a299469ff94&language=es")
    Call<Series> getSerieBusqueda(@Query("query")String nombreSerie);
}
