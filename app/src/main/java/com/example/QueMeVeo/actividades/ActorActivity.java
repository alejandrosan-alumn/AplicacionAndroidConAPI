package com.example.QueMeVeo.actividades;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.QueMeVeo.Datos.ActoresDetalles;
import com.example.QueMeVeo.api.ActoresService;
import com.example.practicaandroid.R;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActorActivity extends AppCompatActivity {

    private ImageView imagenActor;
    private TextView nombreActor;
    private TextView lugarNacimiento;
    private TextView fechaNacimiento;
    private TextView popularidad;
    private TextView conocidoPor;
    private TextView biografia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_actor);

        imagenActor = (ImageView) findViewById(R.id.imageView3);
        nombreActor = (TextView) findViewById(R.id.textView2);
        lugarNacimiento = (TextView) findViewById(R.id.textView12);
        fechaNacimiento = (TextView) findViewById(R.id.textView8);
        popularidad = (TextView) findViewById(R.id.textView14);
        conocidoPor = (TextView) findViewById(R.id.textView16);
        biografia = (TextView) findViewById(R.id.textView9);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ActoresService.actores)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ActoresService servicioActoresAPI = retrofit.create(ActoresService.class);

        int idActor = getIntent().getIntExtra("id", 0);
        Call<ActoresDetalles> callActores = servicioActoresAPI.getActoresDetalle(idActor);
        callActores.enqueue(new Callback<ActoresDetalles>() {
            @Override
            public void onResponse(Call<ActoresDetalles> call, Response<ActoresDetalles> response) {
                switch (response.code()){

                    case 200:
                        ActoresDetalles resultado = response.body();
                        String url = "http://image.tmdb.org/t/p/w500" + resultado.getProfilePath();
                        Picasso.get().load(url).into(imagenActor);
                        nombreActor.setText(resultado.getName());
                        lugarNacimiento.setText(resultado.getPlaceOfBirth());
                        fechaNacimiento.setText(resultado.getBirthday());
                        popularidad.setText(resultado.getPopularity().toString());
                        conocidoPor.setText(resultado.getKnownForDepartment());
                        biografia.setText(resultado.getBiography());

                        break;
                    case 400:
                        System.err.println("Código 400.");
                        break;
                    default:
                        System.err.println("Otro código " + response.code());
                        break;
                }
            }

            @Override
            public void onFailure(Call<ActoresDetalles> call, Throwable t) {

                System.err.println("Error ocurrido, código lanzado: " + t);
            }
        });

    }
}
