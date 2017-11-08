package develop.maikeajuda.Controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import develop.maikeajuda.Application.ApplicationControler;
import develop.maikeajuda.R;


public class CameraFilter extends View {
    private int drawOnTopWidth;
    private int drawOnTopHeight;
    private Bitmap filterImage, mBitmap;

    public CameraFilter(Context context, int drawOnTopWidth, int drawOnTopHeight, Bitmap filterImage) {
        super(context);
        this.drawOnTopWidth = drawOnTopWidth;
        this.drawOnTopHeight = drawOnTopHeight;
        this.filterImage = filterImage;

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inDensity = ApplicationControler.DEVICE_DENSITY_DPI;
        opt.inScaled = true;
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_logo, opt);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mBitmap = Bitmap.createScaledBitmap(mBitmap,drawOnTopWidth,drawOnTopHeight,true);

        Paint paint = new Paint();
        paint.setAlpha(180);
        Rect source = new Rect(0,0,drawOnTopWidth,drawOnTopHeight);
        Rect destiny = new Rect(0,0,drawOnTopWidth,drawOnTopHeight);

        canvas.drawBitmap(mBitmap, source, destiny, paint);
        super.onDraw(canvas);
    }

    public Bitmap getFilter(){
        return mBitmap;
    }
}
