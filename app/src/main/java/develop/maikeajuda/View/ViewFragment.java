package develop.maikeajuda.View;


import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import develop.maikeajuda.Controller.DepthPageTransformer;
import develop.maikeajuda.Controller.GalleryAdapter;
import develop.maikeajuda.Controller.GalleryViewAdapter;
import develop.maikeajuda.Model.Image;
import develop.maikeajuda.R;

import static develop.maikeajuda.Application.AppConfig.IMAGE_DIRECTORY_NAME;


public class ViewFragment extends DialogFragment {
    private GalleryViewAdapter adapter;
    private ViewPager galleryViewPager;
    private LinearLayout galleryItemInfo;
    private ArrayList<Image> picturesData;
    private TextView pageCount;

    public ViewFragment() {
        // Required empty public constructor
    }

    public static ViewFragment newInstance() {
        return new ViewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view, container, false);

        picturesData = loadGalleryView();
        pageCount = view.findViewById(R.id.pager_count);
        galleryViewPager = view.findViewById(R.id.gallery_view_pager);

        galleryViewPager.setPageTransformer(true, new DepthPageTransformer());
        adapter = new GalleryViewAdapter(getLayoutInflater(), loadGalleryView());
        galleryViewPager.setAdapter(adapter);

        galleryItemInfo = view.findViewById(R.id.layout_content);

        galleryViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                pageCount.setText((position + 1) + " de " + picturesData.size());
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    private ArrayList<Image> loadGalleryView(){
        ArrayList<Image> imageArrayList = new ArrayList<>();
        File file = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY_NAME);
        int count = file.listFiles().length;
        File[] pictures = file.listFiles();
        for(int i=0;i<count;i++) {
            Bitmap picture = BitmapFactory.decodeFile(pictures[i].getAbsolutePath());
            DateFormat format = DateFormat.getDateInstance(DateFormat.FULL);
            Date data = new Date(pictures[i].lastModified());

            Image image= new Image(pictures[i].getName(), format.format(data), picture);
            image.setName(pictures[i].getName());

            imageArrayList.add(image);
        }
        return imageArrayList;
    }

}
