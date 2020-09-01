package app.debata.com.debata.Chats;

/**
 * ChatObject will contain the actual message that is being sent.
 *
 * @author Nelaven Subaskaran
 * @since 1.0
 */
public class ChatObject {
    private String message;
    private Boolean currentUser;

    /**
     * Constructor for creating the message ot be sent
     *
     * @param message the actual text to be sent
     * @param currentUser the user you are texting
     */
    public ChatObject(String message, Boolean currentUser) {
        this.message = message;
        this.currentUser = currentUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Boolean currentUser) {
        this.currentUser = currentUser;
    }
}
