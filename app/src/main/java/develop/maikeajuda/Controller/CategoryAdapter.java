package develop.maikeajuda.Controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import develop.maikeajuda.Model.Category;
import develop.maikeajuda.R;


public class CategoryAdapter extends BaseAdapter {
    private final List<Category> categoriesList;
    private LayoutInflater inflater;
    private final Activity activity;

    public CategoryAdapter(List<Category> categoriesList, Activity activity, LayoutInflater inflater) {
        this.inflater = inflater;
        this.categoriesList = categoriesList;
        this.activity = activity;
    }


    @Override
    public int getCount() {
        return categoriesList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoriesList.get(position) ;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_category, parent,false);
        Category category = categoriesList.get(position);

        TextView title = view.findViewById(R.id.category_item_title);
        TextView description = view.findViewById(R.id.category_item_description);
        String texto = (String) activity.getText(R.string.text_test);
        Typeface font = Typeface.createFromAsset(activity.getAssets(),"fonts/SourceSansPro.ttf");

        title.setTypeface(font);
        title.setText(category.getCategoryName());
        description.setTypeface(font);
        description.setText(texto);

        return view;
    }
}
