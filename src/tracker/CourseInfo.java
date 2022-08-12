package tracker;

public class CourseInfo {
    private int points;
    private double completed;

    public CourseInfo(int points, double completed) {
        this.points = points;
        this.completed = completed;
    }

    public int getPoints() {
        return points;
    }

    public double getCompleted() {
        return completed;
    }
}
