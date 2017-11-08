package develop.maikeajuda.Controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.File;

import develop.maikeajuda.R;



public class GalleryAdapter extends BaseAdapter {
    private File[] imagePathList;
    private LayoutInflater inflater;

    public GalleryAdapter(File[] imagePathList, LayoutInflater inflater) {
        this.imagePathList = imagePathList;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return imagePathList.length;
    }

    @Override
    public Object getItem(int position) {
        return imagePathList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_gallery, parent,false);
        File imagePath = imagePathList[position];

        ImageView image = view.findViewById(R.id.thumbnail);

        Bitmap thumbnail = BitmapFactory.decodeFile(imagePath.getAbsolutePath());
        image.setImageBitmap(thumbnail);

        return view;
    }
}
