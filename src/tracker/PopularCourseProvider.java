package tracker;

import java.util.ArrayList;
import java.util.function.ToIntFunction;

public class PopularCourseProvider extends CourseStatisticsProvider {

    PopularCourseProvider(ArrayList<Course> courses) {
        super(courses);
    }

    public String getMost() {
        return getCoursesAsString(false);
    }

    public String getLeast() {
        return getCoursesAsString(true);
    }

    @Override
    protected ToIntFunction<Course> getComparativeCriterion() {
        return Course::getEnrolledStudents;
    }

    @Override
    protected int getValueUsedForComparison(Course course) {
        return course.getEnrolledStudents();
    }
}
