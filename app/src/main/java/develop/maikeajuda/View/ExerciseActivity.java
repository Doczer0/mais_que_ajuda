package develop.maikeajuda.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
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
import develop.maikeajuda.Controller.DepthPageTransformer;
import develop.maikeajuda.Controller.StepAdapter;
import develop.maikeajuda.Model.Step;
import develop.maikeajuda.R;

public class ExerciseActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private ViewPager stepsViewPager;
    private Button buttonPhoto;
    private JSONArray exercisesResponse, comparisonResponse;
    private List<Step> stageList;
    private String exerciseName, image_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        Intent intent = getIntent();
        int id = intent.getIntExtra("exercise_id",1);
        exerciseName = intent.getStringExtra("exercise_name");

        stepsViewPager = findViewById(R.id.viewPager_exercise);
        buttonPhoto = findViewById(R.id.button_photo);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        progressDialog.setMessage("Carregando Exercicio...");
        showDialog();
        loadExercise(id);

        stepsViewPager.setPageTransformer(true, new DepthPageTransformer());

        stepsViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == (stageList.size()-1)){
                    buttonPhoto.setEnabled(true);
                    buttonPhoto.setVisibility(View.VISIBLE);
                }else{
                    buttonPhoto.setEnabled(false);
                    buttonPhoto.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        buttonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextActivity = new Intent(getApplicationContext(), Camera2Activity.class);
                nextActivity.putExtra("exercise_name",exerciseName);
                startActivity(nextActivity);
            }
        });

    }

    private void loadExercise(final int id) {
        String EXERCISE_TAG = "json_obj_exercise";
        stageList = new ArrayList<>();

        JsonObjectRequest exerciseRequest = new JsonObjectRequest(Request.Method.GET, AppConfig.URL_STAGES, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = response.getJSONObject("response");
                    exercisesResponse = obj.getJSONArray("steps");
                    for(int n = 0; n < exercisesResponse.length(); n++){
                        try {
                            JSONObject object = exercisesResponse.getJSONObject(n);
                            int cat = Integer.parseInt(object.getString("id_exercise"));
                            if(cat == id){
                                int exerID = Integer.parseInt(object.getString("id_exercise"));
                                String title = object.getString("step_name");
                                String content = object.getString("step_content");
                                String link = object.getString("image_step_url");

                                Step stageContent = new Step(exerID,title,content);
                                Step stageImage = new Step(exerID,title,link);
                                stageList.add(stageImage);
                                stageList.add(stageContent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    //showToast(stageList.toString());
                    StepAdapter adapter = new StepAdapter(stageList, getApplicationContext(), ExerciseActivity.this);
                    stepsViewPager.setAdapter(adapter);
                    hideDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("Erro: "+e+"\nFalha ao carregar exercicios");
                    hideDialog();
                }
                hideDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToast("Erro: "+error.toString());
                hideDialog();
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
        ApplicationControler.getInstance().addToRequestQueue(exerciseRequest,EXERCISE_TAG);

        progressDialog.setMessage("Carregando...");
        showDialog();
        loadImageSample(id);
    }

    private void loadImageSample(final int id){
        String EXERCISE_IMAGE_TAG = "json_obj_exercise_img";

        JsonObjectRequest exerciseImageRequest = new JsonObjectRequest(Request.Method.GET, AppConfig.URL_EXERCISE_IMG, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = response.getJSONObject("response");
                    comparisonResponse = obj.getJSONArray("comparisons");
                    if(comparisonResponse != null){
                        for(int n = 0; n < comparisonResponse.length(); n++) {
                            try {
                                JSONObject object = comparisonResponse.getJSONObject(n);
                                int cat = Integer.parseInt(object.getString("id_exercise"));
                                if(cat == id){
                                    image_url = object.getString("image_comparison_url");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        image_url ="";
                    }
                    hideDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("Erro: "+e+"\nFalha ao carregar imagem");
                    hideDialog();
                }
                hideDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToast("Erro: "+error.toString());
                hideDialog();
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
        ApplicationControler.getInstance().addToRequestQueue(exerciseImageRequest,EXERCISE_IMAGE_TAG);
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
