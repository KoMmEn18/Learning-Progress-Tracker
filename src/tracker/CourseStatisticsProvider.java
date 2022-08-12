package tracker;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

abstract class CourseStatisticsProvider {

    protected ArrayList<Course> courses;

    CourseStatisticsProvider(ArrayList<Course> courses) {
        this.courses = courses;
    }

    protected ArrayList<Course> getCourses(boolean ascending) {
        if (ascending) {
            courses.sort(Comparator.comparingInt(getComparativeCriterion()));
        } else {
            courses.sort(Comparator.comparingInt(getComparativeCriterion()).reversed());
        }

        ArrayList<Course> outputCourses = new ArrayList<>();
        boolean cannotRetrieveData = courses.stream().allMatch(o -> getValueUsedForComparison(o) == 0)
                || ascending && courses.stream().allMatch(o -> getValueUsedForComparison(o) == getValueUsedForComparison(courses.get(0)));
        if (!cannotRetrieveData) {
            Course firstOne = courses.get(0);
            outputCourses.add(firstOne);
            for (int i = 1; i < courses.size(); i++) {
                if ((!ascending && getValueUsedForComparison(courses.get(i)) < getValueUsedForComparison(firstOne))
                        || (ascending && getValueUsedForComparison(courses.get(i)) > getValueUsedForComparison(firstOne))) {
                    break;
                }
                outputCourses.add(courses.get(i));
            }
        }

        return outputCourses;
    }

    protected String getCoursesAsString(boolean ascending) {
        ArrayList<Course> courses = getCourses(ascending);

        return courses.isEmpty() ? "n/a" : courses
                .stream()
                .map(o -> " " + o.getName())
                .collect(Collectors.joining(", "));
    }

    abstract protected ToIntFunction<Course> getComparativeCriterion();

    abstract protected int getValueUsedForComparison(Course course);
}
