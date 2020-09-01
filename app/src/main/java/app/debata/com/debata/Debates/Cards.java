package app.debata.com.debata.Debates;

/**
 * The actual card that will have a debate to display.
 *
 * @author Nelaven Subaskaran
 * @since 1.0
 */
public class Cards {
    private String userId;
    private String name;

    /**
     * Constructor for each debate card.
     *
     * @param userId the key of the debate
     * @param name the value of the debate
     */
    public Cards(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userID) {
        this.userId = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }
}
