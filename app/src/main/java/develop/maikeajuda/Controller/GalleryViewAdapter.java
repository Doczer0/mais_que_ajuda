package develop.maikeajuda.Controller;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import develop.maikeajuda.Model.Image;
import develop.maikeajuda.R;

/**
 * Created by user on 06/11/2017.
 */

public class GalleryViewAdapter extends PagerAdapter {
    private LayoutInflater inflater;
    private ArrayList<Image> pictures;

    public GalleryViewAdapter(LayoutInflater inflater, ArrayList<Image> pictures) {
        this.inflater = inflater;
        this.pictures = pictures;
    }

    @Override
    public int getCount() {
        return pictures.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final View view = inflater.inflate(R.layout.view_image, container, false);
        Image picture = pictures.get(position);


        TextView imageTitle = view.findViewById(R.id.image_view_title);
        TextView imageData = view.findViewById(R.id.image_view_date);
        ImageView imageView = view.findViewById(R.id.image_picture);

        imageTitle.setText(picture.getName());
        imageData.setText(""+picture.getData());

        imageView.setImageBitmap(picture.getPicture());

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Toast.makeText(view.getContext(),"Teste",Toast.LENGTH_SHORT).show();
                LinearLayout galleryItemInfo = view.findViewById(R.id.content);
                float y = galleryItemInfo.getY();
                float y2 = y + 120;
                ObjectAnimator animation = ObjectAnimator.ofFloat(galleryItemInfo, "y", y, y2);
                animation.setDuration(2000);
                animation.start();
                */
            }
        });

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
