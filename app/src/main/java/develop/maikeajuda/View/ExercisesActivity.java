package develop.maikeajuda.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import develop.maikeajuda.Application.AppConfig;
import develop.maikeajuda.Application.ApplicationControler;
import develop.maikeajuda.Application.SessionManager;
import develop.maikeajuda.Controller.ExerciseAdapter;
import develop.maikeajuda.Model.Exercise;
import develop.maikeajuda.R;

public class ExercisesActivity extends AppCompatActivity {
    private GridView exerciseGridView;
    private ProgressDialog progressDialog;
    private JSONArray exercisesResponse;
    private List<Exercise> exercisesSimpleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        Intent intent = getIntent();
        int id = intent.getIntExtra("category_id",1);

        TextView textTitle = findViewById(R.id.text_category_exercises);
        exerciseGridView =  findViewById(R.id.gridView_exercises);
        Typeface font = Typeface.createFromAsset(getAssets(),"fonts/SourceSansProLight.ttf");

        textTitle.setTypeface(font);
        textTitle.setText(intent.getStringExtra("category"));

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        progressDialog.setMessage("Carregando Exercicios...");
        showDialog();
        listAllExercises(id);

        exerciseGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Exercise exercise = exercisesSimpleList.get(position);
                Intent nextActivity = new Intent(getApplicationContext(),ExerciseActivity.class);
                nextActivity.putExtra("exercise_id",exercise.getExerciseID());
                nextActivity.putExtra("exercise_name",exercise.getExerciseName());
                startActivity(nextActivity);
            }
        });
    }

    private void listAllExercises(final int id){
        String EXERCISES_TAG = "json_obj_exercises";

        JsonObjectRequest exercisesRequest = new JsonObjectRequest(Request.Method.GET, AppConfig.URL_EXERCISES, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = response.getJSONObject("response");
                    exercisesResponse = obj.getJSONArray("exercises");
                    for(int n = 0; n < exercisesResponse.length(); n++){
                        try {
                            JSONObject object = exercisesResponse.getJSONObject(n);
                            int cat = Integer.parseInt(object.getString("id_category"));
                            if(cat == id){
                                Exercise exercises;
                                exercises = new Exercise(
                                        Integer.parseInt(object.getString("id")),
                                        Integer.parseInt(object.getString("id_category")),
                                        object.getString("exercise_name"));
                                exercisesSimpleList.add(exercises);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showToast("Erro: "+e.toString());
                        }
                    }
                    ExerciseAdapter adapter = new ExerciseAdapter(exercisesSimpleList,ExercisesActivity.this);
                    exerciseGridView.setAdapter(adapter);
                    hideDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("Falha ao carregar exercicios\nErro: "+e);
                    hideDialog();
                }
                hideDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String error = volleyError.toString();
                switch(error) {
                    case "com.android.volley.AuthFailureError":
                        showToast("Erro: Não foi possivel autenticar solicitação");
                        break;
                    case "com.android.volley.NetworkError":
                        showToast("Erro: Não foi possivel executar solicitação");
                        break;
                    case "com.android.volley.NoConnectionError":
                        showToast("Erro: Não foi possivel estabelecer uma conexão com o servidor");
                        break;
                    case "com.android.volley.ParseError":
                        showToast("Erro: Não foi possivel analizar a resposta do servidor");
                        break;
                    case "com.android.volley.ServerError":
                        showToast("Erro: Erro no servidor");
                        break;
                    case "com.android.volley.TimeoutError":
                        showToast("Erro: Sem conexão com o servidor");
                        break;
                }
                hideDialog();
                finish();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                SessionManager session = new SessionManager(getApplicationContext());
                String token = session.getTokenSession();
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        ApplicationControler.getInstance().addToRequestQueue(exercisesRequest,EXERCISES_TAG);
    }

    private void showToast(String message){
        Toast notify;
        notify = Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG);
        notify.setGravity(Gravity.CENTER,0,0);
        notify.show();
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
