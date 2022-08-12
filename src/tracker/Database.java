package tracker;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Database {

    private LinkedHashMap<Integer, Student> students;
    private ArrayList<Course> courses;

    public Database() {
        students = new LinkedHashMap<>();
        courses = new ArrayList<>();
    }

    public void addStudent(Student user) {
        students.put(user.getId(), user);
    }

    public ArrayList<Integer> getStudentIds() {
        return new ArrayList<>(students.keySet());
    }

    public Student getStudentById(int id) {
        return students.getOrDefault(id, null);
    }

    public boolean studentExists(int id) {
        return students.containsKey(id);
    }

    public boolean isStudentAlreadyAdded(String email) {
        for (Student student : students.values()) {
            if (student.getEmail().equals(email)) {
                return true;
            }
        }

        return false;
    }

    public HashMap<Student, ArrayList<String>> getStudentsToNotify() {
        HashMap<Student, ArrayList<String>> studentsToNotify = new HashMap<>();
        for (Student student : students.values()) {
            for (Course course : courses) {
                if (student.getCoursePoints(course) >= course.getPointsToComplete() && !student.hasNotificationSent(course)) {
                    ArrayList<String> courseArray = studentsToNotify.getOrDefault(student, new ArrayList<>());
                    courseArray.add(course.getName());
                    studentsToNotify.put(student, courseArray);
                    student.markNotificationAsSend(course);
                }
            }
        }

        return studentsToNotify;
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public ArrayList<Course> getCourses() {
        return (ArrayList<Course>) courses.clone();
    }

    public int getCoursesCount() {
        return courses.size();
    }

    public String getMostPopularCourses() {
        return new PopularCourseProvider((ArrayList<Course>) courses.clone()).getMost();
    }

    public String getLeastPopularCourses() {
        return new PopularCourseProvider((ArrayList<Course>) courses.clone()).getLeast();
    }

    public String getHighestActivityCourses() {
        return new ActivityCourseProvider((ArrayList<Course>) courses.clone()).getHighest();
    }

    public String getLowestActivityCourses() {
        return new ActivityCourseProvider((ArrayList<Course>) courses.clone()).getLowest();
    }

    public String getEasiestActivityCourses() {
        return new LevelCourseProvider((ArrayList<Course>) courses.clone()).getEasiest();
    }

    public String getHardestActivityCourses() {
        return new LevelCourseProvider((ArrayList<Course>) courses.clone()).getHardest();
    }

    public boolean courseExists(String name) {
        for(Course course : courses) {
            if (course.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }

        return false;
    }

    public LinkedHashMap<Integer, CourseInfo> getStudentsForCourse(String courseName) {
        Course course = findCourseByName(courseName);
        ArrayList<Student> studentsAttending = new ArrayList<>();
        LinkedHashMap<Integer, CourseInfo> studentsAttendingMap = new LinkedHashMap<>();
        if (course != null) {
            for (Student student: students.values()) {
                if (student.isEnrolledForCourse(course)) {
                    studentsAttending.add(student);
                }
            }

            studentsAttending.sort(Comparator.comparingInt((Student student) -> student.getCoursePoints(course))
                    .reversed()
                    .thenComparingInt(Student::getId));


            for (Student student: studentsAttending) {
                int coursePoints = student.getCoursePoints(course);
                studentsAttendingMap.put(student.getId(), new CourseInfo(coursePoints, coursePoints * 100.0 / course.getPointsToComplete()));
            }
        }

        return studentsAttendingMap;
    }

    public void setCoursePointsForStudent(int studentId, LinkedHashMap<Course, Integer> coursePoints) {
        Student student = students.get(studentId);
        for (var entry : coursePoints.entrySet()) {
            Course course = entry.getKey();
            int points = entry.getValue();
            if (points != 0) {
                addActivityForCourse(course);
                addGradeForCourse(course, points);
                if (!student.isEnrolledForCourse(course)) {
                    enrollStudentForCourse(course);
                }
            }

            student.setCoursePoints(course, points);
        }
    }

    private void addGradeForCourse(Course course, int points) {
        int courseIndex = courses.indexOf(course);
        if (courseIndex != -1) {
            courses.get(courseIndex).addGrade(points);
        }
    }

    private void addActivityForCourse(Course course) {
        int courseIndex = courses.indexOf(course);
        if (courseIndex != -1) {
            courses.get(courseIndex).incrementAssignments();
        }
    }

    private void enrollStudentForCourse(Course course) {
        int courseIndex = courses.indexOf(course);
        if (courseIndex != -1) {
            courses.get(courseIndex).incrementEnrolledStudents();
        }
    }

    private Course findCourseByName(String courseName) {
        for(Course course : courses) {
            if (course.getName().equalsIgnoreCase(courseName)) {
                return course;
            }
        }

        return null;
    }
}
