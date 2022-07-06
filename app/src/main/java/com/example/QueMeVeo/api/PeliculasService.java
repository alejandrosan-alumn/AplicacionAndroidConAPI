package com.example.QueMeVeo.api;

import com.example.QueMeVeo.Datos.ActoresPelicula;
import com.example.QueMeVeo.Datos.PeliculasDetalles;
import com.example.QueMeVeo.Datos.PeliculasPopulares;
import com.example.practicaandroid.R;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PeliculasService {

    final static String peliculas = "https://api.themoviedb.org/3/movie/";
    final static String busqueda = "https://api.themoviedb.org/3/search/";
    //final static String idioma = getString(R.string.lenguaje);
    @GET("popular?api_key=af855856558eb7aedf787a299469ff94&language=es")
    Call<PeliculasPopulares> getPeliculasPopulares();
    @GET("top_rated?api_key=af855856558eb7aedf787a299469ff94&language=es")
    Call<PeliculasPopulares> getPeliculasValoradas();
    @GET("{id}?api_key=af855856558eb7aedf787a299469ff94&language=es")
    Call<PeliculasDetalles> getPeliculaDetalle(@Path("id") int id);
    @GET("{id}/casts?api_key=af855856558eb7aedf787a299469ff94&language=es")
    Call<ActoresPelicula> getPeliculaActores(@Path("id") int id);
    @GET("{id}?api_key=af855856558eb7aedf787a299469ff94&language=es")
    Call<PeliculasDetalles> getPeliculasFavoritas(@Path("id") int id);
    @GET("movie?filters&api_key=af855856558eb7aedf787a299469ff94&language=es")
    Call<PeliculasPopulares> getPeliculaBusqueda(@Query("query")String nombrePelicula);
    @GET("{id}/similar_movies?api_key=af855856558eb7aedf787a299469ff94&language=es")
    Call<PeliculasDetalles> getPeliculasSimilares(@Path("id") int id);

}
