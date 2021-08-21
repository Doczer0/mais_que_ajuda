package develop.maikeajuda.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class RecoverCodeScreenActivity extends AppCompatActivity {
    private EditText editTxtRecoveryCode1, editTxtRecoveryCode2, editTxtRecoveryCode3, editTxtRecoveryCode4, editTxtRecoveryCode5;
    private String URL_RECOVEREMAILCODE = AppConfig.URL_RECOVEREMAILCODE;
    private String URL_RECOVEREMAIL = AppConfig.URL_RECOVEREMAIL;
    private TextView txtEmail, txtCodeNotReceived;
    private final String TAG_jsonObj_LOGIN = "json_obj_login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_code);

        editTxtRecoveryCode1 = findViewById(R.id.editText_recoverPasswordCode);
        editTxtRecoveryCode2 = findViewById(R.id.editText_recoverPasswordCode2);
        editTxtRecoveryCode3 = findViewById(R.id.editText_recoverPasswordCode3);
        editTxtRecoveryCode4 = findViewById(R.id.editText_recoverPasswordCode4);
        editTxtRecoveryCode5 = findViewById(R.id.editText_recoverPasswordCode5);

        editTxtRecoveryCode1.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1) });
        editTxtRecoveryCode2.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1) });
        editTxtRecoveryCode3.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1) });
        editTxtRecoveryCode4.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1) });
        editTxtRecoveryCode5.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1) });

        txtEmail = findViewById(R.id.txtView_recoverEmail);
        txtCodeNotReceived = findViewById(R.id.txtCodeNotReceived);

        Button btnSendCode = findViewById(R.id.btnSendPassword);

        String emailRecover = getIntent().getStringExtra("email");

        txtEmail.setText(emailRecover);

        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String token = editTxtRecoveryCode1.getText().toString() + editTxtRecoveryCode2.getText().toString() + editTxtRecoveryCode3.getText().toString()
                        + editTxtRecoveryCode4.getText().toString() + editTxtRecoveryCode5.getText().toString();
                try {
                    checkCode(emailRecover, token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        txtCodeNotReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(editTxtRecoveryCode1.getText().toString().length() < 1 ||
                        editTxtRecoveryCode2.getText().toString().length() < 1 ||
                        editTxtRecoveryCode3.getText().toString().length() < 1 ||
                        editTxtRecoveryCode4.getText().toString().length() < 1 ||
                        editTxtRecoveryCode5.getText().toString().length() < 1) return;
                    checkEmail(emailRecover);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        editTxtRecoveryCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editTxtRecoveryCode1.length() == 1) editTxtRecoveryCode2.requestFocus();
            }
        });

        editTxtRecoveryCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editTxtRecoveryCode2.length() == 1) editTxtRecoveryCode3.requestFocus();
            }
        });

        editTxtRecoveryCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editTxtRecoveryCode3.length() == 1) editTxtRecoveryCode4.requestFocus();
            }
        });

        editTxtRecoveryCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editTxtRecoveryCode4.length() == 1) editTxtRecoveryCode5.requestFocus();
            }
        });
    }

    public void checkCode(String email, String token) throws JSONException {
        JSONObject requestObj = new JSONObject();
        requestObj.put("email", email);
        requestObj.put("token", token);
        JsonObjectRequest jsonObjectLoginRequest = new JsonObjectRequest(Request.Method.POST, URL_RECOVEREMAILCODE, requestObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("response");
                            if((message.contains("Wrong"))){
                                return;
                            } else {
                                Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                                intent.putExtra("email", email);
                                intent.putExtra("token", token);
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(RecoverCodeScreenActivity.this);
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

    public void checkEmail(String email) throws JSONException {
        JSONObject emailObj = new JSONObject();
        emailObj.put("email", email);
        JsonObjectRequest jsonObjectLoginRequest = new JsonObjectRequest(Request.Method.POST, URL_RECOVEREMAIL, emailObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(RecoverCodeScreenActivity.this);
                        alertBuilder.setTitle("Sucesso");
                        alertBuilder.setMessage("Código Enviado");
                        alertBuilder.show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(RecoverCodeScreenActivity.this);
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
