package develop.maikeajuda.Controller;


import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        View view = inflater.inflate(R.layout.view_image, container, false);
        Image picture = pictures.get(position);

        TextView imageTitle = view.findViewById(R.id.image_view_title);
        TextView imageData = view.findViewById(R.id.image_view_date);
        ImageView imageView = view.findViewById(R.id.image_picture);

        imageTitle.setText(picture.getName());
        imageData.setText(""+picture.getData());

        imageView.setImageBitmap(picture.getPicture());

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
