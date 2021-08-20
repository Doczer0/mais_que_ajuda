package develop.maikeajuda.View;


import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import develop.maikeajuda.Controller.GalleryAdapter;
import develop.maikeajuda.R;

import static develop.maikeajuda.Application.AppConfig.IMAGE_DIRECTORY_NAME;

public class GalleryFragment extends Fragment {
    private GridView gallery;
    private TextView textGalleryNotify, textGalleryTitle;
    private GalleryAdapter adapter;

    public GalleryFragment() {
        // Required empty public constructor
    }

    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/SourceSansPro.ttf");


        gallery = view.findViewById(R.id.gridView_gallery);
        textGalleryNotify = view.findViewById(R.id.text_gallery);
        textGalleryTitle = view.findViewById(R.id.text_gallery_title);

        textGalleryNotify.setTypeface(font);
        textGalleryNotify.setTypeface(font);
        textGalleryTitle.setTypeface(font);
        loadGallery();

        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ViewFragment newFragment = ViewFragment.newInstance();
                newFragment.show(ft, "Galeria");
            }
        });

        return view;
    }

    private void loadGallery(){
        File file = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY_NAME);
        if(file.listFiles() == null){
            textGalleryTitle.setVisibility(View.GONE);
            gallery.setVisibility(View.GONE);
            textGalleryNotify.setVisibility(View.VISIBLE);
            textGalleryNotify.setText("Sem fotos na galeria");
        } else {
            File[] pictures;
            pictures = file.listFiles();
            adapter = new GalleryAdapter(pictures,getLayoutInflater());
            gallery.setAdapter(adapter);
            textGalleryTitle.setVisibility(View.VISIBLE);
            gallery.setVisibility(View.VISIBLE);
            textGalleryNotify.setVisibility(View.GONE);
        }

    }

}
