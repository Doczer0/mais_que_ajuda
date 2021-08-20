package develop.maikeajuda.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class ForgetPasswordActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private String URL_RECOVEREMAIL = AppConfig.URL_RECOVEREMAIL;
    private final String TAG_jsonObj_LOGIN = "json_obj_login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);

        editTextEmail = findViewById(R.id.editText_newPassword);

        Button btnSendRecoverEmail = findViewById(R.id.btn_sendRecoverEmail);

        btnSendRecoverEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(editTextEmail.getText().toString().length() < 1) return;
                    checkEmail(editTextEmail.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void checkEmail(String email) throws JSONException {
        JSONObject emailObj = new JSONObject();
        emailObj.put("email", email);
        JsonObjectRequest jsonObjectLoginRequest = new JsonObjectRequest(Request.Method.POST, URL_RECOVEREMAIL, emailObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("response");
                            if((message.contains("Wrong"))){

                            } else {
                                Intent intent = new Intent(getApplicationContext(), RecoverCodeScreenActivity.class);
                                intent.putExtra("email", editTextEmail.getText().toString());
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ForgetPasswordActivity.this);
                alertBuilder.setTitle("Erro");
                alertBuilder.setMessage("Email inválido");
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
}
