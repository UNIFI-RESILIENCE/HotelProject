package hotel;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class RoomTest {

    @Test
    public void testEqualsAndHashCodeForEqualObjects() {
        // Arrange
        Room room1 = new Room("1", "Deluxe Room");
        Room room2 = new Room("1", "Deluxe Room");

        // Assert
        assertThat(room1).isEqualTo(room2);
        assertThat(room1.hashCode()).hasSameHashCodeAs(room2.hashCode());
    }

    @Test
    public void testEqualsAndHashCodeForDifferentObjects() {
        // Arrange
        Room room1 = new Room("1", "Deluxe Room");
        Room room2 = new Room("2", "Standard Room");

        // Assert
        assertThat(room1).isNotEqualTo(room2);
        assertThat(room1.hashCode()).isNotEqualTo(room2.hashCode()); // Different hash codes
    }

    @Test
    public void testEqualsWithNull() {
        // Arrange
        Room room = new Room("1", "Deluxe Room");

        // Assert
        assertThat(room).isNotEqualTo(null); // Ensure non-equality to null
    }

    @Test
    public void testEqualsWithDifferentClass() {
        // Arrange
        Room room = new Room("1", "Deluxe Room");

        // Act & Assert
        assertThat(room).isNotEqualTo("Not a Room"); // Ensure non-equality to different class object
    }

    @Test
    public void testEqualsWithDifferentIdSameDescription() {
        // Arrange
        Room room1 = new Room("1", "Deluxe Room");
        Room room2 = new Room("2", "Deluxe Room");

        // Assert
        assertThat(room1).isNotEqualTo(room2); // Ensure id difference causes inequality
    }

    @Test
    public void testEqualsWithSameIdDifferentDescription() {
        // Arrange
        Room room1 = new Room("1", "Deluxe Room");
        Room room2 = new Room("1", "Standard Room");

        // Assert
        assertThat(room1).isNotEqualTo(room2); // Ensure description difference causes inequality
    }

    @Test
    public void testEqualsWithBothNullFields() {
        // Arrange
        Room room1 = new Room(null, null);
        Room room2 = new Room(null, null);

        // Assert
        assertThat(room1).isEqualTo(room2); // Ensure equality when both fields are null
        assertThat(room1.hashCode()).hasSameHashCodeAs(room2.hashCode());
    }

    @Test
    public void testEqualsWithOneFieldNull() {
        // Arrange
        Room room1 = new Room("1", null);
        Room room2 = new Room("1", "Standard Room");

        // Assert
        assertThat(room1).isNotEqualTo(room2); // Ensure non-equality when one field is null
    }

    @Test
    public void testEqualsWhenOnlyIdIsNull() {
        // Arrange
        Room room1 = new Room(null, "Standard Room");
        Room room2 = new Room("1", "Standard Room");

        // Assert
        assertThat(room1).isNotEqualTo(room2); // Ensure non-equality when id is null
    }

    @Test
    public void testToString() {
        // Arrange
        Room room = new Room("1", "Deluxe Room");

        // Assert
        assertThat(room.toString())
            .hasToString("Room{id='1', description='Deluxe Room'}");
    }
    
    
    @Test
    public void testEqualsHandlesNullAndDifferentClasses() {
        // Arrange
        Room room = new Room("1", "Deluxe Room");

        // Assert for `o == null`
        assertThat(room).isNotEqualTo(null); // Ensure non-equality with null

        // Assert for `getClass() != o.getClass()`
        assertThat(room.equals("Not a Room")).isFalse(); // Forces evaluation of `getClass() != o.getClass()`

        // Assert for same class comparison with different data
        Room otherRoom = new Room("2", "Standard Room");
        assertThat(room).isNotEqualTo(otherRoom); // Ensure it does not return true just due to being same class
    }

}
