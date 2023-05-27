import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

public class HorseTest {
    @Test
    public void nullNameException() {
        assertThrows(IllegalArgumentException.class, () -> new Horse(null, 20, 100));
    }

    @Test
    public void nullNameExceptionMessage() {
        try {
            new Horse(null, 20, 100);
        } catch (IllegalArgumentException e) {
            assertEquals("Name cannot be null.", e.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  ", "\t", "\t\t", "\n", "\n\n\n"})
    public void blankNameException(String name) {
        assertThrows(IllegalArgumentException.class, () -> new Horse(name, 20, 100));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  ", "\t", "\t\t", "\n", "\n\n\n"})
    public void blankNameExceptionMessage(String name) {
        try {
            new Horse(name, 20, 100);
        } catch (IllegalArgumentException e) {
            assertEquals("Name cannot be blank.", e.getMessage());
        }
    }

    @Test
    public void negativeSpeedException() {
        assertThrows(IllegalArgumentException.class, () -> new Horse("Конь", -5, 100));
    }

    @Test
    public void negativeSpeedExceptionMessage() {
        try {
            new Horse("Конь", -5, 100);
        } catch (IllegalArgumentException e) {
            assertEquals("Speed cannot be negative.", e.getMessage());
        }
    }

    @Test
    public void negativeDistanceException() {
        assertThrows(IllegalArgumentException.class, () -> new Horse("Конь", 20, -100));
    }

    @Test
    public void negativeDistanceExceptionMessage() {
        try {
            new Horse("Конь", 20, -100);
        } catch (IllegalArgumentException e) {
            assertEquals("Distance cannot be negative.", e.getMessage());
        }
    }

    @Test
    public void getName() throws NoSuchFieldException, IllegalAccessException {
        Horse horse = new Horse("Конь", 20, 100);
        Field name = Horse.class.getDeclaredField("name");
        name.setAccessible(true);

        String nameValue = (String) name.get(horse);

        assertEquals("Конь", nameValue);
    }

    @Test
    public void getSpeed() {
        Horse horse = new Horse("Конь", 20, 100);

        assertEquals(20, horse.getSpeed());
    }

    @Test
    public void getDistance() {
        Horse horse = new Horse("Конь", 20, 100);

        assertEquals(100, horse.getDistance());
    }

    @Test
    public void returnZeroByConstructorWithTwoParam() {
        Horse horse = new Horse("Конь", 20);

        assertEquals(0, horse.getDistance());
    }

    @Test
    public void moveUsesGetRandomDouble() {
        try (MockedStatic<Horse> mockedStatic = mockStatic(Horse.class)) {
            new Horse("Конь", 20, 100).move();

            mockedStatic.verify(() -> Horse.getRandomDouble(0.2, 0.9));
        }
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, 0.1, 0.2, 0.3, 1.0, 100.99})
    public void move(double random) {
        try (MockedStatic<Horse> mockedStatic = mockStatic(Horse.class)) {
            Horse horse = new Horse("Конь", 20, 100);
            mockedStatic.when(() -> Horse.getRandomDouble(0.2, 0.9)).thenReturn(random);

            horse.move();

            assertEquals(100 + 20 * random, horse.getDistance());
        }
    }
}
