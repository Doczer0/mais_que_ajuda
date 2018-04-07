package develop.maikeajuda.Application;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;

import static develop.maikeajuda.Application.AppConfig.IMAGE_DIRECTORY_NAME;


public class ApplicationControler extends Application {
    public static final String TAG = ApplicationControler.class.getSimpleName();
    private static final String LOG_TAG = "REG";
    public static int DEVICE_DENSITY_DPI;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private static ApplicationControler appInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
        DisplayMetrics metrics = new DisplayMetrics();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
        WindowManager windowManager = (WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        DEVICE_DENSITY_DPI = metrics.densityDpi;
        File file = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY_NAME);
        if(!file.exists()) {
            file.mkdir();
        }
    }

    public static synchronized ApplicationControler getInstance() {
        return appInstance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }
}
