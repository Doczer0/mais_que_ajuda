package develop.maikeajuda.Controller;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bluejamesbond.text.DocumentView;
import com.bluejamesbond.text.style.TextAlignment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;

import java.util.List;

import develop.maikeajuda.Model.Step;
import develop.maikeajuda.R;


public class StepAdapter extends PagerAdapter {
    private List<Step> exerciseStepsList;
    private Context context;
    private Activity activity;
    private ImageLoader imageLoader;

    public StepAdapter(List<Step> exerciseStepsList, Context context, Activity activity) {
        this.exerciseStepsList = exerciseStepsList;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return exerciseStepsList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LinearLayout linearLayout = new LinearLayout(context);

        linearLayout.setBackgroundColor(Color.WHITE);

        Step exerciseStage = exerciseStepsList.get(position);
        String stepType = exerciseStage.getStepType();

        DisplayImageOptions display = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.img_logo)
                .showImageOnFail(R.drawable.img_error)
                .cacheInMemory(true)
                .cacheOnDisk(true).build();


        ImageLoaderConfiguration conf = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(display)
                .memoryCacheSize(50 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .threadPoolSize(3)
                .writeDebugLogs()
                .build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(conf);

        Typeface font = Typeface.createFromAsset(activity.getAssets(),"fonts/SourceSansProLight.ttf");
        Typeface font2 = Typeface.createFromAsset(activity.getAssets(),"fonts/SourceSansPro.ttf");

        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        //linearLayout.setPadding(20,20,20,20);
        linearLayout.setLayoutParams(layoutParams);
        //layoutParams.setMargins(20,20,20,20);
        container.addView(linearLayout);

        //Definição dos parametros de layout para a imagem do passo
        RelativeLayout.LayoutParams layoutImage = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        //layoutImage.weight=1;
        layoutImage.setMargins(0,0,0,0);

        //Definição dos parametros de layout para o titulo do passo
        LinearLayout.LayoutParams layoutTitle = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutTitle.setMargins(0,70,0,20);

        //Definição dos parametros de layout para o conteudo do passo
        LinearLayout.LayoutParams layoutContent = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        //layoutContent.weight=0;
        layoutContent.setMargins(20,30,20,20);

        if(stepType.equals("text")){
            TextView textViewTitle = new TextView(context);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                textViewTitle.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }

            textViewTitle.setText(exerciseStage.getStepTitle());
            textViewTitle.setTypeface(font);
            textViewTitle.setTextColor(activity.getResources().getColor(R.color.colorPrimaryText));
            textViewTitle.setTextSize(30);
            textViewTitle.setLayoutParams(layoutTitle);

            linearLayout.addView(textViewTitle);

            String content = exerciseStage.getStepContent();
            String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/SourceSansProLight.ttf\")}body {font-family: MyFont;font-size: medium;text-align: justify;}</style></head><body>";
            String pas = "</body></html>";
            String text = pish + content + pas;
            WebView webView = new WebView(context);
            webView.loadData(text, "text/html; charset=utf-8",  "utf-8");

            webView.setLayoutParams(layoutContent);

            linearLayout.addView(webView);
        } else if(stepType.equals("image")) {

            ImageView imageViewStep = new ImageView(context);
            imageViewStep.setPadding(0,0,0,0);
            imageViewStep.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            imageLoader.displayImage(exerciseStage.getStepContent(),imageViewStep);
            //Picasso.with(context).load(exerciseStage.getStepContent()).error(R.drawable.img_error).into(imageViewStep);
            imageViewStep.setLayoutParams(layoutImage);

            linearLayout.addView(imageViewStep);
        } else {
            ImageView imageViewStep = new ImageView(context);
            imageViewStep.setPadding(0,0,0,0);
            imageViewStep.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageLoader.displayImage(exerciseStage.getStepContent(),imageViewStep);
            //Picasso.with(context).load(R.drawable.img_logo).into(imageViewStep);
            imageViewStep.setLayoutParams(layoutImage);

            linearLayout.addView(imageViewStep);
        }

        return (linearLayout);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
