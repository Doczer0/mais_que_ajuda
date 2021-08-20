package develop.maikeajuda.View;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;


import com.camerakit.CameraKitView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import develop.maikeajuda.Controller.CameraFilter;
import develop.maikeajuda.R;

import static develop.maikeajuda.Application.AppConfig.IMAGE_DIRECTORY_NAME;

//@SuppressLint("NewApi")
public class Camera2Activity extends AppCompatActivity {
    private static final String TAG = "AndroidCameraApi";
    private ImageView cameraFilter;
    private ImageButton buttonBack, buttonPicture, buttonFilter;
    private int drawWidth, drawHeight;
    private boolean filterState = true;
    private String exerciseName, imageLink, imageSavedLink;
    private CameraKitView cameraView;
    private Bitmap filter;
    private Handler handler = new Handler();
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);

        Intent intent = getIntent();
        exerciseName = intent.getStringExtra("exercise_name");
        imageLink = intent.getStringExtra("link");

        DisplayImageOptions display = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.img_logo)
                .showImageOnFail(R.drawable.img_error)
                .cacheInMemory(true)
                .cacheOnDisk(true).build();


        ImageLoaderConfiguration conf = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(display)
                .memoryCacheSize(50 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .threadPoolSize(3)
                .writeDebugLogs()
                .build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(conf);



        cameraView = findViewById(R.id.camera_preview2);

        buttonBack = findViewById(R.id.cam2_button_back);
        buttonPicture = findViewById(R.id.cam2_button_photo);
        buttonFilter = findViewById(R.id.cam2_button_filter);
        cameraFilter = new ImageView(getApplicationContext());
        cameraFilter.setDrawingCacheEnabled(true);
        cameraFilter.setAlpha((float) 0.5);
        cameraFilter.setScaleType(ImageView.ScaleType.FIT_CENTER);
        //cameraView.setPermissions(CameraKit.Constants.PERMISSIONS_PICTURE);

        cameraView.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                        (CameraKitView.LayoutParams.MATCH_PARENT, CameraKitView.LayoutParams.MATCH_PARENT);

                addContentView(cameraFilter,params);
                imageLoader.displayImage(imageLink,cameraFilter);

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

                cameraView.captureImage(new CameraKitView.ImageCallback() {
                    @Override
                    public void onImage(CameraKitView cameraKitView, byte[] bytes) {
                        Bitmap bitmapPicture = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        Bitmap bitmapFilter = cameraFilter.getDrawingCache();

                        Bitmap bitmapPreview = bitmapOverlay(bitmapPicture,bitmapFilter, drawWidth, drawHeight);

                        cameraFilter.setImageBitmap(bitmapPreview);
                        cameraFilter.setAlpha((float) 1.0);
                        cameraFilter.setScaleType(ImageView.ScaleType.CENTER_CROP);

                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams
                                (CameraKitView.LayoutParams.MATCH_PARENT, CameraKitView.LayoutParams.MATCH_PARENT);

                        cameraFilter.setLayoutParams(params);

                        if(filterState){
                            saveBitmap(bitmapPreview);
                        }else {
                            saveBitmap(bitmapPicture);
                        }
                    }
                });

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

    private void loadImageBitmap(){
        new Thread() {
            public void run() {
                try {
                    URL url = new URL(imageLink);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    InputStream input = connection.getInputStream();
                    filter = BitmapFactory.decodeStream(input);
                } catch(IOException e) {
                    //showToast(e.getMessage());
                }
            }
        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.onResume();
        hideNavigationBar();
    }

    @Override
    protected void onPause() {
        hideNavigationBar();
        super.onPause();
        cameraView.onPause();
    }

    public Bitmap bitmapOverlay(Bitmap bitmapImage, Bitmap bitmapFilter, int width, int height) {
        Paint paint = new Paint();
        paint.setAlpha(150);
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

            File f = getApplicationContext().getFilesDir();
            Toast.makeText(getApplicationContext(),getApplicationContext().getFilesDir().toString(),Toast.LENGTH_SHORT).show();

            File file = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY_NAME);

            if(!file.mkdir()){
                File ifile = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY_NAME + fileName);
                FileOutputStream outStream = new FileOutputStream(ifile);
                pBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                outStream.close();
            } else {
                file.mkdir();
                File ifile = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY_NAME + fileName);
                FileOutputStream outStream = new FileOutputStream(ifile);
                pBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                outStream.close();
            }
            //Toast.makeText(getApplicationContext(),"Foto salva com sucesso.", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            //Toast.makeText(getApplicationContext(),"Não foi possivel salvar a foto: "+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void hideNavigationBar(){
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    @Override
    protected void onStart() {
        hideNavigationBar();
        super.onStart();
        cameraView.onStart();
    }

    @Override
    protected void onStop() {
        hideNavigationBar();
        super.onStop();
        cameraView.onStop();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        hideNavigationBar();
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
