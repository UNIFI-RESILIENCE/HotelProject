package hotel;

public class RoomRepositoryException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public RoomRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
