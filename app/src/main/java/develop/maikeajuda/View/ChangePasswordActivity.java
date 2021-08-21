package develop.maikeajuda.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import develop.maikeajuda.Application.AppConfig;
import develop.maikeajuda.Application.ApplicationControler;
import develop.maikeajuda.R;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText newPassword;
    private String URL_CHANGEPASSWORD = AppConfig.URL_CHANGEPASSWORD;
    private final String TAG_jsonObj_LOGIN = "json_obj_login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);

        newPassword = findViewById(R.id.editText_newPassword);

        Button btnSendRecoverEmail = findViewById(R.id.btnSendPassword);

        String emailRecover = getIntent().getStringExtra("email");
        String token = getIntent().getStringExtra("token");

        btnSendRecoverEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(newPassword.getText().toString() == "") return;
                    changePassword(emailRecover, token, newPassword.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void changePassword(String email, String token, String newPassword) throws JSONException {
        JSONObject requestObj = new JSONObject();
        requestObj.put("email", email);
        requestObj.put("token", token);
        requestObj.put("password", newPassword);
        JsonObjectRequest jsonObjectLoginRequest = new JsonObjectRequest(Request.Method.POST, URL_CHANGEPASSWORD, requestObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("response");
                            if((message.contains("Wrong"))){

                            } else {
                                showToast("Sua senha foi redefinida com sucesso!");
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ChangePasswordActivity.this);
                alertBuilder.setTitle("Erro");
                alertBuilder.setMessage("Código Inválido");
                alertBuilder.show();
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
}
