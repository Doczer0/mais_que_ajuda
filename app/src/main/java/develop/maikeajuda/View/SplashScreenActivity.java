package develop.maikeajuda.View;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import develop.maikeajuda.Application.SessionManager;
import develop.maikeajuda.R;

public class SplashScreenActivity extends AppCompatActivity {
    private Handler handler = new Handler();
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        session = new SessionManager(getApplicationContext());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isConnected()){
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Sem conexão com a internet",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        },5000);
    }

    public boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        if((cm.getActiveNetworkInfo() != null) &&(cm.getActiveNetworkInfo().isAvailable())&&(cm.getActiveNetworkInfo().isConnected())){
            return true;
        } else {
            return false;
        }
    }
}
