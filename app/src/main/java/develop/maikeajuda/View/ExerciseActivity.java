package develop.maikeajuda.View;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.LruCache;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import develop.maikeajuda.Application.AppConfig;
import develop.maikeajuda.Application.ApplicationControler;
import develop.maikeajuda.Application.SessionManager;
import develop.maikeajuda.Controller.DepthPageTransformer;
import develop.maikeajuda.Controller.StepAdapter;
import develop.maikeajuda.Model.Step;
import develop.maikeajuda.R;

public class ExerciseActivity extends AppCompatActivity {
    private static final int REQUEST_RUNTIME_PERMISSION = 1;
    private ProgressDialog progressDialog;
    private ViewPager stepsViewPager;
    private Button buttonPhoto;
    private JSONArray exercisesResponse, comparisonResponse;
    private Bitmap imageFilter = null;
    private List<Step> stageList, stageCombinations;
    private String exerciseName, image_url;
    private int count = 0;
    StepAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        Intent intent = getIntent();
        final int id = intent.getIntExtra("exercise_id",1);
        exerciseName = intent.getStringExtra("exercise_name");

        stepsViewPager = findViewById(R.id.viewPager_exercise);
        buttonPhoto = findViewById(R.id.button_photo);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        stageList = new ArrayList<>();
        stageCombinations = new ArrayList<>();

        progressDialog.setMessage("Carregando Exercicio...");
        showDialog();
        loadExercise(id);
        loadImageSample(id);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<stageCombinations.size();i++){
                    stageList.add(stageCombinations.get(i));

                }
                adapter.notifyDataSetChanged();
            }
        },5000);

        adapter = new StepAdapter(stageList, getApplicationContext(), ExerciseActivity.this);
        stepsViewPager.setAdapter(adapter);
        stepsViewPager.setPageTransformer(true, new DepthPageTransformer());

        stepsViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position >= (stageList.size()-count)){

                    image_url = stageList.get(position).getStepTitle();

                    //loadImageBitmap(image_url);

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

                checkPremission();
            }
        });
    }

    void checkPremission() {
        final String permission = Manifest.permission.CAMERA;
        if (ContextCompat.checkSelfPermission(ExerciseActivity.this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ExerciseActivity.this, permission)) {

            } else {
                ActivityCompat.requestPermissions(ExerciseActivity.this, new String[]{android.Manifest.permission.CAMERA,android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_RUNTIME_PERMISSION);
            }
        } else {
            Intent nextActivity = new Intent(getApplicationContext(), Camera2Activity.class);
            nextActivity.putExtra("exercise_name",exerciseName);
            nextActivity.putExtra("link",image_url);
            startActivity(nextActivity);;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_RUNTIME_PERMISSION:
                final int numOfRequest = grantResults.length;
                final boolean isGranted = numOfRequest == 1
                        && PackageManager.PERMISSION_GRANTED == grantResults[numOfRequest - 1];
                if (isGranted) {
                    Intent nextActivity = new Intent(getApplicationContext(), Camera2Activity.class);
                    nextActivity.putExtra("exercise_name",exerciseName);
                    nextActivity.putExtra("link",image_url);
                    startActivity(nextActivity);
                }else{

                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void loadExercise(final int id) {
        String EXERCISE_TAG = "json_obj_exercise";

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
                                String type = object.getString("step_type");

                                Step stageContent = new Step(n,title,content,type);
                                stageList.add(stageContent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    adapter.notifyDataSetChanged();
                    //hideDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("Erro: "+e+"\nFalha ao carregar exercicios");
                    hideDialog();
                }
                //hideDialog();
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
                        count = 0;
                        for(int n = 0; n < comparisonResponse.length(); n++) {
                            try {
                                JSONObject object = comparisonResponse.getJSONObject(n);
                                int cat = Integer.parseInt(object.getString("id_exercise"));
                                if(cat == id){
                                    int exerID = Integer.parseInt(object.getString("id_exercise"));
                                    String modify = object.getString("image_comparison_modify");
                                    String original = object.getString("image_comparison_original");
                                    String type = "image";

                                    Step stageContent = new Step((comparisonResponse.length()+n),modify,original,type);
                                    stageCombinations.add(stageContent);
                                    count++;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                showToast("Erro: "+e.getMessage());
                                hideDialog();
                            }
                        }
                    } else {
                        showToast("Erro:\nFalha ao carregar exercicio");
                        hideDialog();
                    }
                    adapter.notifyDataSetChanged();

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
