package com.example.QueMeVeo.actividades;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

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
import com.example.QueMeVeo.Datos.SeriesDetalles;
import com.example.QueMeVeo.api.PeliculasService;
import com.example.QueMeVeo.api.SeriesService;
import com.example.practicaandroid.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SerieActivity extends AppCompatActivity {

    private TextView nombreSerie;
    private TextView resumenSerie;
    private ImageView posterSerie;
    private RatingBar estrellasSerie;
    private TextView genero;
    private TextView company;
    private TextView fSalida;
    private TextView temporadas;
    private TextView episodios;
    private TextView estado;
    RecyclerView recyclerViewActoresSerie;
    List<Cast> actoresSerie;
    private CheckBox serieFavorita;
    ConexionSQLiteHelper conexion;
    private ProgressBar barraProgreso;
    private Button botonVolver;
    private Button verAhora;
    private String urlVer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_media_serie);

        nombreSerie = (TextView) findViewById(R.id.textView2);
        resumenSerie = (TextView) findViewById(R.id.textView8);
        posterSerie = (ImageView) findViewById(R.id.imageView3);
        estrellasSerie = (RatingBar) findViewById(R.id.ratingBar2);
        genero = (TextView) findViewById(R.id.genre);
        fSalida = (TextView) findViewById(R.id.release);
        temporadas = (TextView) findViewById(R.id.textView25);
        episodios = (TextView) findViewById(R.id.textView27);
        estado = (TextView) findViewById(R.id.textView29);
        company = (TextView) findViewById(R.id.textView9);
        serieFavorita = (CheckBox) findViewById(R.id.checkBox2);
        barraProgreso = (ProgressBar) findViewById(R.id.progressLoading);
        botonVolver = (Button) findViewById(R.id.buttonBack);
        verAhora = (Button) findViewById(R.id.button);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SeriesService.series)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SeriesService servicioSeriesAPI = retrofit.create(SeriesService.class);

        int idSeries = getIntent().getIntExtra("id", 0);
        Call<SeriesDetalles> callSeries = servicioSeriesAPI.getSerieDetalle(idSeries);
        callSeries.enqueue(new Callback<SeriesDetalles>() {
            @Override
            public void onResponse(Call<SeriesDetalles> call, Response<SeriesDetalles> response) {

                switch(response.code()){

                    case 200:
                        SeriesDetalles resultado = response.body();
                        nombreSerie.setText(resultado.getName());
                        resumenSerie.setText(resultado.getOverview());
                        estrellasSerie.setNumStars(5);
                        estrellasSerie.setStepSize((float) 0.5);
                        estrellasSerie.setRating(resultado.getVoteAverage().floatValue()/2);

                        String url = "http://image.tmdb.org/t/p/w500" + resultado.getPosterPath();
                        Picasso.get().load(url).into(posterSerie);
                        int i = 0;
                        String generoSerie = " ";
                        for(i = 0; i < resultado.getGenres().size(); i++){
                            generoSerie = generoSerie + " " + resultado.getGenres().get(i).getName();
                        }
                        genero.setText(generoSerie);
                        fSalida.setText(resultado.getFirstAirDate());
                        temporadas.setText(resultado.getNumberOfSeasons().toString());
                        episodios.setText(resultado.getNumberOfEpisodes().toString());
                        estado.setText(resultado.getStatus());
                        String estudiosSerie = " ";
                        for(i = 0; i < resultado.getProductionCompanies().size(); i++){
                            estudiosSerie = estudiosSerie + " " + resultado.getProductionCompanies().get(i).getName();
                        }
                        company.setText(estudiosSerie);
                        ComprobarFav(idSeries);
                        urlVer = resultado.getHomepage();
                        if (urlVer.isEmpty()){
                            verAhora.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case 401:
                        System.err.println("Código 400.");
                        break;
                    default:
                        System.err.println("Otro código");
                        break;
                }
                barraProgreso.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<SeriesDetalles> call, Throwable t) {

                System.err.println("Error ocurrido, código lanzado: " + t);
            }
        });

        actoresSerie = new ArrayList<>();
        recyclerViewActoresSerie = (RecyclerView) findViewById(R.id.recycler_view2);
        recyclerViewActoresSerie.setHasFixedSize(false);

        final ActoresPeliculasAdapter adaptadorActores = new ActoresPeliculasAdapter(actoresSerie, new ActoresPeliculasAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Cast item) {

                Intent ampliacionActorSerieIntent = new Intent().setClass(getApplicationContext(), ActorActivity.class);
                ampliacionActorSerieIntent.putExtra("id", item.getId());
                startActivity(ampliacionActorSerieIntent);
            }
        });
        recyclerViewActoresSerie.setAdapter(adaptadorActores);
        recyclerViewActoresSerie.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewActoresSerie.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerViewActoresSerie.setItemAnimator(new DefaultItemAnimator());

        Retrofit retrofitActores = new Retrofit.Builder()
                .baseUrl(SeriesService.series)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SeriesService servicioSeriesAPIA = retrofitActores.create(SeriesService.class);

        Call<ActoresPelicula> callActoresS = servicioSeriesAPIA.getSerieActores(idSeries);
        callActoresS.enqueue(new Callback<ActoresPelicula>() {
            @Override
            public void onResponse(Call<ActoresPelicula> callActor, Response<ActoresPelicula> responseActor) {

                switch (responseActor.code()){

                    case 200:
                        ActoresPelicula resultadoActores = responseActor.body();
                        actoresSerie = resultadoActores.getCast();
                        adaptadorActores.setDatos(actoresSerie);
                        adaptadorActores.notifyDataSetChanged();
                        break;
                    case 400:
                        Log.d("EntradaMal", "Entrada 400");
                        break;
                    default:
                        Log.d("EntradaMal", "Entrada default actores series " + responseActor.code());
                        break;
                }
            }

            @Override
            public void onFailure(Call<ActoresPelicula> call, Throwable t) {
                System.err.println("Error ocurrido, código lanzado: " + t);
            }
        });
        serieFavorita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                conexion = new ConexionSQLiteHelper(getApplicationContext());
                boolean check = serieFavorita.isChecked();
                int id = getIntent().getIntExtra("id", 0);
                if(check){

                    SQLiteDatabase bd = conexion.getWritableDatabase();
                    ContentValues valores = new ContentValues();
                    valores.put("IDSeries",id);
                    Long idConsulta = bd.insert("Series", "IDSeries", valores);
                    Log.d("idconsulta", idConsulta.toString());
                    bd.close();
                }
                else{

                    SQLiteDatabase bd = conexion.getWritableDatabase();
                    String columna = "IDSeries=?";
                    String[] argumento = {String.valueOf(id)};
                    int borrarFila = bd.delete("Series", columna, argumento);
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
        ArrayList<Integer> idSeriesFavoritas = new ArrayList<Integer>();
        boolean favCorrecto = false;
        SQLiteDatabase bd = conexion.getReadableDatabase();
        Cursor cursor = bd.rawQuery("SELECT * FROM Series", null);
        while(cursor.moveToNext()){
            idSeriesFavoritas.add(cursor.getInt(0));
        }
        bd.close();
        for (int i = 0; i < idSeriesFavoritas.size(); i++){
            if(idPasado == idSeriesFavoritas.get(i)){
                favCorrecto = true;
            }
        }
        if(favCorrecto){
            serieFavorita.setChecked(favCorrecto);
        }
        else{
            serieFavorita.setChecked(favCorrecto);
        }
    }
}
