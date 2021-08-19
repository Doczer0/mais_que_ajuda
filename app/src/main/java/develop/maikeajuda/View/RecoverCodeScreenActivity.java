package develop.maikeajuda.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import develop.maikeajuda.R;

public class RecoverCodeScreenActivity extends AppCompatActivity {
    private EditText editTxtRecoveryCode1, editTxtRecoveryCode2, editTxtRecoveryCode3, editTxtRecoveryCode4, editTxtRecoveryCode5;
    private TextView txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_code);

        editTxtRecoveryCode1 = findViewById(R.id.editText_recoverPasswordCode);
        editTxtRecoveryCode2 = findViewById(R.id.editText_recoverPasswordCode);
        editTxtRecoveryCode3 = findViewById(R.id.editText_recoverPasswordCode);
        editTxtRecoveryCode4 = findViewById(R.id.editText_recoverPasswordCode);
        editTxtRecoveryCode5 = findViewById(R.id.editText_recoverPasswordCode);

        editTxtRecoveryCode1.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1) });
        editTxtRecoveryCode2.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1) });
        editTxtRecoveryCode3.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1) });
        editTxtRecoveryCode4.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1) });
        editTxtRecoveryCode5.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1) });

        txtEmail = findViewById(R.id.txtView_recoverEmail);
        Button btnSendCode = findViewById(R.id.btnSendCode);

        String emailRecover = getIntent().getStringExtra("email");

        System.out.println(emailRecover);

        txtEmail.setText(emailRecover);

        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
