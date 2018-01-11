package cr.chat.server.room;

public class ChatRoomExistException extends Exception {
    public ChatRoomExistException(String message) {
        super(message);
    }
}
