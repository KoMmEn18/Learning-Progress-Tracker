package tracker;

import java.util.ArrayList;
import java.util.function.ToIntFunction;

public class LevelCourseProvider extends CourseStatisticsProvider {

    LevelCourseProvider(ArrayList<Course> courses) {
        super(courses);
        courses.removeIf(o -> o.getAssignments() == 0);
    }

    public String getEasiest() {
        return getCoursesAsString(false);
    }

    public String getHardest() {
        return getCoursesAsString(true);
    }

    @Override
    protected ToIntFunction<Course> getComparativeCriterion() {
        return Course::getAverageGradePerAssignmentRatio;
    }

    @Override
    protected int getValueUsedForComparison(Course course) {
        return course.getAverageGradePerAssignmentRatio();
    }
}
