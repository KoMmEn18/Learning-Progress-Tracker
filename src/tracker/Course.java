package tracker;

import java.util.Objects;

public class Course {
    private String name;

    private int pointsToComplete;

    private int enrolledStudents = 0;
    private int assignments = 0;
    private int gradeSum = 0;

    public Course(String name, int pointsToComplete) {
        this.name = name;
        this.pointsToComplete = pointsToComplete;
    }

    public String getName() {
        return this.name;
    }

    public int getPointsToComplete() {
        return pointsToComplete;
    }

    public int getEnrolledStudents() {
        return enrolledStudents;
    }

    public int getAssignments() {
        return assignments;
    }

    public int getAverageGradePerAssignmentRatio() {
        return gradeSum * 1000 / assignments;
    }

    public void incrementEnrolledStudents() {
        enrolledStudents++;
    }

    public void incrementAssignments() {
        assignments++;
    }

    public void addGrade(int grade) {
        gradeSum += grade;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Course course = (Course) o;

        return Objects.equals(name, course.name);
    }

    @Override
    public int hashCode()
    {
        int result = name.hashCode();
        return 31 * result;
    }

    @Override
    public String toString() {
        return "[" + getName() + "]";
    }
}
