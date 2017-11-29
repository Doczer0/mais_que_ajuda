package develop.maikeajuda.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import develop.maikeajuda.Application.AppConfig;
import develop.maikeajuda.Application.ApplicationControler;
import develop.maikeajuda.Application.SessionManager;
import develop.maikeajuda.R;

public class LoginActivity extends AppCompatActivity {
    private SessionManager session;
    private EditText textEmail, textPassword;
    private String email, password;
    private ProgressDialog progressDialog;
    private String URL_LOGIN = AppConfig.URL_LOGIN;
    private final String TAG_jsonObj_LOGIN = "json_obj_login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManager(getApplicationContext());
        Typeface font2 = Typeface.createFromAsset(getAssets(),"fonts/SourceSansPro.ttf");

        textEmail =  findViewById(R.id.editText_email);
        textPassword =  findViewById(R.id.editText_password);
        Button buttonLogin =  findViewById(R.id.btnLogin);
        TextView linkNewAccount =  findViewById(R.id.text_new_account);

        buttonLogin.setTypeface(font2);
        linkNewAccount.setTypeface(font2);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);


        if (session.isLoggedIn()) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = textEmail.getText().toString();
                password = textPassword.getText().toString();
                if((!email.isEmpty())&&(!password.isEmpty())){

                    try {
                        progressDialog.setMessage("Entrando...");
                        showDialog();
                        checkLogin(email, password);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        showToast("Erro: "+e.toString());
                        hideDialog();
                    }
                } else{
                    showToast("Todos os campos devem ser preenchidos");
                }
            }
        });

        linkNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    public void checkLogin(String email, String password) throws JSONException, IOException {
        final String localEmail = email;
        final String localPasswd = password;
        JSONObject jObj = new JSONObject();
        jObj.put("email",email);
        jObj.put("password",password);
        JsonObjectRequest jsonObjectLoginRequest = new JsonObjectRequest(Request.Method.POST, URL_LOGIN, jObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("response");
                            if((message.contains("Wrong"))){
                                showToast("E-mail ou senha incorretos para autenticação");
                            } else {
                                String token = response.getString("response");
                                session.createLoginSession(localEmail,localPasswd,token);
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                hideDialog();
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            showToast("Erro: "+e.toString());
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
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                //headers.put("apiKey", "xxxxxxxxxxxxxxx");
                return headers;
            }
        };
        ApplicationControler.getInstance().addToRequestQueue(jsonObjectLoginRequest,TAG_jsonObj_LOGIN);
    }

    public void showToast(String message){
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

    @Override
    protected void onStop() {
        super.onStop();
        ApplicationControler.getInstance().cancelPendingRequests(TAG_jsonObj_LOGIN);
    }
}
