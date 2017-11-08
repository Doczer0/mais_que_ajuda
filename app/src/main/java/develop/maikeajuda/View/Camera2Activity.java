package develop.maikeajuda.View;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.wonderkiln.camerakit.CameraListener;
import com.wonderkiln.camerakit.CameraView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import develop.maikeajuda.Controller.CameraFilter;
import develop.maikeajuda.R;

import static develop.maikeajuda.Application.AppConfig.IMAGE_DIRECTORY_NAME;

//@SuppressLint("NewApi")
public class Camera2Activity extends AppCompatActivity {
    private static final String TAG = "AndroidCameraApi";
    private CameraFilter cameraFilter;
    private ImageButton buttonBack, buttonPicture, buttonFilter;
    private int drawWidth, drawHeight;
    private boolean filterState = true;
    private String exerciseName;
    private CameraView cameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);

        Intent intent = getIntent();
        exerciseName = intent.getStringExtra("exercise_name");

        cameraView = findViewById(R.id.camera_preview2);
        buttonBack = findViewById(R.id.cam2_button_back);
        buttonPicture = findViewById(R.id.cam2_button_photo);
        buttonFilter = findViewById(R.id.cam2_button_filter);

        cameraView.post(new Runnable() {
            @Override
            public void run() {
                drawWidth = cameraView.getWidth();
                drawHeight = cameraView.getHeight();
                cameraFilter = new CameraFilter(getApplicationContext(),drawWidth,drawHeight, null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                        (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                addContentView(cameraFilter, params);
            }
        });

        cameraView.setCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(final byte[] jpeg) {
                Thread timer = new Thread() {
                    @Override
                    public void run() {
                        Bitmap bitmapPicture = BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length);
                        Bitmap bitmapFilter = cameraFilter.getFilter();

                        Bitmap bitmapPreview = bitmapOverlay(bitmapPicture,bitmapFilter, drawWidth, drawHeight);
                        if(filterState){
                            saveBitmap(bitmapPreview);
                        }else {
                            saveBitmap(bitmapPicture);
                        }
                        finish();
                    }
                };
                timer.start();

            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.captureImage();
            }
        });

        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filterState){
                    buttonFilter.setImageResource(R.drawable.ic_filter_off);
                    cameraFilter.setVisibility(View.INVISIBLE);
                    filterState = false;
                }else {
                    buttonFilter.setImageResource(R.drawable.ic_filter_on);
                    cameraFilter.setVisibility(View.VISIBLE);
                    filterState = true;
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        cameraView.stop();
        super.onPause();
    }

    public Bitmap bitmapOverlay(Bitmap bitmapImage, Bitmap bitmapFilter, int width, int height) {
        Paint paint = new Paint();
        paint.setAlpha(200);
        Rect source = new Rect(0,0,bitmapImage.getWidth(), bitmapImage.getHeight());
        Rect destiny = new Rect(0,0,bitmapImage.getWidth(), bitmapImage.getHeight());

        Bitmap newBitmapFilter  = Bitmap.createScaledBitmap(bitmapFilter,bitmapImage.getWidth(),bitmapImage.getHeight(), false);
        Bitmap bitmapOverlay = Bitmap.createBitmap(bitmapImage.getWidth(), bitmapImage.getHeight(), bitmapImage.getConfig());

        Canvas canvas = new Canvas(bitmapOverlay);
        canvas.drawBitmap(bitmapImage, 0, 0, null);

        canvas.drawBitmap(newBitmapFilter, source, destiny, paint);

        return bitmapOverlay;
    }

    public void saveBitmap(Bitmap pBitmap){
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HH_mm_ss");
            Date data = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(data);
            Date data_atual = cal.getTime();
            String data_completa = dateFormat.format(data_atual);
            String fileName = data_completa+".png";

            File file = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY_NAME);
            file.mkdir();

            File ifile = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY_NAME + fileName);
            FileOutputStream outStream = new FileOutputStream(ifile);
            pBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.close();

            //Toast.makeText(getApplicationContext(),"Foto salva com sucesso.", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            //Toast.makeText(getApplicationContext(),"Não foi possivel salvar a foto: "+e.toString(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, e.toString());
        }
    }

}
