package hotel;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;



public class RoomTest {


    @Test
    public  void testEqualsAndHashCodeForEqualObjects() {
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
        assertThat(room1.hashCode()).hasSameHashCodeAs((room1.hashCode()));
    }

    @Test
    public void testEqualsWithNullOrDifferentClass() {
        // Arrange
        Room room = new Room("1", "Deluxe Room");

        // Assert
        assertThat(room).isNotEqualTo(null).isNotEqualTo("Not a Room");
        
    }

    @Test
    public void testToString() {
        // Arrange
        Room room = new Room("1", "Deluxe Room");

        // Assert
        assertThat(room.toString())
            .hasToString("Room{id='1', description='Deluxe Room'}");
    }
}
