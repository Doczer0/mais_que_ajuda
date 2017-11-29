package develop.maikeajuda.Controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import develop.maikeajuda.Model.Exercise;
import develop.maikeajuda.R;


public class ExerciseAdapter extends BaseAdapter {
    private final List<Exercise> itemList;
    private final Activity activity;

    public ExerciseAdapter(List<Exercise> itemList, Activity activity) {
        this.itemList = itemList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = activity.getLayoutInflater().inflate(R.layout.item_exercise,parent,false);
        Exercise item = itemList.get(position);

        TextView exercise = view.findViewById(R.id.exercise_item_title);
        Typeface font = Typeface.createFromAsset(activity.getAssets(),"fonts/SourceSansPro.ttf");

        exercise.setTypeface(font);
        exercise.setText(item.getExerciseName());

        if(item.getExerciseName().length()>15){
            exercise.setTextSize(15);
        } else{
            exercise.setTextSize(18);
        }
        return view;
    }
}
