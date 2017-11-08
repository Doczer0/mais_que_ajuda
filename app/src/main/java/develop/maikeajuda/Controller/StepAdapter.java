package develop.maikeajuda.Controller;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bluejamesbond.text.DocumentView;
import com.bluejamesbond.text.style.TextAlignment;
import com.squareup.picasso.Picasso;

import java.util.List;

import develop.maikeajuda.Model.Step;
import develop.maikeajuda.R;


public class StepAdapter extends PagerAdapter {
    private List<Step> exerciseStepsList;
    private Context context;
    private Activity activity;

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

        Step exerciseStage = exerciseStepsList.get(position);

        Typeface font = Typeface.createFromAsset(activity.getAssets(),"fonts/SourceSansPro.ttf");
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

        if(position%2!=0){
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

            DocumentView documentView = new DocumentView(context, DocumentView.PLAIN_TEXT);
            documentView.getDocumentLayoutParams().setTextAlignment(TextAlignment.JUSTIFIED);
            documentView.getDocumentLayoutParams().setTextColor(R.color.colorPrimaryText);
            documentView.getDocumentLayoutParams().setTextSize(20);
            documentView.getDocumentLayoutParams().setTextTypeface(font);
            documentView.getDocumentLayoutParams().setHyphen("-");
            documentView.getDocumentLayoutParams().setInsetPaddingLeft(10);
            documentView.getDocumentLayoutParams().setInsetPaddingTop(10);
            documentView.getDocumentLayoutParams().setInsetPaddingRight(10);
            documentView.getDocumentLayoutParams().setInsetPaddingBottom(10);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                documentView.setNestedScrollingEnabled(true);
            }
            documentView.setText(exerciseStage.getStepContent());
            documentView.setLayoutParams(layoutContent);

            linearLayout.addView(documentView);

        }else{
            ImageView imageViewStep = new ImageView(context);
            imageViewStep.setPadding(0,0,0,0);
            imageViewStep.setScaleType(ImageView.ScaleType.FIT_XY);
            Picasso.with(context).load(exerciseStage.getStepContent())
                    .error(R.drawable.img_error).into(imageViewStep);
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
