package com.example.QueMeVeo.actividades;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.QueMeVeo.Adapters.ActoresPeliculasAdapter;
import com.example.QueMeVeo.ConexionSQLiteHelper;
import com.example.QueMeVeo.Datos.ActoresPelicula;
import com.example.QueMeVeo.Datos.Cast;
import com.example.QueMeVeo.Datos.PeliculasDetalles;
import com.example.QueMeVeo.Datos.PeliculasPopulares;
import com.example.QueMeVeo.api.PeliculasService;
import com.example.practicaandroid.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PeliculaActivity extends AppCompatActivity {

    private TextView nombrePelicula;
    private TextView resumenPelicula;
    private ImageView posterPelicula;
    private RatingBar estrellasPelicula;
    private TextView genero;
    private TextView company;
    private TextView fSalida;
    private CheckBox peliculaFavorita;
    RecyclerView recyclerViewActores;
    List<Cast> actores;
    ConexionSQLiteHelper conexion;
    private Button botonVolver;
    private Button verAhora;
    private String urlVer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_media);

        nombrePelicula = (TextView) findViewById(R.id.textView2);
        resumenPelicula = (TextView) findViewById(R.id.textView8);
        posterPelicula = (ImageView) findViewById(R.id.imageView3);
        estrellasPelicula = (RatingBar) findViewById(R.id.ratingBar2);
        genero = (TextView) findViewById(R.id.textView6);
        company = (TextView) findViewById(R.id.textView5);
        fSalida = (TextView) findViewById(R.id.textView7);
        peliculaFavorita = (CheckBox) findViewById(R.id.checkBox2);
        botonVolver = (Button) findViewById(R.id.buttonBack);
        verAhora = (Button) findViewById(R.id.button);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PeliculasService.peliculas)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PeliculasService servicioPeliculasAPI = retrofit.create(PeliculasService.class);

        int idPelicula = getIntent().getIntExtra("id", 0);
        Call<PeliculasDetalles> callPeliculas = servicioPeliculasAPI.getPeliculaDetalle(idPelicula);
        callPeliculas.enqueue(new Callback<PeliculasDetalles>() {
            @Override
            public void onResponse(Call<PeliculasDetalles> call, Response<PeliculasDetalles> response) {
                switch(response.code()){

                    case 200:
                        PeliculasDetalles resultado = response.body();
                        nombrePelicula.setText(resultado.getTitle());
                        resumenPelicula.setText(resultado.getOverview());
                        estrellasPelicula.setNumStars(5);
                        estrellasPelicula.setStepSize((float) 0.5);
                        estrellasPelicula.setRating(resultado.getVoteAverage().floatValue()/2);

                        String url = "http://image.tmdb.org/t/p/w500" + resultado.getPosterPath();
                        //Log.d("enlaceFotos", url);
                        Picasso.get().load(url).into(posterPelicula);
                        int i = 0;
                        String companyString = "Compañías: ";
                        for(i = 0; i < resultado.getProductionCompanies().size(); i++){

                            companyString = companyString + " " + resultado.getProductionCompanies().get(i).getName();
                        }
                        company.setText(companyString);

                        String generos = "Generos: ";
                        for(i = 0; i < resultado.getGenres().size(); i++){

                            generos = generos + " " + resultado.getGenres().get(i).getName();
                        }
                        genero.setText(generos);
                        fSalida.setText(resultado.getReleaseDate());
                        ComprobarFav(idPelicula);
                        urlVer = resultado.getHomepage();
                        if(urlVer.isEmpty()){
                            verAhora.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case 401:
                        System.err.println("Código 400.");
                        break;
                    default:
                        System.err.println("Otro código " + response.code());
                        break;
                }
            }

            @Override
            public void onFailure(Call<PeliculasDetalles> call, Throwable t) {

                System.err.println("Error ocurrido, código lanzado: " + t);
            }
        });

        actores = new ArrayList<>();
        recyclerViewActores = (RecyclerView) findViewById(R.id.recycler_view2);
        recyclerViewActores.setHasFixedSize(false);

        final ActoresPeliculasAdapter adaptadorActores = new ActoresPeliculasAdapter(actores, new ActoresPeliculasAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Cast item) {

                Intent ampliacionActorPeliculaIntent = new Intent().setClass(getApplicationContext(), ActorActivity.class);
                ampliacionActorPeliculaIntent.putExtra("id", item.getId());
                startActivity(ampliacionActorPeliculaIntent);
            }
        });
        recyclerViewActores.setAdapter(adaptadorActores);
        recyclerViewActores.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewActores.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerViewActores.setItemAnimator(new DefaultItemAnimator());

        Retrofit retrofitActores = new Retrofit.Builder()
                .baseUrl(PeliculasService.peliculas)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PeliculasService servicioPeliculasAPIA = retrofitActores.create(PeliculasService.class);

        Call<ActoresPelicula> callActoresP = servicioPeliculasAPIA.getPeliculaActores(idPelicula);
        callActoresP.enqueue(new Callback<ActoresPelicula>() {
            @Override
            public void onResponse(Call<ActoresPelicula> callActor, Response<ActoresPelicula> responseActor) {

                switch (responseActor.code()){

                    case 200:
                        ActoresPelicula resultadoActores = responseActor.body();
                        actores = resultadoActores.getCast();
                        adaptadorActores.setDatos(actores);
                        adaptadorActores.notifyDataSetChanged();
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
            public void onFailure(Call<ActoresPelicula> call, Throwable t) {
                System.err.println("Error ocurrido, código lanzado: " + t);
            }
        });
       peliculaFavorita.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               conexion = new ConexionSQLiteHelper(getApplicationContext());
               boolean check = peliculaFavorita.isChecked();
               int id = getIntent().getIntExtra("id", 0);
               if(check){

                   Log.d("Guarda", "Entro a guardar.");
                   SQLiteDatabase bd = conexion.getWritableDatabase();
                   ContentValues valores = new ContentValues();
                   valores.put("IDpelicula",id);
                   Long idConsulta = bd.insert("Peliculas", "IDpelicula", valores);
                   Log.d("idconsulta", idConsulta.toString());
                   bd.close();
               }
               else{

                   Log.d("Eliminar", "Entro a eliminar.");
                   SQLiteDatabase bd = conexion.getWritableDatabase();
                   String columna = "IDpelicula=?";
                   String[] argumento = {String.valueOf(id)};
                   int borrarFila = bd.delete("Peliculas", columna, argumento);
                   Log.d("idBorrado", String.valueOf(borrarFila));
                   bd.close();
               }
           }
       });
       botonVolver.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               onBackPressed();
           }
       });
       verAhora.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(!urlVer.isEmpty()) {
                   Uri uri = Uri.parse(urlVer);
                   Intent intentVer = new Intent(Intent.ACTION_VIEW, uri);
                   startActivity(intentVer);
               }
               else{
               }
           }
       });
    }
    private void ComprobarFav(int idPasado){

        conexion = new ConexionSQLiteHelper(getApplicationContext());
        ArrayList<Integer> idPeliculasFavoritas = new ArrayList<Integer>();
        boolean favCorrecto = false;
        SQLiteDatabase bd = conexion.getReadableDatabase();
        Cursor cursor = bd.rawQuery("SELECT * FROM Peliculas", null);
        while(cursor.moveToNext()){
            idPeliculasFavoritas.add(cursor.getInt(0));
        }
        bd.close();
        for (int i = 0; i < idPeliculasFavoritas.size(); i++){
            if(idPasado == idPeliculasFavoritas.get(i)){
                favCorrecto = true;
            }
        }
        if(favCorrecto){
            peliculaFavorita.setChecked(favCorrecto);
        }
        else{
            peliculaFavorita.setChecked(favCorrecto);
        }
    }
}