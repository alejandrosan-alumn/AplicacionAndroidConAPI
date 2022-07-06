package com.example.QueMeVeo.api;

import com.example.QueMeVeo.Datos.ActoresDetalles;
import com.example.QueMeVeo.Datos.PeliculasDetalles;
import com.example.QueMeVeo.Datos.PeliculasPopulares;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ActoresService {

    final static String actores = "https://api.themoviedb.org/3/person/";
    @GET("{id}?api_key=af855856558eb7aedf787a299469ff94&language=es")
    Call<ActoresDetalles> getActoresDetalle(@Path("id") int id);
}
