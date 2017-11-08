package develop.maikeajuda.Model;

/**
 * Created by user on 01/11/2017.
 */

public class Exercise {
    private int exerciseID;
    private int exerciseCategory;
    private String exerciseName;

    public Exercise(int exerciseID, int exerciseCategory, String exerciseName) {
        this.exerciseID = exerciseID;
        this.exerciseCategory = exerciseCategory;
        this.exerciseName = exerciseName;
    }

    public int getExerciseID() {
        return exerciseID;
    }

    public void setExerciseID(int exerciseID) {
        this.exerciseID = exerciseID;
    }

    public int getExerciseCategory() {
        return exerciseCategory;
    }

    public void setExerciseCategory(int exerciseCategory) {
        this.exerciseCategory = exerciseCategory;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    @Override
    public String toString() {
        return "Exercise {" +
                "exerciseID: " + exerciseID +
                ", exerciseCategory: " + exerciseCategory +
                ", exerciseName: '" + exerciseName + '\'' +
                '}';
    }
}
