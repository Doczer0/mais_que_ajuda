package develop.maikeajuda.Model;

import android.support.annotation.NonNull;

/**
 * Created by user on 01/11/2017.
 */

public class Step implements Comparable<Step> {
    private int exerciseId;
    private String stepTitle;
    private String stepContent;
    private String stepType;

    public Step(int exerciseId, String stepTitle, String stepContent, String stepType) {
        this.exerciseId = exerciseId;
        this.stepTitle = stepTitle;
        this.stepContent = stepContent;
        this.stepType = stepType;
    }

    public Step(int exerciseId, String stepTitle, String stepContent) {
        this.exerciseId = exerciseId;
        this.stepTitle = stepTitle;
        this.stepContent = stepContent;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getStepTitle() {
        return stepTitle;
    }

    public void setStepTitle(String stepTitle) {
        this.stepTitle = stepTitle;
    }

    public String getStepContent() {
        return stepContent;
    }

    public void setStepContent(String stepContent) {
        this.stepContent = stepContent;
    }

    public String getStepType() {
        return stepType;
    }

    public void setStepType(String stepType) {
        this.stepType = stepType;
    }

    @Override
    public String toString() {
        return "Step{" +
                "exerciseId=" + exerciseId +
                ", stepTitle='" + stepTitle + '\'' +
                ", stepContent='" + stepContent + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull Step o) {
        if (this.exerciseId < o.exerciseId) {
            return -1;
        }
        if (this.exerciseId > o.exerciseId) {
            return 1;
        }
        return 0;
    }
}
