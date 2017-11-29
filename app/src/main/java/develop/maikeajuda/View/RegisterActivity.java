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

public class RegisterActivity extends AppCompatActivity {
    private SessionManager session;
    private EditText edtMatriculation, edtName, edtEmail, edtPassword;
    private String matriculation, username, email, password;
    private ProgressDialog progressDialog;
    private String URL_REGISTER = AppConfig.URL_REGISTER;
    private String URL_LOGIN = AppConfig.URL_LOGIN;
    private final String TAG_jsonObj_REGISTER = "json_obj_register";
    private final String TAG_jsonObj_LOGIN = "json_obj_login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Typeface font2 = Typeface.createFromAsset(getAssets(),"fonts/SourceSansPro.ttf");

        edtMatriculation = findViewById(R.id.matriculation);
        edtName = findViewById(R.id.name);
        edtEmail = findViewById(R.id.email);
        edtPassword = findViewById(R.id.password);
        Button btnRegister = findViewById(R.id.btnRegister);
        TextView linkToLogin = findViewById(R.id.linkToLogin);

        btnRegister.setTypeface(font2);
        linkToLogin.setTypeface(font2);

        session = new SessionManager(getApplicationContext());
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                matriculation = edtMatriculation.getText().toString();
                username = edtName.getText().toString();
                email = edtEmail.getText().toString();
                password = edtPassword.getText().toString();

                if(validatingFields()){
                    try {
                        userRegister(matriculation, username, email, password);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        showToast("Erro: "+e.toString());
                        hideDialog();
                    }
                } else {
                    showToast("Todos os campos devem ser preenchidos");
                }
            }
        });

        linkToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public boolean validatingFields(){
        String matriculation = edtMatriculation.getText().toString();
        String username = edtName.getText().toString();
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        if((!matriculation.isEmpty())&&(!username.isEmpty())&&(!email.isEmpty())&&(!password.isEmpty())){
            return true;
        }else{
            showToast("Todos os campos devem ser preenchidos");
            return false;
        }
    }

    public void userRegister(String matriculation, String username, String email, String password) throws JSONException, IOException {
        final String userEmail = email;
        final String userPassword = password;

        JSONObject jObj = new JSONObject();
        jObj.put("name",username);
        jObj.put("email",email);
        jObj.put("password",password);
        jObj.put("matriculation",matriculation);

        progressDialog.setMessage("Registrando...");
        showDialog();
        JsonObjectRequest jsonObjectRegisterRequest = new JsonObjectRequest(Request.Method.POST, URL_REGISTER, jObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String message = response.getString("response");
                    if(message.contains("This account already exists")){
                        showToast("Essa conta jรก existe");
                    } else{
                        hideDialog();
                        showToast("Conta criada com sucesso");
                        checkLogin(userEmail,userPassword);
                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                    showToast("Erro: "+e.toString());
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
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                //headers.put("apiKey", "xxxxxxxxxxxxxxx");
                return headers;
            }
        };
        ApplicationControler.getInstance().addToRequestQueue(jsonObjectRegisterRequest,TAG_jsonObj_REGISTER);
    }

    public void checkLogin(String email, String password) throws JSONException, IOException {
        final String localEmail = email;
        final String localPasswd = password;
        JSONObject jObj = new JSONObject();
        jObj.put("email",email);
        jObj.put("password",password);
        JsonObjectRequest jsonObjectLoginRequest = new JsonObjectRequest(Request.Method.POST, URL_LOGIN, jObj, new Response.Listener<JSONObject>() {
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
        ApplicationControler.getInstance().cancelPendingRequests(TAG_jsonObj_REGISTER);
        ApplicationControler.getInstance().cancelPendingRequests(TAG_jsonObj_LOGIN);
    }
}
