package tracker;

import tracker.exceptions.InvalidCredentialsException;
import tracker.exceptions.StudentAlreadyExistException;

import java.util.*;

public class LearningProgressTrackerApp {

    private static final String APP_NAME = "Learning Progress Tracker";
    private final Scanner scanner = new Scanner(System.in);

    private final Database database = new Database();

    public LearningProgressTrackerApp() {
        addCourses();
    }

    public void start() {
        System.out.println(APP_NAME);
        while (true) {
            readUserCommand();
        }
    }

    private void readUserCommand() {
        String command = scanner.nextLine().toLowerCase();
        if (command.isEmpty() || command.isBlank()) {
            System.out.println("no input");
        } else {
            switch (command) {
                case "add students":
                    addStudents();
                    break;
                case "add points":
                    addPointsForStudent();
                    break;
                case "list":
                    listStudents();
                    break;
                case "find":
                    findStudent();
                    break;
                case "statistics":
                    showStatistics();
                    break;
                case "notify":
                    notifyStudents();
                    break;
                case "back":
                    System.out.println("Enter 'exit' to exit the program");
                    break;
                case "exit":
                    processExit();
                    break;
                default:
                    System.out.println("Error: unknown command!");
            }
        }
    }

    private void notifyStudents() {
        HashMap<Student, ArrayList<String>> studentsToNotify = database.getStudentsToNotify();
        int studentsNotified = 0;
        for (var entry : studentsToNotify.entrySet()) {
            Student student = entry.getKey();
            ArrayList<String> courses = entry.getValue();
            for (String course : courses) {
                System.out.println("To: " + student.getEmail());
                System.out.println("Re: Your Learning Progress");
                System.out.println("Hello, " + student.getFullName() + "! You have accomplished our " + course + " course!");
            }
            studentsNotified++;
        }
        System.out.println("Total " + studentsNotified + " students have been notified.");
    }

    private void showStatistics() {
        System.out.println("Type the name of a course to see details or 'back' to quit");
        printCoursesStatistics();
        while (true) {
            String userInput = scanner.nextLine();
            if (userInput.equals("back")) {
                break;
            }

            if (database.courseExists(userInput)) {
                System.out.println(userInput.substring(0, 1).toUpperCase() + userInput.substring(1).toLowerCase());
                System.out.printf("%-4s %-8s %-11s %n", "id", "points", "completed");
                LinkedHashMap<Integer, CourseInfo> students = database.getStudentsForCourse(userInput);
                for (var studentEntry : students.entrySet()) {
                    int studentId = studentEntry.getKey();
                    CourseInfo courseInfo = studentEntry.getValue();
                    System.out.printf("%-4d %-8d %-12s %n", studentId, courseInfo.getPoints(), Math.round(courseInfo.getCompleted() * 10.0) / 10.0 + "%");
                }
            } else {
                System.out.println("Unknown course");
            }
        }

    }

    private void printCoursesStatistics() {
        System.out.println("Most popular: " + database.getMostPopularCourses());
        System.out.println("Least popular: " + database.getLeastPopularCourses());
        System.out.println("Highest activity: " + database.getHighestActivityCourses());
        System.out.println("Lowest activity: " + database.getLowestActivityCourses());
        System.out.println("Easiest course: " + database.getEasiestActivityCourses());
        System.out.println("Hardest course: " + database.getHardestActivityCourses());
    }

    private void findStudent() {
        System.out.println("Enter an id or 'back' to return");
        while (true) {
            String userInput = scanner.nextLine();
            if (userInput.equals("back")) {
                break;
            }

            try {
                int studentId = Integer.parseUnsignedInt(userInput);
                Student student = database.getStudentById(studentId);
                if (student != null) {
                    student.printDetails();
                } else {
                    System.out.println("No student is found for id=" + userInput);
                }

            } catch (NumberFormatException ex) {
                System.out.println("No student is found for id=" + userInput);
            }
        }
    }

    private void addPointsForStudent() {
        System.out.println("Enter an id and points or 'back' to return");
        while (true) {
            String command = scanner.nextLine();
            if (command.equals("back")) {
                break;
            }

            String[] pointsData = command.split("\\s");
            if (pointsData.length != (1 + database.getCoursesCount())) {
                System.out.println("Incorrect points format");
                continue;
            }

            boolean studentExists = false;
            int studentId = 0;
            try {
                studentId = Integer.parseUnsignedInt(pointsData[0]);
                studentExists = database.studentExists(studentId);
            } catch (NumberFormatException ignored) {}

            if (studentExists) {
                try {
                    int iterator = 1;
                    LinkedHashMap<Course, Integer> points = new LinkedHashMap<>();
                    for (Course course : database.getCourses()) {
                        points.put(course, Integer.parseUnsignedInt(pointsData[iterator++]));
                    }

                    database.setCoursePointsForStudent(studentId, points);
                    System.out.println("Points updated");
                } catch (NumberFormatException ex) {
                    System.out.println("Incorrect points format");
                }
            } else {
                System.out.println("No student is found for id=" + pointsData[0]);
            }
        }
    }

    private void listStudents() {
        var studentIds = database.getStudentIds();
        if (studentIds.size() == 0) {
            System.out.println("No students found");
        } else {
            System.out.println("Students:");
            for (int id : studentIds) {
                System.out.println(id);
            }
        }
    }

    private void addStudents() {
        int studentsAdded = 0;
        System.out.println("Enter student credentials or 'back' to return");
        while (true) {
            String command = scanner.nextLine();
            if (command.equals("back")) {
                break;
            }

            String[] credentials = command.split("\\s");
            if (credentials.length < 3) {
                System.out.println("Incorrect credentials");
                continue;
            }

            String firstName = credentials[0];
            String lastName = String.join(" ", Arrays.copyOfRange(credentials, 1, credentials.length - 1));
            String email = credentials[credentials.length - 1];

            try {
                addNewUser(firstName, lastName, email);
                studentsAdded++;
                System.out.println("Student has been added.");
            } catch (InvalidCredentialsException|StudentAlreadyExistException ex) {
                System.out.println(ex.getMessage());
            }
        }
        System.out.println("Total " + studentsAdded + " students have been added.");
    }

    private void addNewUser(String firstName, String lastName, String email) {
        if (!InputValidator.isFirstName(firstName)) {
            throw new InvalidCredentialsException("Incorrect first name");
        } else if (!InputValidator.isLastName(lastName)) {
            throw new InvalidCredentialsException("Incorrect last name");
        } else if (!InputValidator.isEmailAddress(email)) {
            throw new InvalidCredentialsException("Incorrect email");
        } else if (database.isStudentAlreadyAdded(email)) {
            throw new StudentAlreadyExistException("This email is already taken.");
        }

        database.addStudent(new Student(firstName, lastName, email));
    }

    private void addCourses() {
        database.addCourse(new Course("Java", 600));
        database.addCourse(new Course("DSA", 400));
        database.addCourse(new Course("Databases", 480));
        database.addCourse(new Course("Spring", 550));
    }

    private void processExit() {
        System.out.println("Bye");
        System.exit(1);
    }
}
