package tracker;

public class InputValidator {
    public static boolean isFirstName(String firstName) {
        return firstName.matches("(?:[A-Za-z]+['-]?)+[A-Za-z]+");
    }

    public static boolean isLastName(String lastName) {
        return lastName.matches("(?:(?:[A-Za-z]+['-]?)+[A-Za-z]+[ ]*)*");
    }

    public static boolean isEmailAddress(String email) {
        return email.matches("[^@]+@[^@]+\\.[^@]+");
    }
}
