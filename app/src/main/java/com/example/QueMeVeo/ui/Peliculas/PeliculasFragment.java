package com.example.QueMeVeo.ui.Peliculas;

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

import com.example.QueMeVeo.Adapters.PeliculasBusquedaAdapter;
import com.example.QueMeVeo.Adapters.PeliculasPopularesAdapter;
import com.example.QueMeVeo.Datos.PeliculasDetalles;
import com.example.QueMeVeo.Datos.PeliculasPopulares;
import com.example.QueMeVeo.Datos.Result;
import com.example.QueMeVeo.actividades.PeliculaActivity;
import com.example.QueMeVeo.api.PeliculasService;
import com.example.practicaandroid.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PeliculasFragment extends Fragment {

    RecyclerView recyclerViewPP;
    List<Result> peliculasPopulares;
    RecyclerView recyclerViewValoradas;
    List<Result> peliculasValoradas;
    EditText textoBusqueda;
    RecyclerView recyclerViewBusqueda;
    ProgressBar barraProgreso;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_media_final, container, false);

        peliculasPopulares = new ArrayList<>();
        recyclerViewPP = (RecyclerView) root.findViewById(R.id.recycler_view2);
        recyclerViewPP.setHasFixedSize(false);

        peliculasValoradas = new ArrayList<>();
        recyclerViewValoradas = (RecyclerView) root.findViewById(R.id.recycler_view);
        recyclerViewValoradas.setHasFixedSize(false);

        textoBusqueda = (EditText) root.findViewById(R.id.editText);
        recyclerViewBusqueda = (RecyclerView) root.findViewById(R.id.recycler_view_busqueda);
        barraProgreso = (ProgressBar) root.findViewById(R.id.progressBar);

        final PeliculasPopularesAdapter adaptador = new PeliculasPopularesAdapter(peliculasPopulares, new PeliculasPopularesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Result item) {

                Intent ampliacionPeliculaPopularIntent = new Intent().setClass(getContext(), PeliculaActivity.class);
                ampliacionPeliculaPopularIntent.putExtra("id", item.getId());
                startActivity(ampliacionPeliculaPopularIntent);
            }
        });

        final PeliculasPopularesAdapter adaptadorValoradas = new PeliculasPopularesAdapter(peliculasValoradas, new PeliculasPopularesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Result itemValoradas) {

                Intent ampliacionPeliculasValoradas = new Intent().setClass(getContext(), PeliculaActivity.class);
                ampliacionPeliculasValoradas.putExtra("id", itemValoradas.getId());
                startActivity(ampliacionPeliculasValoradas);
            }
        });

        recyclerViewPP.setAdapter(adaptador);
        recyclerViewPP.setLayoutManager(new LinearLayoutManager(root.getContext(),LinearLayoutManager.HORIZONTAL,false ));
        recyclerViewPP.addItemDecoration(new DividerItemDecoration(root.getContext(),DividerItemDecoration.VERTICAL));
        recyclerViewPP.setItemAnimator(new DefaultItemAnimator());

        recyclerViewValoradas.setAdapter(adaptadorValoradas);
        recyclerViewValoradas.setLayoutManager(new LinearLayoutManager(root.getContext(),LinearLayoutManager.HORIZONTAL,false));
        recyclerViewValoradas.addItemDecoration(new DividerItemDecoration(root.getContext(), DividerItemDecoration.VERTICAL));
        recyclerViewValoradas.setItemAnimator(new DefaultItemAnimator());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PeliculasService.peliculas)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PeliculasService servicioPeliculasAPI = retrofit.create(PeliculasService.class);

        Call<PeliculasPopulares> callPeliculasP = servicioPeliculasAPI.getPeliculasPopulares();
        callPeliculasP.enqueue(new Callback<PeliculasPopulares>() {
            @Override
            public void onResponse(Call<PeliculasPopulares> call, Response<PeliculasPopulares> response) {

                switch (response.code()){
                    case 200:
                        //Log.d("dondeEstoy", "Estoy aquí");
                        PeliculasPopulares resultadoPopulares = response.body();
                        peliculasPopulares = resultadoPopulares.getResults();
                        adaptador.setDatos(peliculasPopulares);
                        adaptador.notifyDataSetChanged();
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
            public void onFailure(Call<PeliculasPopulares> call, Throwable t) {

                System.err.println("Error ocurrido, código lanzado: " + t);
            }
        });
        Call<PeliculasPopulares> callPeliculasV = servicioPeliculasAPI.getPeliculasValoradas();
        callPeliculasV.enqueue(new Callback<PeliculasPopulares>() {
            @Override
            public void onResponse(Call<PeliculasPopulares> callV, Response<PeliculasPopulares> responseValoradas) {

                switch (responseValoradas.code()){
                    case 200:
                        PeliculasPopulares resultadoValoradas = responseValoradas.body();
                        peliculasValoradas = resultadoValoradas.getResults();
                        adaptadorValoradas.setDatos(peliculasValoradas);
                        adaptadorValoradas.notifyDataSetChanged();
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
            public void onFailure(Call<PeliculasPopulares> call, Throwable t) {

                System.err.println("Error ocurrido, código lanzado: " + t);
            }
        });
        textoBusqueda.addTextChangedListener(new TextWatcher() {
            List<Result> peliculasBuscadas = new ArrayList<>();

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                recyclerViewBusqueda.setVisibility(View.INVISIBLE);
                recyclerViewBusqueda.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                final PeliculasBusquedaAdapter adaptadorBusqueda = new PeliculasBusquedaAdapter(peliculasBuscadas, new PeliculasBusquedaAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Result item) {

                        Intent ampliacionPeliculaBuscadaIntent = new Intent().setClass(getContext(), PeliculaActivity.class);
                        ampliacionPeliculaBuscadaIntent.putExtra("id", item.getId());
                        startActivity(ampliacionPeliculaBuscadaIntent);
                    }
                });

                recyclerViewBusqueda.setAdapter(adaptadorBusqueda);
                recyclerViewBusqueda.setLayoutManager(new LinearLayoutManager(root.getContext(), LinearLayoutManager.HORIZONTAL, false));
                recyclerViewBusqueda.addItemDecoration(new DividerItemDecoration(root.getContext(),DividerItemDecoration.VERTICAL));
                recyclerViewBusqueda.setItemAnimator(new DefaultItemAnimator());

                Retrofit retrofitBusqueda = new Retrofit.Builder()
                        .baseUrl(PeliculasService.busqueda)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                PeliculasService servicioBuscarPeliAPI = retrofitBusqueda.create(PeliculasService.class);

                Call<PeliculasPopulares> callPeliculasB = servicioBuscarPeliAPI.getPeliculaBusqueda(textoBusqueda.getText().toString());
                callPeliculasB.enqueue(new Callback<PeliculasPopulares>() {
                    @Override
                    public void onResponse(Call<PeliculasPopulares> callB, Response<PeliculasPopulares> responseB) {

                        switch (responseB.code()){
                            case 200:
                                PeliculasPopulares resultadoBusqueda = responseB.body();
                                peliculasBuscadas = resultadoBusqueda.getResults();
                                adaptadorBusqueda.setDatos(peliculasBuscadas);
                                adaptador.notifyDataSetChanged();
                                break;
                            case 400:
                                Log.d("EntradaMal", "Entrada 400");
                                break;
                            default:
                                Log.d("EntradaMal", "Entrada default " + responseB.code());
                                break;
                        }

                    }

                    @Override
                    public void onFailure(Call<PeliculasPopulares> call, Throwable t) {

                        System.err.println("Error ocurrido, código lanzado: " + t);
                    }
                });
                //recyclerViewBusqueda.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return root;
    }
}