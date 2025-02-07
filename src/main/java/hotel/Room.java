package hotel;

import java.util.Objects;

public class Room {

	private String id;
	private String description;

	public Room() {
	}

	public Room(String id, String description) {
		this.setId(id);
		this.setDescription(description);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean equals(Object o) {
		// Check if the objects are the same instance
		if (o == null)
			return false; // Check type compatibility
		if (getClass() != o.getClass())
			return false;
		Room room = (Room) o;
		return Objects.equals(id, room.id) && Objects.equals(description, room.description); // Compare `description`
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, description); // Compute a hash based on `id` and `description`
	}

	@Override
	public String toString() {
		return "Room{" + "id='" + id + '\'' + ", description='" + description + '\'' + '}';
	}

}
