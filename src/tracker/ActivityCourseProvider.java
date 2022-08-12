package tracker;

import java.util.ArrayList;
import java.util.function.ToIntFunction;

public class ActivityCourseProvider extends CourseStatisticsProvider {

    ActivityCourseProvider(ArrayList<Course> courses) {
        super(courses);
    }

    public String getHighest() {
        return getCoursesAsString(false);
    }

    public String getLowest() {
        return getCoursesAsString(true);
    }

    @Override
    protected ToIntFunction<Course> getComparativeCriterion() {
        return Course::getAssignments;
    }

    @Override
    protected int getValueUsedForComparison(Course course) {
        return course.getAssignments();
    }
}
