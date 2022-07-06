package com.example.QueMeVeo.ui.Series;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.QueMeVeo.Adapters.SeriesAdapter;
import com.example.QueMeVeo.Adapters.SeriesBusquedaAdapter;
import com.example.QueMeVeo.Datos.PeliculasPopulares;
import com.example.QueMeVeo.Datos.Result;
import com.example.QueMeVeo.Datos.ResultSeries;
import com.example.QueMeVeo.Datos.Series;
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

public class SeriesFragment extends Fragment {

    RecyclerView recyclerViewSP;
    List<ResultSeries> seriesPopulares;
    RecyclerView recyclerViewSValoradas;
    List<ResultSeries> seriesValoradas;
    EditText textoBusquedaSeries;
    RecyclerView recyclerViewSeriesBusqueda;
    ProgressBar barraProgreso;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_media_final, container, false);

        seriesPopulares = new ArrayList<>();
        recyclerViewSP = (RecyclerView) root.findViewById(R.id.recycler_view2);
        recyclerViewSP.setHasFixedSize(false);

        seriesValoradas = new ArrayList<>();
        recyclerViewSValoradas = (RecyclerView) root.findViewById(R.id.recycler_view);
        recyclerViewSValoradas.setHasFixedSize(false);

        textoBusquedaSeries = (EditText) root.findViewById(R.id.editText);
        recyclerViewSeriesBusqueda = (RecyclerView) root.findViewById(R.id.recycler_view_busqueda);
        barraProgreso = (ProgressBar) root.findViewById(R.id.progressBar);

        final SeriesAdapter adaptadorP = new SeriesAdapter(seriesPopulares, new SeriesAdapter.OnItemClickListener(){
            @Override
            public void OnItemClick(ResultSeries item){

                Intent ampliacionSeriePopularIntent = new Intent().setClass(getContext(), SerieActivity.class);
                ampliacionSeriePopularIntent.putExtra("id", item.getId());
                startActivity(ampliacionSeriePopularIntent);
            }
        });

        final SeriesAdapter adaptadorV = new SeriesAdapter(seriesValoradas, new SeriesAdapter.OnItemClickListener(){
            @Override
            public void OnItemClick(ResultSeries itemV){

                Intent ampliacionSerieValoradaIntent = new Intent().setClass(getContext(), SerieActivity.class);
                ampliacionSerieValoradaIntent.putExtra("id", itemV.getId());
                startActivity(ampliacionSerieValoradaIntent);
            }
        });

        recyclerViewSP.setAdapter(adaptadorP);
        recyclerViewSP.setLayoutManager(new LinearLayoutManager(root.getContext(),LinearLayoutManager.HORIZONTAL,false ));
        recyclerViewSP.addItemDecoration(new DividerItemDecoration(root.getContext(),DividerItemDecoration.VERTICAL));
        recyclerViewSP.setItemAnimator(new DefaultItemAnimator());

        recyclerViewSValoradas.setAdapter(adaptadorV);
        recyclerViewSValoradas.setLayoutManager(new LinearLayoutManager(root.getContext(),LinearLayoutManager.HORIZONTAL,false ));
        recyclerViewSValoradas.addItemDecoration(new DividerItemDecoration(root.getContext(),DividerItemDecoration.VERTICAL));
        recyclerViewSValoradas.setItemAnimator(new DefaultItemAnimator());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SeriesService.series)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SeriesService servicioSeriesAPI = retrofit.create(SeriesService.class);

        Call<Series> callSeriesP = servicioSeriesAPI.getSeriesPopulares();
        callSeriesP.enqueue(new Callback<Series>() {
            @Override
            public void onResponse(Call<Series> call, Response<Series> response) {

                switch (response.code()){
                    case 200:
                        Series resultadoPopulares = response.body();
                        seriesPopulares = resultadoPopulares.getResults();
                        adaptadorP.setDatos(seriesPopulares);
                        adaptadorP.notifyDataSetChanged();
                        break;
                    case 400:
                        Log.d("EntradaMal", "Entrada 400");
                        break;
                    default:
                        Log.d("EntradaMal", "Entrada default");
                        break;
                }
                barraProgreso.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<Series> call, Throwable t) {

                System.err.println("Error ocurrido, código lanzado: " + t);
            }
        });
        Call<Series> callSeriesV = servicioSeriesAPI.getSeriesValoradas();
        callSeriesV.enqueue(new Callback<Series>() {
            @Override
            public void onResponse(Call<Series> callV, Response<Series> responseV) {

                switch (responseV.code()){
                    case 200:
                        Series resultadoValoradas = responseV.body();
                        seriesValoradas = resultadoValoradas.getResults();
                        adaptadorV.setDatos(seriesValoradas);
                        adaptadorV.notifyDataSetChanged();
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
            public void onFailure(Call<Series> call, Throwable t) {

                System.err.println("Error ocurrido, código lanzado: " + t);
            }
        });

        textoBusquedaSeries.addTextChangedListener(new TextWatcher() {

            List<ResultSeries> seriesBuscadas = new ArrayList<>();

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                recyclerViewSeriesBusqueda.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {



                final SeriesBusquedaAdapter adaptadorBusquedaSeries = new SeriesBusquedaAdapter(seriesBuscadas, new SeriesBusquedaAdapter.OnItemClickListener() {
                    @Override
                    public void OnItemClick(ResultSeries itemB) {

                        Intent ampliacionSerieBusquedaIntent = new Intent().setClass(getContext(), SerieActivity.class);
                        ampliacionSerieBusquedaIntent.putExtra("id", itemB.getId());
                        startActivity(ampliacionSerieBusquedaIntent);
                    }
                });

                recyclerViewSeriesBusqueda.setAdapter(adaptadorBusquedaSeries);
                recyclerViewSeriesBusqueda.setLayoutManager(new LinearLayoutManager(root.getContext(), LinearLayoutManager.HORIZONTAL, false));
                recyclerViewSeriesBusqueda.addItemDecoration(new DividerItemDecoration(root.getContext(),DividerItemDecoration.VERTICAL));
                recyclerViewSeriesBusqueda.setItemAnimator(new DefaultItemAnimator());

                Retrofit retrofitBusquedaSerie = new Retrofit.Builder()
                        .baseUrl(SeriesService.busqueda)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                SeriesService servicioBuscarSerieAPI = retrofitBusquedaSerie.create(SeriesService.class);
                Call<Series> callSeriesB = servicioBuscarSerieAPI.getSerieBusqueda(textoBusquedaSeries.getText().toString());
                callSeriesB.enqueue(new Callback<Series>() {
                    @Override
                    public void onResponse(Call<Series> callSeriesBuscar, Response<Series> responseSeriesBuscar) {

                        switch (responseSeriesBuscar.code()){
                            case 200:
                                Series resultadoBuscado = responseSeriesBuscar.body();
                                seriesBuscadas = resultadoBuscado.getResults();
                                adaptadorBusquedaSeries.setDatos(seriesBuscadas);
                                adaptadorBusquedaSeries.notifyDataSetChanged();
                                break;
                            case 400:
                                Log.d("EntradaMal", "Entrada 400");
                                break;
                            default:
                                Log.d("EntradaMal", "Entrada default" +responseSeriesBuscar.code());
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<Series> call, Throwable t) {

                        System.err.println("Error ocurrido, código lanzado: " + t);
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return root;
    }
}