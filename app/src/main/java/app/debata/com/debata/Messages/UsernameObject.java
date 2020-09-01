package app.debata.com.debata.Messages;

/**
 * UsernameObject will contain the opposite user's name.
 *
 * @author Nelaven Subaskaran
 * @since 1.0
 */
public class UsernameObject {
    private String username;

    public UsernameObject(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
