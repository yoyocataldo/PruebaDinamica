package cl.example.pruebadinamica;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;
import android.util.Log;

import cl.example.pruebadinamica.api.Api;
import cl.example.pruebadinamica.api.RetrofitClient;
import cl.example.pruebadinamica.pojos.Pregunta;
import cl.example.pruebadinamica.pojos.PreguntasLista;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "VistaLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "método onCreate");

        cargarApiInfo();
    }

    private void cargarApiInfo() {
        Api service = RetrofitClient.getRetrofitInstance().create(Api.class);
        Call<PreguntasLista> call = service.getAllQuestions();

        //Async
        call.enqueue(new Callback<PreguntasLista>() {
            @Override
            public void onResponse(Call<PreguntasLista> call,
                                   Response<PreguntasLista> response) {
                if (response != null) {
                    Pregunta pregunta = response.body().getResults().get(0);
                    crearFragmentPregunta(pregunta);
                }
            }

            @Override
            public void onFailure(Call<PreguntasLista> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error: no pudimos recuperar los datos de opentdb", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void crearFragmentPregunta(Pregunta pregunta) {
        PreguntaFragment preguntaFragment = PreguntaFragment.newInstance(
                pregunta.getQuestion(),
                pregunta.getCategory(),
                pregunta.getCorrectAnswer(),
                pregunta.getIncorrectAnswer()
        );
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, preguntaFragment,"FRAGMENTO").commit();
    }
}