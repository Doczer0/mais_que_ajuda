package develop.maikeajuda.Model;

/**
 * Created by user on 01/11/2017.
 */

public class Step {
    private int exerciseId;
    private String stepTitle;
    private String stepContent;

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

    @Override
    public String toString() {
        return "Step{" +
                "exerciseId=" + exerciseId +
                ", stepTitle='" + stepTitle + '\'' +
                ", stepContent='" + stepContent + '\'' +
                '}';
    }
}
