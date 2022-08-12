package tracker;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class InputValidatorTest {

    @ParameterizedTest
    @ValueSource(strings = {"Juan", "Jean-Claude", "O'Neill", "O'N'e'ill-Sad"})
    void testIsFirstName(String string) {
        assertTrue(InputValidator.isFirstName(string));
    }

    @ParameterizedTest
    @ValueSource(strings = {"o", "Stanisław Oğuz", "'ONeill", "ONeill'", "ONeill-", "-ONeill", "O''Neill"})
    void testIsNotFirstName(String string) {
        assertFalse(InputValidator.isFirstName(string));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Helsing", "Luise Johnson", "van Helsing", "van Hel's'ing"})
    void testIsLastName(String string) {
        assertTrue(InputValidator.isLastName(string));
    }

    @ParameterizedTest
    @ValueSource(strings = {"港", "D.", "Da'", "Oğuz"})
    void testIsNotLastName(String string) {
        assertFalse(InputValidator.isLastName(string));
    }

    @ParameterizedTest
    @ValueSource(strings = {"test@test.eu", "t@t.e"})
    void testIsEmailAddress(String string) {
        assertTrue(InputValidator.isEmailAddress(string));
    }

    @ParameterizedTest
    @ValueSource(strings = {"test@test", "email@emailxyz", "emailemail.xyz", "email@e@mail.xyz"})
    void testIsNotEmailAddress(String string) {
        assertFalse(InputValidator.isEmailAddress(string));
    }
}
