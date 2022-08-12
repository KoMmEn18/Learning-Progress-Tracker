package tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

public class Student {
    private static int students = 0;
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private LinkedHashMap<Course, Integer> courseProgress = new LinkedHashMap<>();
    private HashSet<Course> notifications = new HashSet<>();

    public Student(String firstName, String lastName, String email) {
        this.id = ++students;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    public String getEmail() {
        return email;
    }

    public boolean isEnrolledForCourse(Course course) {
        return courseProgress.getOrDefault(course, 0) != 0;
    }

    public void setCoursePoints(Course course, int points) {
        courseProgress.put(course, courseProgress.getOrDefault(course, 0) + points);
    }

    public int getCoursePoints(Course course) {
        return courseProgress.getOrDefault(course, 0);
    }

    public boolean hasNotificationSent(Course course) {
        return notifications.contains(course);
    }

    public void markNotificationAsSend(Course course) {
        notifications.add(course);
    }

    public void printDetails() {
        System.out.print(id + " points:");
        System.out.println(courseProgress.entrySet()
                .stream()
                .map(e -> " " + e.getKey().getName() + "=" + e.getValue())
                .collect(Collectors.joining(";")));
    }
}
